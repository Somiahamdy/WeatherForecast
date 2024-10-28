package com.example.weatherforecast.home.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.model.Forecast
import com.example.weatherforecast.model.Repo.Repo
import com.example.weatherforecast.model.WeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class HomeViewModel(var repo:Repo): ViewModel() {

    val _weatherList = MutableStateFlow<WeatherResponse?>(null)
    val weatherList: StateFlow<WeatherResponse?> = _weatherList

    val _hourlyForecastList = MutableStateFlow<List<Forecast>?>(null)
    val hourlyForecastList: StateFlow<List<Forecast>?> = _hourlyForecastList

    val _dailyForecastList = MutableStateFlow<List<Forecast>?>(null)
    val dailyForecastList: StateFlow<List<Forecast>?> = _dailyForecastList

    fun getWeather(long: Double, lat: Double, appid: String, units: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val weather = repo.getWeather(long, lat, appid, units).collect { weatherResponse ->
                _weatherList.value = weatherResponse
                Log.d("homevm", "Weather: ${weatherResponse}")

            }

        }
    }

    fun get3HourForecast(long: Double, lat: Double, appid: String, units: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val hourlyForecast =
                repo.get3HoursForecast(long, lat, appid, units).collect { hourlyResponse ->
                    _hourlyForecastList.value = hourlyResponse
                    Log.d("homevm", "Weather: ${hourlyResponse}")

                }
        }
    }

    fun getDailyForecast(long: Double, lat: Double, appid: String, units: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val hourlyForecast =
                repo.getDayForecast(long, lat, appid, units).collect { dailyResponse ->
                    _dailyForecastList.value = dailyResponse
                    Log.d("homevm", "Weather: ${dailyResponse}")

                }
        }
    }
}