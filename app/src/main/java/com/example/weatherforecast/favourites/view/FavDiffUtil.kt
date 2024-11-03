package com.example.weatherforecast.favourites.view

import androidx.recyclerview.widget.DiffUtil
import com.example.weatherforecast.model.Forecast
import com.example.weatherforecast.model.WeatherRoom

class FavDiffUtil : DiffUtil.ItemCallback<WeatherRoom>(){

    override fun areItemsTheSame(oldItem: WeatherRoom, newItem: WeatherRoom): Boolean {
        return  oldItem.lat==newItem.lat}

    override fun areContentsTheSame(oldItem: WeatherRoom, newItem: WeatherRoom): Boolean {
        return oldItem==newItem
    }

}