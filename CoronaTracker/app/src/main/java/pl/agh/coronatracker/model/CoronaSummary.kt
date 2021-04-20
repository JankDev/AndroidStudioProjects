package pl.agh.coronatracker.model

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import pl.agh.coronatracker.util.FlagPicker
import pl.agh.coronatracker.util.InstantSerializer
import pl.agh.coronatracker.util.LocalDateSerializer
import java.time.LocalDate

@ExperimentalSerializationApi
@Serializable
data class CoronaSummary(
    @SerialName("ID") val id: String,
    @SerialName("Message") val message: String,
    @SerialName("Global") val global: GlobalSummary,
    @SerialName("Countries") val countries: List<CountrySummary>,
    @SerialName("Date") @Serializable(with = InstantSerializer::class) val date: Instant
)

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
)

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
)

@ExperimentalSerializationApi
@Serializable
data class CountryDetails(
    @SerialName("Country") val country: String,
    @SerialName("Slug") val slug: String,
    @SerialName("ISO2") val iso2: String
)

@ExperimentalSerializationApi
@Serializable
data class CountryTotalSummary(
    @SerialName("Country") val country: String,
    @SerialName("Deaths") val deaths: Int,
    @SerialName("Recovered") val recovered: Int,
    @SerialName("Active") val active: Int,
    @SerialName("Date") @Serializable(with = LocalDateSerializer::class) val date: LocalDate
) {
    fun isEmpty(): Boolean = deaths != 0 || recovered != 0 || active != 0
}

data class CountryWithTotalSummaries private constructor(
    val country: CountryDetails,
    val summaries: List<CountryTotalSummary>,
    val countryFlag: Int,
    val today: CountryTotalSummary
) {

    companion object {
        fun acceptCountryData(
            country: CountryDetails,
            summaries: List<CountryTotalSummary>
        ): CountryWithTotalSummaries? {
            val firstNonEmptyIndex = summaries.indexOfFirst { it.isEmpty() } // most of the data is empty. get skip data until first day that data is not empty
            return if (firstNonEmptyIndex == -1) null
            else CountryWithTotalSummaries(
                country,
                summaries.drop(firstNonEmptyIndex),
                FlagPicker.getFlag(country.country),
                summaries.last()
            )
        }
    }
}
