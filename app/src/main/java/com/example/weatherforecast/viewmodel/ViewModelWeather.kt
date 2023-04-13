package com.example.weatherforecast.viewmodel

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

    fun getForecast(
        lat:Double,
        long:Double,
        hourly: String?,
        curWeather:String?,
        days:Int,
        timeZone:String?
    ){

        viewModelScope.launch {
            val request = repo.getForecast(lat,long,hourly,curWeather,days,timeZone)

            when(request){
                is NetworkResponse.Success -> {
                    responseBody.postValue(request.body)
                }
                is NetworkResponse.ServerError -> TODO()

                is NetworkResponse.NetworkError -> TODO()

                is NetworkResponse.UnknownError -> TODO()

            }

        }

    }

//    fun getWeather(
//        lat:Double,
//        long:Double,
//        hourly: String?,
//        curWeather:String?,
//        days:Int,
//        timeZone:String?
//    ){
//
//        viewModelScope.launch {
//            val request = repo.getForecast(lat,long,hourly,curWeather,days,timeZone)
//            when(request){
//
//            }
//
//
//        }
//
//    }

    fun getCurAvg(): String {
        val x = responseBody.value?.hourly?.precipitationProbability as List<Int>
        var sum = 0.0

        for (i in 0 until 24) {
            sum += x[i]
        }

        return (sum/24).roundToInt().toString()
    }

}