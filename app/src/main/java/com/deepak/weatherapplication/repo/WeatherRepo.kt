package com.deepak.weatherapplication.repo

import com.deepak.weatherapplication.data.model.WeatherForecastResponse
import com.deepak.weatherapplication.data.services.CoroutineApiService
import com.deepak.weatherapplication.AppConstants
import com.deepak.weatherapplication.NetworkResult

class WeatherRepo constructor(private val coroutineApiService: CoroutineApiService) {

    suspend fun getWeatherForecast(latitude: String?, longitude: String?):
            NetworkResult<WeatherForecastResponse> {
        val response =
            coroutineApiService.getWeatherForecast(latitude, longitude, AppConstants.API_KEY)
        if (response.isSuccessful) {
            val data = response.body()
            if (data != null) {
                return NetworkResult.Success(data)
            }
        }
        return NetworkResult.Failure(response)
    }
}