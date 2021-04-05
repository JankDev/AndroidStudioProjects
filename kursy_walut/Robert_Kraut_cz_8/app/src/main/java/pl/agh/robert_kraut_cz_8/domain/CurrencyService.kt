package pl.agh.robert_kraut_cz_8.domain

import pl.agh.robert_kraut_cz_8.config.retrofit
import pl.agh.robert_kraut_cz_8.model.CurrencyDetail
import pl.agh.robert_kraut_cz_8.model.CurrencyOverview
import pl.agh.robert_kraut_cz_8.model.CurrencyTable

class CurrencyService {
    private val nbpApi = retrofit.create(NBPApi::class.java)

    suspend fun getCurrencyListings(): List<CurrencyOverview> {
        val tableA = nbpApi.getCurrencyTable("A")
        val tableB = nbpApi.getCurrencyTable("B")
        return if (tableA.isSuccessful && tableB.isSuccessful) {
            (tableA.body()!! + tableB.body()!!)
                .flatMap { table -> table.rates.map { it.withTable(table.table) } }
        } else {
            throw RuntimeException(tableA.message())
        }

    }

    suspend fun getCurrencyDetails(currencyCode: String, currencyTable: String): CurrencyDetail {
        return nbpApi.getCurrency(currencyTable, currencyCode, 30).let {
            if (it.isSuccessful) it.body()!!
            else throw RuntimeException(it.message())
        }
    }

}