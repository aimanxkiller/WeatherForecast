package com.example.weatherforecast.di

import com.example.weatherforecast.api.APIService
import javax.inject.Inject

class Repository @Inject constructor(
    private val api:APIService
){
    suspend fun getWeather(
        lat:Double,
        long:Double,
        hourly: String?,
        curWeather:String?,
        days:Int,
        timeZone:String?) = api.getForecast(lat,long,hourly,curWeather,days,timeZone)

}