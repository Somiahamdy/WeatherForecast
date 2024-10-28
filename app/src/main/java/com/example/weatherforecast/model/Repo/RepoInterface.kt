package com.example.weatherforecast.model.Repo

import com.example.weatherforecast.model.Forecast
import com.example.weatherforecast.model.ForecastResponse
import com.example.weatherforecast.model.WeatherResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface RepoInterface {
    //suspend fun getWeatherForecast(long:Double , lat:Double, lang:String,appid:String): Flow<Response<ForecastModel>>

    suspend fun getWeather(long:Double , lat:Double,appid:String,units:String):Flow<WeatherResponse>
    suspend fun get3HoursForecast(long:Double , lat:Double,appid:String,units:String):Flow<List<Forecast>>
    suspend fun getDayForecast(long:Double , lat:Double,appid:String,units:String):Flow<List<Forecast>>

}