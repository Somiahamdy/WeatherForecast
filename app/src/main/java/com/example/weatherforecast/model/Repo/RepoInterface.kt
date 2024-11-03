package com.example.weatherforecast.model.Repo

import com.example.weatherforecast.model.AlarmRoom
import com.example.weatherforecast.model.Forecast
import com.example.weatherforecast.model.ForecastResponse
import com.example.weatherforecast.model.Weather
import com.example.weatherforecast.model.WeatherResponse
import com.example.weatherforecast.model.WeatherRoom
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface RepoInterface {
    //suspend fun getWeatherForecast(long:Double , lat:Double, lang:String,appid:String): Flow<Response<ForecastModel>>

    suspend fun getWeather(long:Double , lat:Double,appid:String,units:String,lang:String):Flow<WeatherResponse>
    suspend fun get3HoursForecast(long:Double , lat:Double,appid:String,units:String,lang:String):Flow<List<Forecast>>
    suspend fun getDayForecast(long:Double , lat:Double,appid:String,units:String,lang:String):Flow<List<Forecast>>

    suspend fun getAllFavWeather():Flow<List<WeatherRoom>>
    suspend fun insertWeatherToFav(weather:WeatherRoom)
    suspend fun deleteWeatherFromFav(weather:WeatherRoom)

    suspend fun addAlarm(alarmRoom: AlarmRoom)
    suspend fun removeAlarm(alarmRoom: AlarmRoom)
    fun getAllAlarms(): Flow<List<AlarmRoom>>

}