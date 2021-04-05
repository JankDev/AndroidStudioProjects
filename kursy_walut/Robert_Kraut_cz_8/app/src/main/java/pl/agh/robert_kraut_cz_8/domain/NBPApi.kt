package pl.agh.robert_kraut_cz_8.domain

import pl.agh.robert_kraut_cz_8.model.CurrencyTable
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface NBPApi {

    @GET("exchangerates/tables/{table}")
    @Headers("Accept: application/json")
    suspend fun getCurrencyTable(@Path("table") table: String): Response<List<CurrencyTable>>
}