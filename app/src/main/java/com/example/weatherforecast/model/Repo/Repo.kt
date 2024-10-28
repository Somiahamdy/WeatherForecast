package com.example.weatherforecast.model.Repo

import com.example.weatherforecast.db.WeatherLocalDataSource
import com.example.weatherforecast.model.Forecast
import com.example.weatherforecast.model.WeatherResponse
import com.example.weatherforecast.network.WeatherRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEmpty
import retrofit2.Response

class Repo(private val remoteDataSource: WeatherRemoteDataSource ,private val localDataSource: WeatherLocalDataSource) : RepoInterface {

    companion object{
        @Volatile
        private var INSTANCE: Repo? = null

        fun getInstance(remoteDataSource: WeatherRemoteDataSource,localDataSource: WeatherLocalDataSource): Repo? {
            return INSTANCE ?: synchronized(this) {
                INSTANCE = Repo(remoteDataSource,localDataSource)
                INSTANCE
            }
        }
    }

//    override suspend fun getWeatherForecast(long: Double, lat: Double, lang: String,appid:String) : Flow<Response<ForecastModel>> {
//        return remoteDataSource.getWeatherForecast(long,lat,lang,appid)
//    }

    override suspend fun getWeather(
        long: Double,
        lat: Double,
        appid: String,
        units:String
    ): Flow<WeatherResponse> {
        return remoteDataSource.getWeather(long,lat,appid,units)
    }

    override suspend fun get3HoursForecast(
        long: Double,
        lat: Double,
        appid: String,
        units: String
    ): Flow<List<Forecast>> {
        return remoteDataSource.getForecast(long,lat,appid,units).map { forecastResponse ->
            // Extract and return the list of 3-hour forecast
            forecastResponse.list
        }
    }

    override suspend fun getDayForecast(
        long: Double,
        lat: Double,
        appid: String,
        units: String
    ): Flow<List<Forecast>> {
        return remoteDataSource.getForecast(long,lat,appid,units).map { forecastResponse ->
            // Group by day and calculate daily temperatures
            forecastResponse.list.groupBy { it.dt / 86400 } // Group by day (86400 seconds = 1 day)
                .map { entry -> entry.value.first() } // For simplicity, taking the first forecast of each day
        }
    }
}