package com.example.weatherforecast.model.Repo

import com.example.weatherforecast.db.FakeWeatherLocalDataSourceImp
import com.example.weatherforecast.network.FakeWeatherRemoteDataSourceImp
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach

class RepoTest {

    lateinit var remoteDataSource: FakeWeatherRemoteDataSourceImp
    lateinit var localDataSource: FakeWeatherLocalDataSourceImp
    lateinit var repo: Repo

    @Before
    fun setUp() {
        remoteDataSource = FakeWeatherRemoteDataSourceImp()
        localDataSource = FakeWeatherLocalDataSourceImp()
        repo = Repo.getInstance(remoteDataSource, localDataSource)!!
    }

    @Test
    fun getWeather_withMetricUnit_returnsCelsiusTemperature() = runTest() {

        val lat = 12.0
        val lon = 12.0
        val units = "metric"
        val lang = "en"
        val api="0f121088b1919d00bf3ffec84d4357f9"


        val weatherResponse = repo.getWeather(lat, lon, api, units, lang)

        assertNotNull(weatherResponse)

        assertTrue(weatherResponse.first().main!!.temp > -273.15) // Celsius should be above absolute zero
    }

    @Test
    fun get3HoursForecast_returnsListOfForecasts() = runTest {
        // Arrange
        val lat = 12.0
        val lon = 12.0
        val units = "metric"
        val lang = "en"
        val api = "0f121088b1919d00bf3ffec84d4357f9"


        val forecastListFlow = repo.get3HoursForecast(lat, lon, api, units, lang)
        val forecastList = forecastListFlow.first()


        assertNotNull(forecastList)
        assertTrue(forecastList.isNotEmpty())

    }
}


