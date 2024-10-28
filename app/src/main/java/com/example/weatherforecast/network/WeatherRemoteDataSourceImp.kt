package com.example.weatherforecast.network

//import com.example.weatherforecast.model.ForecastModel
import com.example.weatherforecast.model.ForecastResponse
import com.example.weatherforecast.model.WeatherResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class WeatherRemoteDataSourceImp(var apiService: ApiService) : WeatherRemoteDataSource {

    companion object {
        @Volatile
        private var INSTANCE: WeatherRemoteDataSourceImp? = null
        fun getInstance(apiService: ApiService): WeatherRemoteDataSourceImp {
            return INSTANCE ?: synchronized(this) {
                val instance = WeatherRemoteDataSourceImp(apiService)
                INSTANCE = instance
                instance
            }
        }
    }


//    override suspend fun getWeatherForecast(
//        lon: Double,
//        lat: Double,
//        lang: String,
//        appid:String
//    ): Flow<Response<ForecastModel>> =
//        flow { emit(apiService.getForecast(lat, lon, lang,appid)) }

    override suspend fun getWeather(lon: Double, lat: Double, apid: String,units:String,lang:String): Flow<WeatherResponse> {
       return flow {
            emit(apiService.getWeather(lat,lon,apid,units,lang))
        }
    }

    override suspend fun getForecast(
        lon: Double,
        lat: Double,
        apid: String,
        units: String,
        lang:String
    ): Flow<ForecastResponse> {
        return flow{
            emit(apiService.getForecast(lat,lon,apid,units,lang))
        }
    }


}