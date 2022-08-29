package com.weather.checker.data

import com.weather.checker.data.local.db.DatabaseRepository
import com.weather.checker.data.remote.ApiRepository

class DataManager {
    private val apiRepository = ApiRepository()
    private val databaseRepository = DatabaseRepository()

    fun getApiRepository(): ApiRepository {
        return apiRepository
    }

    fun getDatabaseRepository(): DatabaseRepository {
        return databaseRepository
    }
}