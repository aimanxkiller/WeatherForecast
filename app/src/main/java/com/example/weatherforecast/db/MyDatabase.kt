package com.example.weatherforecast.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weatherforecast.model.TempCache

@Database(entities = [TempCache::class], version = 1)
abstract class MyDatabase: RoomDatabase() {

    abstract fun getDao():TempDao

    companion object{
        private var INSTANCE:MyDatabase? = null

        fun getMyDB(context: Context):MyDatabase{
            val tempInstance = INSTANCE

            if(tempInstance!=null){

                return tempInstance
            }else{
                synchronized(this){
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        MyDatabase::class.java,
                        "my_database")
                        .allowMainThreadQueries()
                        .build()

                    INSTANCE = instance

                    return instance
                }
            }
        }
    }

}