package com.example.weatherforecast.db

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Database
import androidx.room.DatabaseConfiguration
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.InvalidationTracker
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.example.weatherforecast.model.Weather
import com.example.weatherforecast.model.WeatherResponse
import com.example.weatherforecast.model.WeatherRoom
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Query("SELECT * FROM weather_table")
    fun getAllFavWeather(): Flow<List<WeatherRoom>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weatherRoom: WeatherRoom)

    @Delete
    suspend fun deleteWeather(weather:WeatherRoom)
}