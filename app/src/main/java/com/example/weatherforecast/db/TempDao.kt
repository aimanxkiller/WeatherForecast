package com.example.weatherforecast.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherforecast.model.TempCache

@Dao
interface TempDao {

    @Query("SELECT * FROM temperature_table")
    fun getAll():List<TempCache>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(temp:TempCache)

}