package com.example.weatherforecast.model

import com.google.gson.annotations.SerializedName

data class ResponseWeather(

	@field:SerializedName("elevation")
	val elevation: Any? = null,

	@field:SerializedName("hourly_units")
	val hourlyUnits: HourlyUnits? = null,

	@field:SerializedName("generationtime_ms")
	val generationTimeMs: Any? = null,

	@field:SerializedName("timezone_abbreviation")
	val timezoneAbbreviation: String? = null,

	@field:SerializedName("timezone")
	val timezone: String? = null,

	@field:SerializedName("latitude")
	val latitude: Any? = null,

	@field:SerializedName("utc_offset_seconds")
	val utcOffsetSeconds: Int? = null,

	@field:SerializedName("hourly")
	val hourly: Hourly? = null,

	@field:SerializedName("current_weather")
	val currentWeather: CurrentWeather? = null,

	@field:SerializedName("longitude")
	val longitude: Any? = null
)

data class Hourly(

	@field:SerializedName("temperature_2m")
	val temperature2m: List<Double?>? = null,

	@field:SerializedName("precipitation_probability")
	val precipitationProbability: List<Int?>? = null,

	@field:SerializedName("time")
	val time: List<String?>? = null
)

data class CurrentWeather(

	@field:SerializedName("weathercode")
	val weatherCode: Int? = null,

	@field:SerializedName("temperature")
	val temperature: Any? = null,

	@field:SerializedName("windspeed")
	val windSpeed: Any? = null,

	@field:SerializedName("is_day")
	val isDay: Int? = null,

	@field:SerializedName("time")
	val time: String? = null,

	@field:SerializedName("winddirection")
	val windDirection: Any? = null
)

data class HourlyUnits(

	@field:SerializedName("temperature_2m")
	val temperature2m: String? = null,

	@field:SerializedName("precipitation_probability")
	val precipitationProbability: String? = null,

	@field:SerializedName("time")
	val time: String? = null
)
