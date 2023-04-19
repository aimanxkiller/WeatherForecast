package com.example.weatherforecast.db

import androidx.room.Dao
import com.example.weatherforecast.model.TempCache
import javax.inject.Inject

class RoomRepo @Inject constructor(
    private val appDao:TempDao
) {

    fun getData():List<TempCache>{
        return appDao.getAll()
    }

    fun insertData(temp:TempCache){
        appDao.insert(temp)
    }

}