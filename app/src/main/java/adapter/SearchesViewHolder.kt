package adapter

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.envidual.finance.touchlab.databinding.SearchesCardviewBinding
import domain.data.CompanyData
import fragments.CheckBoxCompany

class SearchesViewHolder constructor(
    private val binding: SearchesCardviewBinding,
    private val onCheckboxClicked: MutableLiveData<CheckBoxCompany>
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(companyData: CompanyData) {
        binding.companyName.text =  companyData.name
        binding.companyTicker.text = companyData.ticker
        if(companyData.isFavourite != null)
            binding.favouritesCheckbox.isChecked = companyData.isFavourite!!
        binding.favouritesCheckbox.setOnClickListener {
            if(binding.favouritesCheckbox.isChecked){
                onCheckboxClicked.postValue(CheckBoxCompany(true, companyData))
            } else {
                onCheckboxClicked.postValue(CheckBoxCompany(false, companyData))
            }
        }
    }
}