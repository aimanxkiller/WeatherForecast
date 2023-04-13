package com.example.weatherforecast.api

import com.example.weatherforecast.model.ErrorResponseAPI
import com.example.weatherforecast.model.ResponseWeather
import com.haroldadmin.cnradapter.NetworkResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {

    companion object{
        //Expected url blow
        //https://api.open-meteo.com/v1/forecast?latitude=3.14&longitude=101.69&hourly=temperature_2m,precipitation_probability&current_weather=true&forecast_days=7&timezone=auto
        const val BASE_URL = "https://api.open-meteo.com/v1/"
    }

    @GET("forecast?")
    suspend fun getForecast(
        @Query("latitude")latitude:Double,
        @Query("longitude")longitude:Double,
        @Query("hourly")hourly: String? = "temperature_2m,precipitation_probability",
        @Query("current_weather")currentWeather: String? = "true",
        @Query("forecast_days")days:Int,
        @Query("timezone")timeZone:String? = "auto"
    ): NetworkResponse<ResponseWeather,ErrorResponseAPI>

}