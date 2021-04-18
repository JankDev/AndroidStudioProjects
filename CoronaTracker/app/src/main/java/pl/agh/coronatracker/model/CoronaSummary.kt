package pl.agh.coronatracker.model

import kotlinx.datetime.Instant
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import pl.agh.coronatracker.util.InstantSerializer

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