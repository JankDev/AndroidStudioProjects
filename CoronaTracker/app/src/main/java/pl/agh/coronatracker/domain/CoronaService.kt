package pl.agh.coronatracker.domain

import android.content.Context
import pl.agh.coronatracker.config.AppDatabase
import pl.agh.coronatracker.config.coronaApi
import pl.agh.coronatracker.entity.CoronaSummary
import pl.agh.coronatracker.entity.CountrySummary
import pl.agh.coronatracker.view_model.CoronaSummaryViewModel
import pl.agh.coronatracker.view_model.CountryWithTotalSummaries
import java.util.*

class CoronaService(context: Context) {
    private val coronaDao = AppDatabase.getInstance(context).coronaDao()

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

                coronaDao.insertCoronaSummary(
                    CoronaSummary(
                        summary.id,
                        summary.global,
                        summary.date.toEpochMilliseconds()
                    )
                )
                coronaDao.insertCountrySummaries(summary.countries.map {
                    CountrySummary(
                        UUID.randomUUID().toString(),
                        it.country,
                        it.countryCode,
                        it.slug,
                        it.newConfirmed,
                        it.totalConfirmed,
                        it.newDeaths,
                        it.totalDeaths,
                        it.newRecovered,
                        it.totalRecovered,
                    )
                })
            }
            ?.let { it.toViewModel() }
            ?: coronaDao.getCoronaWithCountriesSummary()
                .firstOrNull()
                .also { println(it) }
                ?.toViewModel()
            ?: throw RuntimeException("Error loading data")
    }

    suspend fun getCountrySummary(countryName: String) = coronaApi
        .countrySummary(countryName)
        .takeIf { it.isSuccessful }
        ?.body()
        ?.let { CountryWithTotalSummaries.acceptCountryData(countryName, it) }
        ?: throw RuntimeException("Error loading data")
}