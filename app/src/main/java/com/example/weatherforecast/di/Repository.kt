package com.example.weatherforecast.di

import com.example.weatherforecast.api.APIService
import com.example.weatherforecast.model.ErrorResponseAPI
import com.example.weatherforecast.model.ResponseError
import com.example.weatherforecast.model.ResponseWeather
import com.haroldadmin.cnradapter.NetworkResponse
import javax.inject.Inject

class Repository @Inject constructor(
    private val api:APIService
){
    suspend fun getWeather(
        lat:Double?,
        long:Double,
        hourly: String?,
        curWeather:String?,
        days:Int,
        timeZone:String?): NetworkResponse<ResponseWeather, ResponseError> {
        return api.getForecast(lat,long,hourly,curWeather,days,timeZone)
    }

}