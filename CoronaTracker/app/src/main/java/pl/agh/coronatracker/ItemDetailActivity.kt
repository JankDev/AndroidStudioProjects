package pl.agh.coronatracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
//import com.anychart.APIlib
//import com.anychart.AnyChartView
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

//    internal lateinit var chart: AnyChartView


    override fun onCreate(savedInstanceState: Bundle?) = runBlocking {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)
        countryName = intent.getStringExtra("countryName")!!
        confirmed = findViewById(R.id.confirmedCases)
        deaths = findViewById(R.id.deathCases)
        recovered = findViewById(R.id.recoveredCases)
        supervisorScope {
            try {
                country = withContext(Dispatchers.IO) {
                    CoronaService.getCountrySummary(countryName)
                }
                showData()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
        showData()
    }


    private fun showData() {
        val today = country.today
        confirmed.text = getString(R.string.today_confirmed_text, today.confirmed)
        deaths.text = getString(R.string.today_deaths_text, today.deaths)
        recovered.text = getString(R.string.today_recovered_text, today.recovered)
//        APIlib.getInstance().setActiveAnyChartView(last7DaysChart);
//        chart.setChart(CurrencyChart.createChart(currencyRates, 7))
//        APIlib.getInstance().setActiveAnyChartView(last30DaysChart);
//        last30DaysChart.setChart(CurrencyChart.createChart(currencyRates, 30))
    }
}