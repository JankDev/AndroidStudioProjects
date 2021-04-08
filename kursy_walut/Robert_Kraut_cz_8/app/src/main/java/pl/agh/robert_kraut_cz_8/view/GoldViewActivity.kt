package pl.agh.robert_kraut_cz_8.view

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.ValueDataEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import pl.agh.robert_kraut_cz_8.R
import pl.agh.robert_kraut_cz_8.domain.GoldService
import pl.agh.robert_kraut_cz_8.model.GoldPrice

class GoldViewActivity : AppCompatActivity() {
    private val goldService = GoldService()
    private var goldPrices: List<GoldPrice> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) = runBlocking {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gold_view)

        goldPrices = withContext(Dispatchers.Default) { goldService.getGoldListings() }

        findViewById<TextView>(R.id.goldPrice).text = goldPrices.lastOrNull()?.cena.toString()

        val goldChart = findViewById<AnyChartView>(R.id.goldPriceChart)
        val goldLineChart = AnyChart.line()
        goldLineChart.data(goldPrices.map { rate ->
            ValueDataEntry(
                rate.data,
                rate.cena
            )
        })

        goldChart.setChart(goldLineChart)
    }
}