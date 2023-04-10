package com.example.weatherforecast.api

import com.example.weatherforecast.model.ResponseWeather
import retrofit2.Response
import retrofit2.http.GET

interface APIService {

    companion object{
        //Expected url blow
        //https://api.open-meteo.com/v1/forecast?latitude=3.14&longitude=101.69&hourly=temperature_2m,precipitation_probability&current_weather=true&forecast_days=7&timezone=auto
        const val BASE_URL = "https://api.open-meteo.com/v1/forecast?"
    }

    @GET()
    suspend fun getForecast(): Response<ResponseWeather>
}