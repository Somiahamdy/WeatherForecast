package com.example.weatherforecast.db

import com.example.weatherforecast.network.ApiService
import com.example.weatherforecast.network.WeatherRemoteDataSourceImp

class WeatherLocalDataSourceImp: WeatherLocalDataSource {

    companion object {
        @Volatile
        private var INSTANCE: WeatherLocalDataSourceImp? = null
        fun getInstance(): WeatherLocalDataSourceImp {
            return INSTANCE ?: synchronized(this) {
                val instance = WeatherLocalDataSourceImp()
                INSTANCE = instance
                instance
            }
        }
    }

}