package pl.agh.coronatracker.util

import pl.agh.coronatracker.model.CountryTotalSummary
import pl.agh.coronatracker.model.CountryWithTotalSummaries
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

object CountryDataStore {

    private val mutex: Lock = ReentrantLock()

    var currencyRates: List<CountryWithTotalSummaries> = emptyList()

    fun acceptNewCountry(country: CountryWithTotalSummaries) {
        mutex.lock()
        currencyRates = currencyRates + country
        mutex.unlock()
    }

    fun getCountryObj(countryName: String): CountryWithTotalSummaries =
        currencyRates.find { it.country.country == countryName }!!
}