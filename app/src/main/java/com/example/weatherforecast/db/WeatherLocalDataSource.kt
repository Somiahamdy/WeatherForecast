package com.example.weatherforecast.db

import com.example.weatherforecast.model.AlarmRoom
import com.example.weatherforecast.model.WeatherResponse
import com.example.weatherforecast.model.WeatherRoom
import kotlinx.coroutines.flow.Flow

interface WeatherLocalDataSource {
    suspend fun insertWeather(weather:WeatherRoom)
    suspend fun deleteWeather(weather:WeatherRoom)
    suspend fun getAllFavWeather(): Flow<List<WeatherRoom>>

    suspend fun insertAlarm(alarmRoom: AlarmRoom)
    suspend fun deleteAlarm(alarmRoom: AlarmRoom)
    fun getAllAlarms(): Flow<List<AlarmRoom>>

}