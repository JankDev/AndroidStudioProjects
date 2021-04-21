package pl.agh.coronatracker.domain

import android.content.Context
import kotlinx.serialization.ExperimentalSerializationApi
import pl.agh.coronatracker.config.AppDatabase
import pl.agh.coronatracker.config.coronaApi
import pl.agh.coronatracker.entity.CoronaSummary
import pl.agh.coronatracker.entity.CountrySummary
import pl.agh.coronatracker.view_model.CoronaSummaryViewModel
import pl.agh.coronatracker.view_model.CountryWithTotalSummaries
import java.util.*

class CoronaService(context: Context) {
    private val coronaDao = AppDatabase.getInstance(context).coronaDao()

    @ExperimentalSerializationApi
    suspend fun getCoronaSummary(): CoronaSummaryViewModel {
        return coronaApi.runCatching {
            getSummary()
        }
            .getOrNull()
            ?.takeIf { it.isSuccessful }
            ?.body()
            ?.also { summary ->
                coronaDao.deleteAllFromCountry()
                coronaDao.deleteAllFromCorona()

                val summaryId = coronaDao.insertCoronaSummary(
                    CoronaSummary(0, summary.global, summary.date.toEpochMilliseconds())
                )
                coronaDao.insertCountrySummaries(summary.countries.map { it.toEntity(summaryId) })
            }
            ?.toViewModel()
            ?: coronaDao.getCoronaWithCountriesSummary()
                .firstOrNull()
                ?.toViewModel()
            ?: throw RuntimeException("Error loading data")
    }

    @ExperimentalSerializationApi
    suspend fun getCountrySummary(countryName: String) = coronaApi
        .countrySummary(countryName)
        .takeIf { it.isSuccessful }
        ?.body()
        ?.let { CountryWithTotalSummaries.acceptCountryData(countryName, it) }
        ?: throw RuntimeException("Error loading data")
}