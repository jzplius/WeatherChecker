package com.weather.checker.data.remote

import com.weather.checker.data.model.Result
import com.weather.checker.data.model.api.WeatherResponse

interface ApiDataSource {
    suspend fun fetchResponse(location: String): Result<WeatherResponse>
}