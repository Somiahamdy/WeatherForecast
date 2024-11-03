package com.example.weatherforecast.favourites.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.model.Repo.Repo
import com.example.weatherforecast.model.Weather
import com.example.weatherforecast.model.WeatherResponse
import com.example.weatherforecast.model.WeatherRoom
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavViewModel (var repo: Repo): ViewModel() {
    private val _favList = MutableStateFlow<List<WeatherRoom>>(emptyList())
    val favList: StateFlow<List<WeatherRoom>> = _favList.asStateFlow()

    val _weatherList = MutableStateFlow<WeatherResponse?>(null)
    val weatherList: StateFlow<WeatherResponse?> = _weatherList

    fun getFavWeather(){
        viewModelScope.launch(Dispatchers.IO) {
            repo.getAllFavWeather().collect { weatherList ->
                _favList.value = weatherList
                Log.d("favvm", "favWeather: ${weatherList}")
            }
        }
    }

    fun insertWeatherToFav(weatherRoom: WeatherRoom){
        viewModelScope.launch(Dispatchers.IO) {
            repo.insertWeatherToFav(weatherRoom)
            getFavWeather()
        }
    }

    fun deleteWeatherFromFav(weatherRoom: WeatherRoom){
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteWeatherFromFav(weatherRoom)
        }
    }

    fun getWeather(long: Double, lat: Double, appid: String, units: String,lang:String) {
        viewModelScope.launch(Dispatchers.IO) {
            val weather = repo.getWeather(long, lat, appid, units,lang).collect { weatherResponse ->
                _weatherList.value = weatherResponse
                Log.d("favvm", "Weather: ${weatherResponse}")

            }

        }
    }
}