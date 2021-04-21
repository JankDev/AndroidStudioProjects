package pl.agh.coronatracker.dto

import androidx.room.Entity
import com.blongho.country_data.World
import kotlinx.datetime.Instant
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import pl.agh.coronatracker.domain.CountryFlagRetriever
import pl.agh.coronatracker.entity.CountrySummary
import pl.agh.coronatracker.util.InstantSerializer
import pl.agh.coronatracker.util.LocalDateSerializer
import pl.agh.coronatracker.view_model.CoronaRegionSummaryViewModel
import pl.agh.coronatracker.view_model.CoronaSummaryViewModel
import java.time.LocalDate
import java.util.*

interface Regionalizable {
    fun toRegion(): CoronaRegionSummaryViewModel
}

@Entity
@ExperimentalSerializationApi
@Serializable
data class CoronaSummaryDTO(
    @SerialName("ID") val id: String,
    @SerialName("Global") val global: GlobalSummaryDTO,
    @SerialName("Countries") val countries: List<CountrySummaryDTO>,
    @SerialName("Date") @Serializable(with = InstantSerializer::class) val date: Instant
) {
    fun toViewModel() =
        CoronaSummaryViewModel(listOf(global.toRegion()) + countries.map { it.toRegion() })
}

@ExperimentalSerializationApi
@Serializable
data class GlobalSummaryDTO(
    @SerialName("NewConfirmed") val newConfirmed: Int,
    @SerialName("TotalConfirmed") val totalConfirmed: Int,
    @SerialName("NewDeaths") val newDeaths: Int,
    @SerialName("TotalDeaths") val totalDeaths: Int,
    @SerialName("NewRecovered") val newRecovered: Int,
    @SerialName("TotalRecovered") val totalRecovered: Int
) : Regionalizable {
    override fun toRegion() =
        CoronaRegionSummaryViewModel(World.getWorldFlag(), "Global", newConfirmed)

}

@ExperimentalSerializationApi
@Serializable
data class CountrySummaryDTO(
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

    fun toEntity(coronaSummaryId: Long) = CountrySummary(
        0,
        coronaSummaryId,
        country,
        countryCode,
        slug,
        newConfirmed,
        totalConfirmed,
        newDeaths,
        totalDeaths,
        newRecovered,
        totalRecovered
    )
}

@ExperimentalSerializationApi
@Serializable
data class CountryTotalSummaryDTO(
    @SerialName("Country") val country: String,
    @SerialName("Deaths") val deaths: Int,
    @SerialName("Recovered") val recovered: Int,
    @SerialName("Confirmed") val confirmed: Int,
    @SerialName("Date") @Serializable(with = LocalDateSerializer::class) val date: LocalDate
) {
    fun isEmpty(): Boolean = deaths == 0 && recovered == 0 && confirmed == 0
}

