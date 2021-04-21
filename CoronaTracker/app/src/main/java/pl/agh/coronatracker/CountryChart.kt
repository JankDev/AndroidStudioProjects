package pl.agh.coronatracker

import android.util.Log
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Cartesian
import com.anychart.core.cartesian.series.Line
import com.anychart.data.Set
import com.anychart.enums.Anchor
import com.anychart.enums.MarkerType
import com.anychart.enums.TooltipPositionMode
import com.anychart.graphics.vector.Stroke
import pl.agh.coronatracker.model.CountryTotalSummary
import pl.agh.coronatracker.model.CountryWithTotalSummaries
import java.time.format.DateTimeFormatter

object CountryChart {

    fun createChart(countrySummary: CountryWithTotalSummaries): Cartesian {
        val cartesian = AnyChart.line()

        cartesian.padding(10, 20, 5, 20)
        cartesian.crosshair().enabled(true)
        cartesian.crosshair()
            .yLabel(true)
            .yStroke(null as Stroke?, null, null, null as String?, null as String?)

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT)
        cartesian.xAxis(0).labels().padding(5.0, 5.0, 5.0, 5.0)

        val seriesData = countrySummary.summaries.map { CoronaDataEntry.fromDailyRate(it) }.takeLast(365)

        val set = Set.instantiate()
        set.data(seriesData)

        val confirmedSeriesMapping = set.mapAs("{ x: 'x', value: 'value' }")
        val deathsSeriesMapping = set.mapAs("{ x: 'x', value: 'deaths' }")
        val recoveredSeriesMapping = set.mapAs("{ x: 'x', value: 'recovered' }")

        val confirmedSeries: Line = cartesian.line(confirmedSeriesMapping)
        confirmedSeries.name("Confirmed")
        confirmedSeries.hovered().markers()
            .type(MarkerType.CIRCLE)
            .size(4.0)
        confirmedSeries.tooltip()
            .position("right")
            .anchor(Anchor.LEFT_CENTER)
            .offsetX(5.0)
            .offsetY(5.0)

        val deathSeries = cartesian.line(deathsSeriesMapping)
        deathSeries.name("Deaths")
        deathSeries.hovered().markers().enabled(true)
        deathSeries.hovered().markers()
            .type(MarkerType.CIRCLE)
            .size(4.0)
        deathSeries.tooltip()
            .position("right")
            .anchor(Anchor.LEFT_CENTER)
            .offsetX(5.0)
            .offsetY(5.0)

        val recoveredSeries = cartesian.line(recoveredSeriesMapping)
        recoveredSeries.name("Recovered")
        recoveredSeries.hovered().markers().enabled(true)
        recoveredSeries.hovered().markers()
            .type(MarkerType.CIRCLE)
            .size(4.0)
        recoveredSeries.tooltip()
            .position("right")
            .anchor(Anchor.LEFT_CENTER)
            .offsetX(5.0)
            .offsetY(5.0)

        cartesian.legend().enabled(true)
        cartesian.legend().fontSize(13.0)
        cartesian.legend().padding(0.0, 0.0, 10.0, 0.0)

        return cartesian
    }


    class CoronaDataEntry(x: String, confirmed: Long, deaths: Long, recovered: Long) :
        ValueDataEntry(x, confirmed) {
        init {
            setValue("deaths", deaths)
            setValue("recovered", recovered)
        }

        companion object {
            fun fromDailyRate(totalSummary: CountryTotalSummary): CoronaDataEntry {
                val date = totalSummary.date.format(DateTimeFormatter.ofPattern("dd.MM"))
                return CoronaDataEntry(
                    x = date,
                    confirmed = totalSummary.confirmed.toLong(),
                    deaths = totalSummary.deaths.toLong(),
                    recovered = totalSummary.recovered.toLong()
                )
            }
        }
    }
}