package com.example.weatherforecast.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherforecast.model.AlarmRoom
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addAlarm(alarmRoom: AlarmRoom)
    @Delete
    suspend fun deleteAlarm(alarmRoom: AlarmRoom)
    @Query("Select * from alarm_table")
    fun getAllAlarms(): Flow<List<AlarmRoom>>
}