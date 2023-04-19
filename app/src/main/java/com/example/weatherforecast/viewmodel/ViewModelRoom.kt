package com.example.weatherforecast.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherforecast.db.RoomRepo
import com.example.weatherforecast.model.TempCache
import com.example.weatherforecast.model.TemperatureModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class ViewModelRoom @Inject constructor(
    private val repo:RoomRepo
):ViewModel() {

    val allData:MutableLiveData<List<TempCache>> = MutableLiveData()

    init{
        loadRepo()
    }

    private fun loadRepo(){
        val list = repo.getData()
        allData.postValue(list)
    }

    fun getTemp(){
        allData.postValue(repo.getData())
    }

    fun insertData(list:List<TemperatureModel>){
        list.forEachIndexed { index, temperatureModel ->
            val x = TempCache(
                index,
                temperatureModel.time,
                temperatureModel.temperature2m?.roundToInt()
            )
            repo.insertData(x)
        }
    }

}