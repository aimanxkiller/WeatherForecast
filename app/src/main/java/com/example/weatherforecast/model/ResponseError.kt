package com.example.weatherforecast.model

import com.google.gson.annotations.SerializedName

data class ResponseError(

	@field:SerializedName("reason")
	val reason: String,

	@field:SerializedName("error")
	val error: Boolean
)
