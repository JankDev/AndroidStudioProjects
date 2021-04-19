package pl.agh.coronatracker

import android.content.Intent
import android.os.Bundle
import androidx.core.widget.NestedScrollView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import pl.agh.coronatracker.config.coronaApi

import pl.agh.coronatracker.dummy.DummyContent
import pl.agh.coronatracker.model.CoronaSummary
import pl.agh.coronatracker.model.CountrySummary
import pl.agh.coronatracker.model.CountryWithTotalSummaries
import pl.agh.coronatracker.util.CountryDataStore
import pl.agh.coronatracker.util.FlagPicker

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CountryAdapter
    val instance = this

    @ExperimentalSerializationApi
    override fun onCreate(savedInstanceState: Bundle?) = runBlocking {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FlagPicker.prepare(applicationContext)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(instance)
        adapter = CountryAdapter(emptyList(), instance)
        recyclerView.adapter = adapter

        supervisorScope {
            try {
                val countries = withContext(Dispatchers.IO) {
                    coronaApi.getCountries()
                        .also { print(it) }
                        .takeIf { it.isSuccessful }
                        ?.body()
                        ?: throw RuntimeException("Error loading data")
                }.take(30)

                countries.mapNotNull { country ->
                    val result = withContext(Dispatchers.IO) {
                        Thread.sleep(100)
                        try {
                            coronaApi.countrySummary(country.country)
                                .also { print(it) }
                                .takeIf { it.isSuccessful }
                                ?.body()
                        } catch (ex: Exception) {
                            null
                        }
                    }
                    if (result != null) CountryWithTotalSummaries.acceptCountryData(
                        country,
                        result
                    ) else null
                }.map { country ->
                    CountryDataStore.acceptNewCountry(country)
                    adapter.dataSet = CountryDataStore.currencyRates
                    adapter.notifyDataSetChanged()
                }

            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            Unit
        }
    }
}