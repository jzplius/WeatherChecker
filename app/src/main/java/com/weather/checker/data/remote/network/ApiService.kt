package com.weather.checker.data.remote.network


import com.weather.checker.data.model.api.WeatherResponse
import com.weather.checker.utils.AppConstants
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("weather")
    suspend fun getWeather(
        @Query("q") location: String = AppConstants.DEFAULT_LOCATION_VALUE,
        @Query("appid") appId: String = AppConstants.DEFAULT_APP_ID,
        @Query("units") units: String = AppConstants.DEFAULT_CALCULATION_SYSTEM
    ): WeatherResponse
}