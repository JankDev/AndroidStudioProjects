package pl.agh.coronatracker.domain

import pl.agh.coronatracker.R
import com.blongho.country_data.World


object CountryFlagRetriever {
    fun getFlagForCode(countryCode: String) =
        when (countryCode) {
            "EUR" -> R.drawable.eu
            "HKD" -> R.drawable.hk
            "IDR" -> World.getFlagOf(World.getCountryFrom("MCO").id)
            "INR" -> World.getFlagOf(World.getCountryFrom("IN").id)
            "BYN" -> World.getFlagOf(World.getCountryFrom("BLR").id)
            "ZWL" -> World.getFlagOf(World.getCountryFrom("ZWE").id)
            "STN" -> World.getFlagOf(World.getCountryFrom("ST").id)
            "XPF" -> World.getFlagOf(World.getCountryFrom("CH").id)
            "GIP" -> World.getFlagOf(World.getCountryFrom("GI").id)
            "GHS" -> World.getFlagOf(World.getCountryFrom("GH").id)
            else -> World.getFlagOf(countryCode.take(2))
        }
}