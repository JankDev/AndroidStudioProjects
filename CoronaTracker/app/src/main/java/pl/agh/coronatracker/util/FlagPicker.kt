package pl.agh.coronatracker.util

import android.content.Context
import com.blongho.country_data.Country
import com.blongho.country_data.World
import pl.agh.coronatracker.R
import java.util.jar.Attributes

object FlagPicker {
    private lateinit var countries: List<Country>

    fun prepare(context: Context) {
        World.init(context)
        this.countries = World.getAllCountries().distinctBy { it.name }
    }

    fun getFlag(countryName: String): Int {
        return this.countries.find { it.name == countryName }?.flagResource
            ?: World.getWorldFlag()
    }
}