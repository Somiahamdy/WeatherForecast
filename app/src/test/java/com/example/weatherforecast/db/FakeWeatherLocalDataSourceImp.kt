package com.example.weatherforecast.db

import com.example.weatherforecast.model.AlarmRoom
import com.example.weatherforecast.model.WeatherRoom
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeWeatherLocalDataSourceImp  : WeatherLocalDataSource {

    private val weatherList = mutableListOf<WeatherRoom>()
    private val alarmList = mutableListOf<AlarmRoom>()

    override suspend fun insertWeather(weather: WeatherRoom) {
        weatherList.add(weather)
    }

    override suspend fun deleteWeather(weather: WeatherRoom) {
        weatherList.remove(weather)
    }

    override suspend fun getAllFavWeather(): Flow<List<WeatherRoom>> {
        return flow { emit(weatherList) }
    }

    override suspend fun insertAlarm(alarmRoom: AlarmRoom) {
        alarmList.add(alarmRoom)
    }

    override suspend fun deleteAlarm(alarmRoom: AlarmRoom) {
        alarmList.remove(alarmRoom)
    }

    override fun getAllAlarms(): Flow<List<AlarmRoom>> {
        return flow { emit(alarmList) }
    }
}