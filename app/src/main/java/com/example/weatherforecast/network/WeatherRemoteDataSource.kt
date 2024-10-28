package com.example.weatherforecast.network

import com.example.weatherforecast.model.ForecastResponse
import com.example.weatherforecast.model.WeatherResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface WeatherRemoteDataSource {
    //suspend fun getWeatherForecast(lon:Double,lat:Double,lang:String,apid:String): Flow<Response<ForecastModel>>

    suspend fun getWeather(lon:Double,lat:Double,apid:String,units:String):Flow<WeatherResponse>
    suspend fun getForecast(lon:Double,lat:Double,apid:String,units:String):Flow<ForecastResponse>
}