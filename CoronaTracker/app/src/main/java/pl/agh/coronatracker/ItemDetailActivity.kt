package pl.agh.coronatracker

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.anychart.APIlib
import com.anychart.AnyChartView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import pl.agh.coronatracker.domain.CoronaService
import pl.agh.coronatracker.model.CountryWithTotalSummaries

class ItemDetailActivity : AppCompatActivity() {

    internal lateinit var countryName: String
    internal lateinit var country: CountryWithTotalSummaries

    internal lateinit var confirmed: TextView
    internal lateinit var deaths: TextView
    internal lateinit var recovered: TextView

    internal lateinit var goBackButton: Button

    internal lateinit var chart: AnyChartView

    val instance: ItemDetailActivity = this

    override fun onCreate(savedInstanceState: Bundle?) = runBlocking {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)
        countryName = intent.getStringExtra("countryName")!!
        confirmed = findViewById(R.id.confirmedCases)
        deaths = findViewById(R.id.deathCases)
        recovered = findViewById(R.id.recoveredCases)
        chart = findViewById(R.id.chart)
        goBackButton = findViewById(R.id.goBackButton)
        supervisorScope {
            try {
                country = withContext(Dispatchers.IO) {
                    CoronaService.getCountrySummary(countryName)
                }
                showData()
            } catch (ex: Exception) {
                instance.finish()
                ex.printStackTrace()
            }
        }
    }


    private fun showData() {
        val today = country.today
        goBackButton.setOnClickListener { this.finish() }
        confirmed.text = getString(R.string.today_confirmed_text, today.confirmed)
        deaths.text = getString(R.string.today_deaths_text, today.deaths)
        recovered.text = getString(R.string.today_recovered_text, today.recovered)
        chart.setChart(CountryChart.createChart(country))
        APIlib.getInstance().setActiveAnyChartView(chart);
    }
}