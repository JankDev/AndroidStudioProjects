package pl.agh.robert_kraut_cz_8.domain

import android.widget.Toast
import pl.agh.robert_kraut_cz_8.config.retrofit
import pl.agh.robert_kraut_cz_8.model.CurrencyRate
import pl.agh.robert_kraut_cz_8.model.CurrencyTable

class CurrencyService {
    private val nbpApi = retrofit.create(NBPApi::class.java)

    suspend fun getCurrencyListings(): List<CurrencyRate> {
        val tableA = nbpApi.getCurrencyTable("A")
        val tableB = nbpApi.getCurrencyTable("B")
        return if(tableA.isSuccessful && tableB.isSuccessful){
            (tableA.body()!! + tableB.body()!!)
                .flatMap { it.rates }
        } else {
            throw RuntimeException(tableA.message())
        }

    }

}