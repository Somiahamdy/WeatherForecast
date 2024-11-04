package com.example.weatherforecast.network

import com.example.weatherforecast.model.City
import com.example.weatherforecast.model.Clouds
import com.example.weatherforecast.model.Coord
import com.example.weatherforecast.model.Forecast
import com.example.weatherforecast.model.ForecastResponse
import com.example.weatherforecast.model.Main
import com.example.weatherforecast.model.Sys
import com.example.weatherforecast.model.Weather
import com.example.weatherforecast.model.WeatherResponse
import com.example.weatherforecast.model.Wind
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeWeatherRemoteDataSourceImp  : WeatherRemoteDataSource {

    override suspend fun getWeather(
        lon: Double,
        lat: Double,
        apid: String,
        units: String,
        lang: String
    ): Flow<WeatherResponse> {
        return flow {
            emit(
                WeatherResponse(
                    coord = Coord(lon, lat),
                    weather = arrayListOf(
                        Weather(
                            id = 800,
                            main = "Clear",
                            description = "clear sky",
                            icon = "01d"
                        )
                    ),
                    base = "stations",
                    main = Main(
                        temp = 25.0,
                        feels_like = 26.5,
                        temp_min = 23.0,
                        temp_max = 27.0,
                        pressure = 1012,
                        humidity = 60,
                        sea_level = 1012,
                        grnd_level = 1012
                    ),
                    visibility = 10000,
                    wind = Wind(speed = 3.6, deg = 180, gust = 5.1),
                    rain = null,
                    clouds = Clouds(all = 0),
                    dt = 1678886400,
                    sys = Sys(
                        type = 1,
                        id = 200,
                        country = "US",
                        sunrise = 1678864200,
                        sunset = 1678912600
                    ),
                    timezone = -18000,
                    id = 4164138,
                    name = "Miami",
                    cod = 200
                )
            )
        }
    }

    override suspend fun getForecast(
        lon: Double,
        lat: Double,
        apid: String,
        units: String,
        lang: String
    ): Flow<ForecastResponse> {
        return flow {
            emit(
                ForecastResponse(
                    list = listOf(
                        Forecast(
                            dt = 1678900400,
                            main = Main(
                                temp = 24.5,
                                feels_like = 25.8,
                                temp_min = 23.2,
                                temp_max = 25.5,
                                pressure = 1011,
                                humidity = 65,
                                sea_level = 1011,
                                grnd_level = 1011
                            ),
                            weather = arrayListOf(
                                Weather(
                                    id = 800,
                                    main = "Clear",
                                    description = "clear sky",
                                    icon = "01n"
                                )
                            ),
                            clouds = Clouds(all = 0),
                            wind = Wind(speed = 2.6, deg = 220, gust = 4.1),
                            visibility = 10000,
                            pop = 0.0,
                            rain = null,
                            sys = Sys(
                                type = 1,
                                id = 200,
                                country = "US",
                                sunrise = 1678864200,
                                sunset = 1678912600
                            ),
                            dt_txt = "2023-03-15 18:00:00"
                        ),
                        // Add more forecast data objects here...
                    ),
                    city = City(
                        name = "Miami",
                        country = "US"
                    )
                )
            )
        }
    }
}