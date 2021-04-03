package pl.agh.robert_kraut_cz_8

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NBPApi {

    @GET("tables/")
    fun getTable(@Query("table") table: String): Call<List<CurrencyTable>>
}