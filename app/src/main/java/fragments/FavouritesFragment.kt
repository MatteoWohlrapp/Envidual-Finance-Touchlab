package fragments

import adapter.FavouritesAdapter
import adapter.ItemSpacingDecoration
import android.graphics.Canvas
import android.media.MediaRouter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.envidual.finance.touchlab.R
import kotlinx.android.synthetic.main.favourites_fragment.*
import viewmodel.FavouritesViewModel

class FavouritesFragment : Fragment(){

    lateinit var favouritesViewModel : FavouritesViewModel

    lateinit var favouritesAdapter: FavouritesAdapter

    lateinit var itemTouchHelperCallback: ItemTouchHelper.SimpleCallback

    override fun onCreate(savedInstanceState: Bundle?) {

        favouritesViewModel = ViewModelProviders.of(this).get(FavouritesViewModel::class.java)

        super.onCreate(savedInstanceState)

        favouritesViewModel.getCompanyDataForFavourites()

//        callback for the swipe gesture
        itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val companyData = favouritesAdapter.currentList[position]
                Log.d("Swipe", "Swiped position: $position")
                Log.d("Swipe", "Company name: ${companyData.name}")
                favouritesViewModel.deleteCompanyFromFavourites(companyData)
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.favourites_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favouritesAdapter= FavouritesAdapter()
        favouritesViewModel.favourites.observe(viewLifecycleOwner, Observer { favouritesAdapter.submitList(it)})
        favouritesViewModel.favouritesProgressBar.observe(viewLifecycleOwner, Observer {
            if(it)
                favourites_progress_bar.visibility = View.VISIBLE
            else
                favourites_progress_bar.visibility = View.GONE
        })

        favourites_recycler_view.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(ItemSpacingDecoration())
            adapter = favouritesAdapter
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(favourites_recycler_view)
    }
}