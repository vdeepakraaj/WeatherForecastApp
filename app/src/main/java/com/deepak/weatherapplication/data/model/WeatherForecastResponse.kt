package com.deepak.weatherapplication.data.model

import com.google.gson.annotations.SerializedName

data class WeatherForecastResponse(
    @SerializedName("list") val list: List<WeatherData>
)