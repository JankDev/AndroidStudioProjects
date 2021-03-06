package pl.agh.robert_kraut_cz_8.domain

import pl.agh.robert_kraut_cz_8.config.retrofit
import pl.agh.robert_kraut_cz_8.model.CurrencyDetail
import pl.agh.robert_kraut_cz_8.model.CurrencyOverview
import pl.agh.robert_kraut_cz_8.model.CurrencyTable

class CurrencyService {
    private val nbpApi = retrofit.create(NBPApi::class.java)

    suspend fun getCurrencyListings(): List<CurrencyOverview> {
        val tableA = try {
            nbpApi.getCurrencyTable("A", 2)
        } catch (ex: Exception) {
            throw RuntimeException(ex.message)
        }
        val tableB = try {
            nbpApi.getCurrencyTable("B", 2)
        } catch (ex: Exception) {
            throw RuntimeException(ex.message)
        }
        return if (tableA.isSuccessful && tableB.isSuccessful) {
            val tableARates = getRatesForTable(tableA.body()!!)
            val tableBRates = getRatesForTable(tableB.body()!!)
            tableARates + tableBRates
        } else {
            throw RuntimeException(tableA.errorBody()?.string())
        }

    }

    suspend fun getCurrencyDetails(currencyCode: String, currencyTable: String): CurrencyDetail {
        return nbpApi.getCurrency(currencyTable, currencyCode, 30).let {
            if (it.isSuccessful) it.body()!!
            else throw RuntimeException(it.errorBody()?.string())
        }
    }

    private fun getRatesForTable(table: List<CurrencyTable>): List<CurrencyOverview> {
        return table[1].rates
            .map { currencyOverview ->
                currencyOverview.copy(up = currencyOverview.mid > table[0].rates.first { it.code == currencyOverview.code }.mid)
            }
            .map { it.withTable(table[0].table) }
    }
}