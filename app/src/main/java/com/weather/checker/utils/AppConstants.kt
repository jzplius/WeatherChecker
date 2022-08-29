package com.weather.checker.utils

import com.weather.checker.BuildConfig

object AppConstants {
    const val PREF_NAME = BuildConfig.APPLICATION_ID + "_pref"

    const val DATABASE_NAME = "db_name"
    const val TABLE_NAME_WEATHER = "weather"

    const val DEFAULT_APP_ID = "7587eaff3affbf8e56a81da4d6c51d06"
    const val DEFAULT_LOCATION_VALUE = "Šiauliai,LTU"
    const val DEFAULT_CALCULATION_SYSTEM = "metric"
}