package com.example.weatherforecast.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "temperature_table")
data class TempCache(
    @PrimaryKey(autoGenerate = false)val position:Int?,
    @ColumnInfo(name = "date")val date:String?,
    @ColumnInfo(name = "temp")val temp:Int?,
    @ColumnInfo(name = "update")val update:String?
    )
