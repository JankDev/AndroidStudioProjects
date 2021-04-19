package pl.agh.coronatracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.anychart.APIlib
import com.anychart.AnyChartView
import pl.agh.coronatracker.model.CountryWithTotalSummaries
import pl.agh.coronatracker.util.CountryDataStore

class SingleCountryActivity : AppCompatActivity() {

    internal lateinit var active: TextView
    internal lateinit var deaths: TextView
    internal lateinit var recovered: TextView

//    internal lateinit var chart: AnyChartView

    internal lateinit var country: CountryWithTotalSummaries


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.single_currency_activity)
        val countryName = intent.getStringExtra("countryName")!!
        country = CountryDataStore.getCountryObj(countryName)
        active = findViewById(R.id.activeCases)
        deaths = findViewById(R.id.deathCases)
        recovered = findViewById(R.id.recoveredCases)
        showData()
    }

    private fun showData() {
        val today = country.today
        active.text = getString(R.string.today_active_text, today.active)
        deaths.text = getString(R.string.today_deaths_text, today.deaths)
        recovered.text = getString(R.string.today_recovered_text, today.recovered)
//        APIlib.getInstance().setActiveAnyChartView(last7DaysChart);
//        chart.setChart(CurrencyChart.createChart(currencyRates, 7))
//        APIlib.getInstance().setActiveAnyChartView(last30DaysChart);
//        last30DaysChart.setChart(CurrencyChart.createChart(currencyRates, 30))
    }
}