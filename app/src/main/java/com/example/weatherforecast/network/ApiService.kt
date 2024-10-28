package com.example.weatherforecast.network

//import com.example.weatherforecast.model.ForecastModel
import com.example.weatherforecast.model.ForecastResponse
import com.example.weatherforecast.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
//    @GET("data/2.5/onecall")
//    suspend fun getForecast(
//        @Query("lat") lat: Double,
//        @Query("lon") lon: Double,
//        @Query("lang") lang:String,
//        @Query("appid") appid: String
//        ):Response<ForecastModel>

    @GET("data/2.5/weather")
    suspend fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appid: String,
        @Query("units") units: String,
        @Query("lang") lang:String
        ):WeatherResponse

    @GET("data/2.5/forecast")
    suspend fun getForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appid: String,
        @Query("units") units: String,
        @Query("lang") lang:String
    ):ForecastResponse
}

