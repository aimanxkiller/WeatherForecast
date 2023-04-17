package com.example.weatherforecast.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.di.Repository
import com.example.weatherforecast.model.ResponseWeather
import com.haroldadmin.cnradapter.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt


@HiltViewModel
class ViewModelWeather @Inject constructor(
    private val repo:Repository
): ViewModel() {

    var responseBody = MutableLiveData<ResponseWeather>()
    var error = MutableLiveData<String>()
    var loading = MutableLiveData<Boolean>()

    fun getForecast(
        lat:Double,
        long:Double,
        hourly: String?,
        curWeather:String?,
        days:Int,
        timeZone:String?
    ){

        viewModelScope.launch {

            //Add progressbar to load API
            loading.postValue(true)
            when(val request = repo.getWeather(lat,long,hourly,curWeather,days,timeZone)){
                is NetworkResponse.Success -> {
                    responseBody.postValue(request.body)
                }
                is NetworkResponse.ServerError -> {
                    error.postValue(request.body?.reason)
                }
                is NetworkResponse.NetworkError -> {
                    error.postValue("Network Error")
                }
                is NetworkResponse.UnknownError -> {
                    error.postValue("Unknown Error")
                }
            }
            loading.postValue(false)
            //Remove loading here
        }

    }

    fun getCurAvg(): String {
        val x = responseBody.value?.hourly?.precipitationProbability as List<Int>
        var sum = 0.0

        for (i in 0 until 24) {
            sum += x[i]
        }

        return (sum/24).roundToInt().toString()
    }

}