package com.example.weatherforecast.favourites.view

import com.example.weatherforecast.model.WeatherRoom

interface onFavClickListener {
    fun onRemoveClickListener(weatherRoom: WeatherRoom)
    fun onFavClickListener(weatherRoom: WeatherRoom)
}