package com.example.weatherforecast.model.Repo

import com.example.weatherforecast.db.WeatherLocalDataSource
import com.example.weatherforecast.model.AlarmRoom
import com.example.weatherforecast.model.Forecast
import com.example.weatherforecast.model.WeatherResponse
import com.example.weatherforecast.model.WeatherRoom
import com.example.weatherforecast.network.WeatherRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class FakeRepo(
    private val remoteDataSource: WeatherRemoteDataSource,
    private val localDataSource: WeatherLocalDataSource
) : RepoInterface {

    private val weatherList = mutableListOf<WeatherResponse>()
    private val forecastList = mutableListOf<Forecast>()
    private val favWeatherList = mutableListOf<WeatherRoom>()
    private val alarmList = mutableListOf<AlarmRoom>()

    override suspend fun getWeather(
        long: Double,
        lat: Double,
        appid: String,
        units: String,
        lang: String
    ): Flow<WeatherResponse> {
        return flow { emit(weatherList.firstOrNull() ?: WeatherResponse(null, null, null, null, 0, null, null, null, 0, null, 0, 0, null, 0)) }
    }

    override suspend fun get3HoursForecast(
        long: Double,
        lat: Double,
        appid: String,
        units: String,
        lang: String
    ): Flow<List<Forecast>> {
        return flow { emit(forecastList) }
    }

    override suspend fun getDayForecast(
        long: Double,
        lat: Double,
        appid: String,
        units: String,
        lang: String
    ): Flow<List<Forecast>> {
        return flow { emit(forecastList) }
    }

    override suspend fun getAllFavWeather(): Flow<List<WeatherRoom>> {
        return flow { emit(favWeatherList) }
    }

    override suspend fun insertWeatherToFav(weather: WeatherRoom) {
        favWeatherList.add(weather)
    }

    override suspend fun deleteWeatherFromFav(weather: WeatherRoom) {
        favWeatherList.remove(weather)
    }

    override suspend fun addAlarm(alarmRoom: AlarmRoom) {
        alarmList.add(alarmRoom)
    }

    override suspend fun removeAlarm(alarmRoom: AlarmRoom) {
        alarmList.remove(alarmRoom)
    }

    override fun getAllAlarms(): Flow<List<AlarmRoom>> {
        return flow { emit(alarmList) }
    }

    fun addWeatherResponse(weatherResponse: WeatherResponse) {
        weatherList.add(weatherResponse)
    }

    fun addForecast(forecast: Forecast) {
        forecastList.add(forecast)
    }
}