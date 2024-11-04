package com.example.weatherforecast.home.viewmodel

import org.junit.jupiter.api.Assertions.*

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.weatherforecast.db.FakeWeatherLocalDataSourceImp
import com.example.weatherforecast.model.Repo.FakeRepo
import com.example.weatherforecast.model.Repo.Repo
import com.example.weatherforecast.network.FakeWeatherRemoteDataSourceImp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
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



//    @Test
//    fun getWeather_updatesWeatherList() = runBlocking {
//        val lat = 12.0
//        val lon = 12.0
//        val units = "metric"
//        val lang = "en"
//        val api = "0f121088b1919d00bf3ffec84d4357f9"
//
//        viewModel.getWeather(lat, lon, api, units, lang)
//
//        assertNotNull(viewModel.weatherList.value)
//        assertEquals("ExpectedCityName", viewModel.weatherList.value?.name) // Replace with the expected city name
//    }

}