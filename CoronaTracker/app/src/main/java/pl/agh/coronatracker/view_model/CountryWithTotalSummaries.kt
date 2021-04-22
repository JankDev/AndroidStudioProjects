package pl.agh.coronatracker.view_model

import kotlinx.serialization.ExperimentalSerializationApi
import pl.agh.coronatracker.dto.CountryTotalSummaryDTO

@ExperimentalSerializationApi
data class CountryWithTotalSummaries private constructor(
    val country: String,
    val summaries: List<CountryTotalSummaryDTO>,
    val today: CountryTotalSummaryDTO
) {

    companion object {
        @ExperimentalSerializationApi
        fun acceptCountryData(
            country: String,
            summaries: List<CountryTotalSummaryDTO>
        ): CountryWithTotalSummaries? {
            val firstNonEmptyIndex =
                summaries.indexOfFirst { it.isEmpty() } // most of the data is empty. get skip data until first day that data is not empty
            val correctedData = summaries.drop(firstNonEmptyIndex).zipWithNext()
                .fold(emptyList<CountryTotalSummaryDTO>(),
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