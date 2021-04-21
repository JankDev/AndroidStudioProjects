package pl.agh.coronatracker.domain

import kotlinx.serialization.ExperimentalSerializationApi
import pl.agh.coronatracker.config.CONTENT_TYPE
import pl.agh.coronatracker.model.CoronaSummary
import pl.agh.coronatracker.model.CountryTotalSummary
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface CoronaApi {
    @ExperimentalSerializationApi
    @GET("summary")
    @Headers("Accept: $CONTENT_TYPE")
    suspend fun getSummary(): Response<CoronaSummary>

    @ExperimentalSerializationApi
    @GET("total/country/{country}")
    @Headers("Accept: $CONTENT_TYPE")
    suspend fun countrySummary(@Path("country") country: String): Response<List<CountryTotalSummary>>
}