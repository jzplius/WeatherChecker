package com.weather.checker.data.remote

import com.weather.checker.data.model.Result
import com.weather.checker.data.model.api.WeatherResponse
import com.weather.checker.data.remote.network.ApiService
import org.koin.core.KoinComponent
import org.koin.core.inject

class ApiRepository : ApiDataSource, KoinComponent {

    private val apiService: ApiService by inject()

    override suspend fun fetchResponse(location: String): Result<WeatherResponse> {
        return try {
            val response = apiService.getWeather(location)
            Result.Success(response)
        } catch (exception: Exception) {
            Result.Error(exception.localizedMessage)
        }
    }
}