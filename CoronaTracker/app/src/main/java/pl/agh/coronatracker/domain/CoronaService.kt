package pl.agh.coronatracker.domain

import kotlinx.serialization.ExperimentalSerializationApi
import pl.agh.coronatracker.config.CONTENT_TYPE
import pl.agh.coronatracker.config.coronaApi
import pl.agh.coronatracker.model.CountryTotalSummary
import pl.agh.coronatracker.model.CountryWithTotalSummaries
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

object CoronaService {
    suspend fun getCoronaSummary() = coronaApi
        .getSummary()
        .takeIf { it.isSuccessful }
        ?.body()
        ?.let { it.toViewModel() }
        ?: throw RuntimeException("Error loading data")

    suspend fun getCountrySummary(countryName: String) = coronaApi
        .countrySummary(countryName)
        .takeIf { it.isSuccessful }
        ?.body()
        ?.let { CountryWithTotalSummaries.acceptCountryData(countryName, it) }
        ?: throw RuntimeException("Error loading data")
}