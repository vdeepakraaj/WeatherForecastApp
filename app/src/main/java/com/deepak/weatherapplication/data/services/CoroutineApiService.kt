package com.deepak.weatherapplication.data.services

import com.deepak.weatherapplication.data.model.WeatherForecastResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CoroutineApiService {
    @GET("forecast?")
    suspend fun getWeatherForecast(
        @Query("lat") lat: String?, @Query("lon") lon: String?, @Query
            ("appid")
        key: String?
    ): Response<WeatherForecastResponse>
}