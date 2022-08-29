package com.weather.checker.data.local.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.weather.checker.data.model.db.Weather
import com.weather.checker.utils.AppConstants

@Dao
interface WeatherDAO {
    @Insert
    fun insert(item: Weather)

    @Query("select * from ${AppConstants.TABLE_NAME_WEATHER}")
    fun loadAll(): LiveData<MutableList<Weather>>

    @Query("delete from ${AppConstants.TABLE_NAME_WEATHER} where primaryId = :id")
    fun delete(id: Int)

    @Query("select COUNT(*) from ${AppConstants.TABLE_NAME_WEATHER}")
    fun countSum(): Int
}