package com.example.weatherforecast.model


data class Clouds (
    var all: Int
)

data class Coord (
    var lon: Double ,
    var lat: Double
)



data class Main (
    var temp: Double ,
    var feels_like: Double ,
    var temp_min: Double ,
    var temp_max: Double ,
    var pressure: Int ,
    var humidity: Int ,
    var sea_level: Int ,
    var grnd_level: Int
)

data class Rain (
    @JsonProperty("1h")
    var _1h: Double
)

annotation class JsonProperty(val value: String)

data class WeatherResponse (
    var coord: Coord? ,
    var weather: ArrayList<Weather>? ,
    var base: String? ,
    var main: Main? ,
    var visibility: Int ,
    var wind: Wind? ,
    var rain: Rain? ,
    var clouds: Clouds? ,
    var dt: Int ,
    var sys: Sys? ,
    var timezone: Int ,
    var id: Int ,
    var name: String? ,
    var cod: Int
    )

data class Sys (
    var type: Int ,
    var id: Int ,
    var country: String? ,
    var sunrise: Int ,
    var sunset: Int ,
    )

data class Weather (
    var id: Int ,
    var main: String? ,
    var description: String? ,
    var icon: String?
)

data class Wind (
    var speed: Double ,
    var deg: Int ,
    var gust: Double
)
data class ForecastResponse(
    val list: List<Forecast>,
    val city: City
)

data class Forecast (
    var dt: Int ,
    var main: Main? ,
    var weather: ArrayList<Weather>? ,
    var clouds: Clouds? ,
    var wind: Wind? ,
    var visibility: Int ,
    var pop: Double ,
    var rain: Rain? ,
    var sys: Sys? ,
    var dt_txt: String?
)


data class MainForecast(
    val temp: Double,
    val pressure: Int,
    val humidity: Int
)

data class City(
    val name: String,
    val country: String
)

