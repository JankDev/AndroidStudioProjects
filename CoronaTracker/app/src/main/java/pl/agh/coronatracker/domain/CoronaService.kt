package pl.agh.coronatracker.domain

import pl.agh.coronatracker.config.coronaApi

class CoronaService {
    suspend fun getCoronaSummary() = coronaApi
        .getSummary()
        .takeIf { it.isSuccessful }
        ?.body()
        ?.let { it.toViewModel() }
        ?: throw RuntimeException("Error loading data")
}