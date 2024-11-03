package com.example.weatherforecast.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_table", primaryKeys = ["lon", "lat"])
data class WeatherRoom(
    var lon:Double,
    var lat:Double,
    var name: String?,
    var temp_min: Double? ,
    var temp_max: Double?,
    var description: String?,
    var icon: String?,
)