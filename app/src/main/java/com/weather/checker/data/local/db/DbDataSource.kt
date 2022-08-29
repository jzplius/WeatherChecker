package com.weather.checker.data.local.db

import androidx.lifecycle.LiveData
import com.weather.checker.data.model.db.Weather

interface DbDataSource {
    fun getWeathers(): LiveData<MutableList<Weather>>
    fun addWeather(item: Weather)
    fun deleteWeather(id: Int)
    fun getWeathersCount(): Int
}