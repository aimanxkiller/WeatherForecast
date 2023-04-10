package com.example.weatherforecast.api

import com.example.weatherforecast.model.ResponseWeather
import retrofit2.Response
import retrofit2.http.GET

interface APIService {

    companion object{
        const val BASE_URL = "https://api.open-meteo.com/v1/forecast?"
    }

    @GET()
    suspend fun getForecast(): Response<ResponseWeather>
}