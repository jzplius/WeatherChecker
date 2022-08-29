package com.weather.checker.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.weather.checker.data.local.db.dao.WeatherDAO
import com.weather.checker.data.model.db.Weather

@Database(entities = [Weather::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract val weathersDAO: WeatherDAO
}