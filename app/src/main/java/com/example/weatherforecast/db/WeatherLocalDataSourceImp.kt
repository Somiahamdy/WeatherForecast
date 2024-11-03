package com.example.weatherforecast.db

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.weatherforecast.model.AlarmRoom
import com.example.weatherforecast.model.Weather
import com.example.weatherforecast.model.WeatherResponse
import com.example.weatherforecast.model.WeatherRoom
import com.example.weatherforecast.network.ApiService
import com.example.weatherforecast.network.WeatherRemoteDataSourceImp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WeatherLocalDataSourceImp(val appDatabase: AppDatabase): WeatherLocalDataSource {
    companion object {
        @Volatile
        private var INSTANCE: WeatherLocalDataSourceImp? = null

        fun getInstance(context: Context): WeatherLocalDataSourceImp {
            return INSTANCE ?: synchronized(this) {
                val database = AppDatabase.getDatabase(context)
                val instance = WeatherLocalDataSourceImp(database)
                INSTANCE = instance
                instance
            }
        }
    }

    override suspend fun insertWeather(weather:WeatherRoom) {
         appDatabase.weatherDao.insertWeather(weather)
    }

    override suspend fun deleteWeather(weather:WeatherRoom) {
        appDatabase.weatherDao.deleteWeather(weather)
    }

    override suspend fun getAllFavWeather(): Flow<List<WeatherRoom>> {
        return appDatabase.weatherDao.getAllFavWeather()
    }

    override suspend fun insertAlarm(alarmRoom: AlarmRoom) {
        appDatabase.alarmDao.addAlarm(alarmRoom)
    }

    override suspend fun deleteAlarm(alarmRoom: AlarmRoom) {
        appDatabase.alarmDao.deleteAlarm(alarmRoom)
    }

    override fun getAllAlarms(): Flow<List<AlarmRoom>> {
        return appDatabase.alarmDao.getAllAlarms()
    }


}