package com.example.weatherforecast.db

import android.content.Context

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherforecast.model.WeatherRoom
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WeatherDaoTest {

    private lateinit var weatherDao: WeatherDao
    private lateinit var db: AppDatabase

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        weatherDao = db.weatherDao
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insertAndGetAllFavWeather() = runBlocking {
        val weatherRoom = WeatherRoom(12.0, 12.0, "cairo", 12.0, 20.0,"clear sky","")
        weatherDao.insertWeather(weatherRoom)

        val allWeather = weatherDao.getAllFavWeather().first()

        assertEquals(allWeather.size, 1)
        assertEquals(allWeather[0], weatherRoom)
    }

    @Test
    fun deleteWeather() = runBlocking {
        val weatherRoom = WeatherRoom(12.0, 12.0, "cairo", 12.0, 20.0,"clear sky","")
        weatherDao.insertWeather(weatherRoom)
        weatherDao.deleteWeather(weatherRoom)

        val allWeather = weatherDao.getAllFavWeather().first()

        assertTrue(allWeather.isEmpty())
    }
}