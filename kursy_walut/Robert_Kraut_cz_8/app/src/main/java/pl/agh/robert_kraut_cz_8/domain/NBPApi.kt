package pl.agh.robert_kraut_cz_8.domain

import pl.agh.robert_kraut_cz_8.model.CurrencyDetail
import pl.agh.robert_kraut_cz_8.model.CurrencyTable
import pl.agh.robert_kraut_cz_8.model.GoldPrice
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

typealias NumberOfDays = Int

interface NBPApi {

    @GET("exchangerates/tables/{table}/last/{topCount}")
    @Headers("Accept: application/json")
    suspend fun getCurrencyTable(
        @Path("table") table: String,
        @Path("topCount") topCount: NumberOfDays = 1
    ): Response<List<CurrencyTable>>

    @GET("exchangerates/rates/{table}/{code}/last/{topCount}")
    @Headers("Accept: application/json")
    suspend fun getCurrency(
        @Path("table") table: String,
        @Path("code") code: String,
        @Path("topCount") topCount: NumberOfDays
    ): Response<CurrencyDetail>

    @GET("cenyzlota/last/{topCount}")
    @Headers("Accept: application/json")
    suspend fun getGoldPrice(
        @Path("topCount") topCount: NumberOfDays
    ): Response<List<GoldPrice>>
}