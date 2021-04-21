package pl.agh.coronatracker.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import pl.agh.coronatracker.entity.CoronaSummary
import pl.agh.coronatracker.entity.CoronaWithCountriesSummary
import pl.agh.coronatracker.entity.CountrySummary

@Dao
interface CoronaDao {
    @Transaction
    @Insert
    suspend fun insertCoronaSummary(coronaSummary: CoronaSummary): Long

    @Transaction
    @Insert
    suspend fun insertCountrySummaries(countrySummaries: List<CountrySummary>)

    @Transaction
    @Query("SELECT * FROM CoronaSummary")
    suspend fun getCoronaWithCountriesSummary(): List<CoronaWithCountriesSummary>

    @Query("DELETE FROM CoronaSummary")
    suspend fun deleteAllFromCorona()

    @Query("DELETE FROM CountrySummary")
    suspend fun deleteAllFromCountry()
}