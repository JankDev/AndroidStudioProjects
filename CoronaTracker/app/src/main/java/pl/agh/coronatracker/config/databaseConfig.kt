package pl.agh.coronatracker.config

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import pl.agh.coronatracker.dao.CoronaDao
import pl.agh.coronatracker.entity.CoronaSummary
import pl.agh.coronatracker.entity.CountrySummary

const val DATABASE_NAME = "corona_db"

@Database(entities = [CoronaSummary::class, CountrySummary::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun coronaDao(): CoronaDao

    companion object {

        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .build()
        }
    }
}