package cache

import co.example.CompaniesNews
import co.touchlab.stately.freeze
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import domain.data.CompanyNews
import domain.use_cases.backgroundDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import org.koin.core.KoinComponent
import org.koin.core.inject

class CompanyNewsCache(
    private val dbHelper: DatabaseHelper
) : CompanyNewsCacheInterface{

    override suspend fun insert(companiesNews: List<CompanyNews>) {
        dbHelper.insertCompaniesNews(companiesNews)
    }

    override fun selectByTicker(ticker: String): List<CompanyNews> {
        return dbHelper.selectByTickerFromCompaniesNews(ticker)
            .executeAsList()
            .mapCompaniesNewsToCompanyNews()
            .freeze()
    }

    override fun selectByTickerAsFlow(ticker: String): Flow<List<CompanyNews>> {
        return dbHelper.selectByTickerFromCompaniesNewsAsFlow(ticker)
            .asFlow()
            .mapToList()
            .flowOn(backgroundDispatcher)
            .mapCompaniesNewsToCompanyNews()
            .freeze()
    }

    override suspend fun deleteAll() {
        dbHelper.deleteAllFromCompanyNews()
    }

    override suspend fun deleteByTicker(ticker: String) {
        dbHelper.deleteCompanyNewsByTicker(ticker)
    }

    override suspend fun deleteByTickerAndDateTime(ticker: String, dateTime: Long) {
        dbHelper.deleteCompanyNewsByTickerAndTimestamp(ticker, dateTime)
    }

    //    helper Functions for mapping flows of companiesNews to companyNews
    fun List<CompaniesNews>.mapCompaniesNewsToCompanyNews() : List<domain.data.CompanyNews>{
        val listOfCompanyData = mutableListOf<domain.data.CompanyNews>()
        for (company in this)
            listOfCompanyData.add(CompanyNews(company.ticker, company.category, company.datetime, company.headline, company.id, company.image, company.related, company.source, company.summary, company.url))
        return listOfCompanyData
    }

    fun Flow<List<CompaniesNews>>.mapCompaniesNewsToCompanyNews() : Flow<List<domain.data.CompanyNews>> = map {
        val listOfCompanyData = mutableListOf<domain.data.CompanyNews>()
        for (company in it)
            listOfCompanyData.add(CompanyNews(company.ticker, company.category, company.datetime, company.headline, company.id, company.image, company.related, company.source, company.summary, company.url))
        return@map listOfCompanyData
    }
}