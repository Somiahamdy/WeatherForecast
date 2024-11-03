package com.example.weatherforecast.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weatherforecast.model.AlarmRoom
import com.example.weatherforecast.model.WeatherResponse
import com.example.weatherforecast.model.WeatherRoom

@Database(entities = [WeatherRoom::class,AlarmRoom::class], version = 4, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract val weatherDao: WeatherDao
    abstract val alarmDao:AlarmDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "weather_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}