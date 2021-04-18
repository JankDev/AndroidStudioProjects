package pl.agh.coronatracker.domain

import kotlinx.serialization.ExperimentalSerializationApi
import pl.agh.coronatracker.config.CONTENT_TYPE
import pl.agh.coronatracker.model.CoronaSummary
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface CoronaApi {
    @ExperimentalSerializationApi
    @GET("summary")
    @Headers("Accept: $CONTENT_TYPE")
    suspend fun getSummary(): Response<CoronaSummary>
}