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
import kotlinx.serialization.ExperimentalSerializationApi
import pl.agh.coronatracker.domain.CoronaService
import pl.agh.coronatracker.view_model.CountryWithTotalSummaries

class ItemDetailActivity : AppCompatActivity() {
    private lateinit var coronaService: CoronaService

    private lateinit var countryName: String
    @ExperimentalSerializationApi
    internal lateinit var country: CountryWithTotalSummaries

    private lateinit var confirmed: TextView
    private lateinit var deaths: TextView
    private lateinit var recovered: TextView

    private lateinit var goBackButton: Button

    private lateinit var chart: AnyChartView

    val instance: ItemDetailActivity = this

    override fun onCreate(savedInstanceState: Bundle?) = runBlocking {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)
        coronaService = CoronaService(this@ItemDetailActivity)

        countryName = intent.getStringExtra("countryName")!!
        confirmed = findViewById(R.id.confirmedCases)
        deaths = findViewById(R.id.deathCases)
        recovered = findViewById(R.id.recoveredCases)
        chart = findViewById(R.id.chart)
        goBackButton = findViewById(R.id.goBackButton)
        supervisorScope {
            try {
                country = withContext(Dispatchers.IO) {
                    coronaService.getCountrySummary(countryName)
                }
                showData()
            } catch (ex: Exception) {
                instance.finish()
                ex.printStackTrace()
            }
        }
    }


    @ExperimentalSerializationApi
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