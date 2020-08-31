package cache

import co.example.envidual.finance.touchlab.db.CompaniesNews
import domain.data.CompanyData
import kotlinx.coroutines.flow.Flow

interface CompanyDataCacheInterface {
    suspend fun insert(companyData: List<CompanyData>)
    suspend fun updateIsFavouriteByTicker(isFavourite: Boolean, ticker: String)
    suspend fun updateIsSearchedByTicker(isSearched: Boolean, ticker: String)
    suspend fun updateLastSearchedByTicker(lastSearched: Long, ticker: String)
    fun selectByTicker(ticker:String) : List<CompanyData>
    fun selectCompaniesToUpdate(lastSearched: Long) : List<CompanyData>
    fun selectAllFavourites() : List<CompanyData>
    fun selectAllFavouritesAsFlow() : Flow<List<CompanyData>>
    fun selectAllSearchesAsFlow() : Flow<List<CompanyData>>
    suspend fun deleteAll()
    suspend fun deleteByTicker(ticker: String)
}