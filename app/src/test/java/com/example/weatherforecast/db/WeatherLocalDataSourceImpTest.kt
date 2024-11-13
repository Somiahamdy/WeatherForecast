package com.example.weatherforecast.db

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import com.example.weatherforecast.model.AlarmRoom
import com.example.weatherforecast.model.WeatherRoom
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class WeatherLocalDataSourceImpTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: AppDatabase
    private lateinit var localDataSource: FakeWeatherLocalDataSourceImp

    @Before
    fun setUp() {
        // Create an in-memory database for testing
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = AppDatabase.getDatabase(context)
        localDataSource = FakeWeatherLocalDataSourceImp()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertWeather_and_getAllFavWeather() = runTest {
        // Arrange
        val weatherRoom = WeatherRoom(
            lon = 12.0,
            lat = 34.0,
            name = "Test City",
            temp_min = 15.0,
            temp_max = 25.0,
            description = "clear sky",
            icon = "01d"
        )

        // Act
        localDataSource.insertWeather(weatherRoom)
        val result = localDataSource.getAllFavWeather().first()

        // Assert
        assertEquals(1, result.size)
        assertEquals("Test City", result[0].name)
        assertEquals("clear sky", result[0].description)
    }

    @Test
    fun insertAlarm_and_deleteAlarm_and_getAllAlarms() = runTest {
        // Arrange
        val alarmRoom = AlarmRoom(
            formattedTime = "08:00",
            timeInMillis = System.currentTimeMillis()
        )

        // Act
        localDataSource.insertAlarm(alarmRoom)
        var alarms = localDataSource.getAllAlarms().first()

        // Assert initial insertion
        assertEquals(1, alarms.size)
        //assertEquals("08:00", alarms[0].time)

        // Act - delete the alarm
        localDataSource.deleteAlarm(alarmRoom)
        alarms = localDataSource.getAllAlarms().first()

        // Assert deletion
        assertTrue(alarms.isEmpty())
    }
}