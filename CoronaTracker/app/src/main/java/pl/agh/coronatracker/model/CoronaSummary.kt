package pl.agh.coronatracker.model

import android.provider.Settings.Global.getString
import com.blongho.country_data.World
import kotlinx.datetime.Instant
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import pl.agh.coronatracker.R
import pl.agh.coronatracker.domain.CountryFlagRetriever
import pl.agh.coronatracker.util.InstantSerializer
import pl.agh.coronatracker.util.LocalDateSerializer
import pl.agh.coronatracker.view_model.CoronaRegionSummaryViewModel
import pl.agh.coronatracker.view_model.CoronaSummaryViewModel
import java.time.LocalDate

interface Regionalizable {
    fun toRegion(): CoronaRegionSummaryViewModel
}

@ExperimentalSerializationApi
@Serializable
data class CoronaSummary(
    @SerialName("ID") val id: String,
    @SerialName("Message") val message: String,
    @SerialName("Global") val global: GlobalSummary,
    @SerialName("Countries") val countries: List<CountrySummary>,
    @SerialName("Date") @Serializable(with = InstantSerializer::class) val date: Instant
) {
    fun toViewModel() =
        CoronaSummaryViewModel(listOf(global.toRegion()) + countries.map { it.toRegion() })
}

@ExperimentalSerializationApi
@Serializable
data class GlobalSummary(
    @SerialName("NewConfirmed") val newConfirmed: Int,
    @SerialName("TotalConfirmed") val totalConfirmed: Int,
    @SerialName("NewDeaths") val newDeaths: Int,
    @SerialName("TotalDeaths") val totalDeaths: Int,
    @SerialName("NewRecovered") val newRecovered: Int,
    @SerialName("TotalRecovered") val totalRecovered: Int,
    @SerialName("Date") @Serializable(with = InstantSerializer::class) val date: Instant
) : Regionalizable {
    override fun toRegion() =
        CoronaRegionSummaryViewModel(World.getWorldFlag(), "Global", newConfirmed)

}

@ExperimentalSerializationApi
@Serializable
data class CountrySummary(
    @SerialName("ID") val id: String,
    @SerialName("Country") val country: String,
    @SerialName("CountryCode") val countryCode: String,
    @SerialName("Slug") val slug: String,
    @SerialName("NewConfirmed") val newConfirmed: Int,
    @SerialName("TotalConfirmed") val totalConfirmed: Int,
    @SerialName("NewDeaths") val newDeaths: Int,
    @SerialName("TotalDeaths") val totalDeaths: Int,
    @SerialName("NewRecovered") val newRecovered: Int,
    @SerialName("TotalRecovered") val totalRecovered: Int,
    @SerialName("Date") @Serializable(with = InstantSerializer::class) val date: Instant,
) : Regionalizable {
    override fun toRegion() = CoronaRegionSummaryViewModel(
        CountryFlagRetriever.getFlagForCode(countryCode),
        country,
        newConfirmed
    )
}

@ExperimentalSerializationApi
@Serializable
data class CountryTotalSummary(
    @SerialName("Country") val country: String,
    @SerialName("Deaths") val deaths: Int,
    @SerialName("Recovered") val recovered: Int,
    @SerialName("Confirmed") val confirmed: Int,
    @SerialName("Date") @Serializable(with = LocalDateSerializer::class) val date: LocalDate
) {
    fun isEmpty(): Boolean = deaths == 0 && recovered == 0 && confirmed == 0
}

data class CountryWithTotalSummaries private constructor(
    val country: String,
    val summaries: List<CountryTotalSummary>,
    val today: CountryTotalSummary
) {

    companion object {
        fun acceptCountryData(
            country: String,
            summaries: List<CountryTotalSummary>
        ): CountryWithTotalSummaries? {
            val firstNonEmptyIndex =
                summaries.indexOfFirst { it.isEmpty() } // most of the data is empty. get skip data until first day that data is not empty
            val correctedData = summaries.drop(firstNonEmptyIndex).zipWithNext()
                .fold(emptyList<CountryTotalSummary>(),
                    { acc, (a, b) ->
                        acc + b.copy(
                            deaths = Math.max(b.deaths - a.deaths, 0),
                            recovered = Math.max(b.recovered - a.recovered, 0),
                            confirmed = Math.max(b.confirmed - a.confirmed, 0)
                        )
                    })

            return if (firstNonEmptyIndex == -1) null
            else CountryWithTotalSummaries(
                country,
                correctedData,
                correctedData.last()
            )
        }
    }
}