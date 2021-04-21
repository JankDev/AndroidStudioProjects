package pl.agh.coronatracker.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import pl.agh.coronatracker.domain.CountryFlagRetriever
import pl.agh.coronatracker.dto.GlobalSummaryDTO
import pl.agh.coronatracker.dto.Regionalizable
import pl.agh.coronatracker.view_model.CoronaRegionSummaryViewModel
import pl.agh.coronatracker.view_model.CoronaSummaryViewModel

@Entity
data class CoronaSummary(
    @PrimaryKey val coronaSummaryId: String,
    @Embedded val global: GlobalSummaryDTO,
    val date: Long
)


@Entity
data class CountrySummary(
    @PrimaryKey val countrySummaryId: String,
    val country: String,
    val countryCode: String,
    val slug: String,
    val newConfirmed: Int,
    val totalConfirmed: Int,
    val newDeaths: Int,
    val totalDeaths: Int,
    val newRecovered: Int,
    val totalRecovered: Int
) : Regionalizable {
    override fun toRegion() = CoronaRegionSummaryViewModel(
        CountryFlagRetriever.getFlagForCode(countryCode),
        country,
        newConfirmed
    )

}

data class CoronaWithCountriesSummary(
    @Embedded val coronaSummary: CoronaSummary,
    @Relation(parentColumn = "coronaSummaryId", entityColumn = "countrySummaryId", entity = CountrySummary::class) val countries: List<CountrySummary>
) {
    fun toViewModel() =
        CoronaSummaryViewModel(listOf(coronaSummary.global.toRegion()) + countries.map { it.toRegion() })
}