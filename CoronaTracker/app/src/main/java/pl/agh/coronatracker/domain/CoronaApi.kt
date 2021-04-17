package pl.agh.coronatracker.domain

import pl.agh.coronatracker.model.CoronaSummary
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface CoronaApi {
    @GET("summary")
    @Headers("Accept: application/json")
    suspend fun getSummary(): Response<CoronaSummary>
}