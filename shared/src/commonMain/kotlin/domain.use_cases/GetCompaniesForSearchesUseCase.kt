package domain.use_cases

import cache.CompanyDataCacheInterface
import domain.data.CompanyData
import kotlinx.coroutines.flow.Flow
import org.koin.core.KoinComponent
import org.koin.core.inject
import cache.DatabaseHelper

class GetCompaniesForSearchesUseCase(private val companyDataCache : CompanyDataCacheInterface): KoinComponent {

    suspend fun invoke(): Flow<List<CompanyData>> =
         companyDataCache.selectAllSearchesAsFlow()
}
