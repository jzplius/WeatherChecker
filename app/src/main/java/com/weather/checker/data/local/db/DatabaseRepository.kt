package com.weather.checker.data.local.db

import androidx.lifecycle.LiveData
import com.weather.checker.data.model.db.Weather
import org.koin.core.KoinComponent
import org.koin.core.inject

class DatabaseRepository : DbDataSource, KoinComponent {

    private val appDatabase: AppDatabase by inject()

    override fun getWeathers(): LiveData<MutableList<Weather>> {
        return appDatabase.weathersDAO.loadAll()
    }

    override fun addWeather(item: Weather) {
        appDatabase.weathersDAO.insert(item)
    }

    override fun deleteWeather(id: Int) {
        appDatabase.weathersDAO.delete(id)
    }

    override fun getWeathersCount(): Int {
        return appDatabase.weathersDAO.countSum()
    }
}