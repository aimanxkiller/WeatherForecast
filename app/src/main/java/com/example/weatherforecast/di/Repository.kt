package com.example.weatherforecast.di

import com.example.weatherforecast.api.APIService
import com.example.weatherforecast.model.ErrorResponse
import com.example.weatherforecast.model.ResponseWeather
import com.haroldadmin.cnradapter.NetworkResponse
import retrofit2.Response
import javax.inject.Inject

class Repository @Inject constructor(
    private val api:APIService
){

    suspend fun getForecast(
        lat:Double,
        long:Double,
        hourly: String?,
        curWeather:String?,
        days:Int,
        timeZone:String?
    ): NetworkResponse<ResponseWeather,ErrorResponse> {
        return api.getForecast(lat,long,hourly,curWeather,days,timeZone)
    }

}