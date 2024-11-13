package com.example.weatherforecast.home.viewmodel

import org.junit.jupiter.api.Assertions.*

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.weatherforecast.db.FakeWeatherLocalDataSourceImp
import com.example.weatherforecast.model.Clouds
import com.example.weatherforecast.model.Coord
import com.example.weatherforecast.model.Forecast
import com.example.weatherforecast.model.Main
import com.example.weatherforecast.model.Rain
import com.example.weatherforecast.model.Repo.FakeRepo
import com.example.weatherforecast.model.Repo.Repo
import com.example.weatherforecast.model.Sys
import com.example.weatherforecast.model.WeatherResponse
import com.example.weatherforecast.model.Wind
import com.example.weatherforecast.network.FakeWeatherRemoteDataSourceImp
import com.google.android.gms.awareness.state.Weather
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: HomeViewModel
    private lateinit var repo: FakeRepo

    @Before
    fun setUp() {
        val remoteDataSource = FakeWeatherRemoteDataSourceImp()
        val localDataSource = FakeWeatherLocalDataSourceImp()
        repo = FakeRepo(remoteDataSource, localDataSource)!!
        viewModel = HomeViewModel(repo)
    }





    @Test
    fun getWeather_updatesWeatherList() = runTest {

        val lat = 12.0
        val lon = 12.0
        val units = "metric"
        val lang = "en"
        val api = "0f121088b1919d00bf3ffec84d4357f9"

        val fakeForecast = WeatherResponse(
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
//                Weather(
//                    id = 800,
//                    main = "Clear",
//                    description = "clear sky",
//                    icon = "01n"
//                )
            ),
            clouds = Clouds(all = 0),
            wind = Wind(speed = 2.6, deg = 220, gust = 4.1),
            visibility = 10000,
            rain = null,
            sys = Sys(
                type = 1,
                id = 200,
                country = "US",
                sunrise = 1678864200,
                sunset = 1678912600
            ),
            id = 12345,
            name = "Cairo",
            cod = 200,
            base = "stations",
            coord = Coord(lon = 12.0, lat = 12.0),
            timezone = -18000
        )
        repo.addWeatherResponse(fakeForecast)

        viewModel.getWeather(lat, lon, api, units, lang)

        advanceUntilIdle()

        assertNotNull(viewModel.weatherList.value)
        assertEquals("Cairo", viewModel.weatherList.value?.name)
    }

    @Test
    fun getDailyForecast_updatesDailyForecastList() = runTest() {
        // Arrange
        val lat = 12.0
        val lon = 12.0
        val units = "metric"
        val lang = "en"
        val api = "0f121088b1919d00bf3ffec84d4357f9"

        // Create a fake repository
        //val fakeRepo = FakeRepo(FakeWeatherRemoteDataSourceImp(), FakeWeatherLocalDataSourceImp())
        //val viewModel = HomeViewModel(fakeRepo)

        // Add a fake forecast to the repository
        val fakeForecast = Forecast(
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
//                Weather(
//                    id = 800,
//                    main = "Clear",
//                    description = "clear sky",
//                    icon = "01n"
//                )
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
        )
        repo.addForecast(fakeForecast)

        viewModel.getDailyForecast(lat, lon, api, units, lang)

        val dispatcher= StandardTestDispatcher()
        dispatcher.scheduler. advanceUntilIdle( )

        assertNotNull(viewModel.dailyForecastList.value)
        assertEquals(1, viewModel.dailyForecastList.value?.size)
        assertEquals(fakeForecast, viewModel.dailyForecastList.value?.firstOrNull())
    }
}