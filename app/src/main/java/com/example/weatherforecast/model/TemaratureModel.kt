package com.example.weatherforecast.model

data class TemaratureModel(
    val time: ArrayList<String> = arrayListOf(),
    val temperature2m:ArrayList<Double> = arrayListOf(),
    val precipitationProbability:ArrayList<Double> = arrayListOf(),
)
