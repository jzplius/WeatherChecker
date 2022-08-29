package com.weather.checker.utils

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object CalendarUtils {
    val PATTERN_WEEK_DAY_NAME = "EEE"
    val PATTERN_MONTH_DAY = "dd"

    fun format(format: String, timestamp: Long): String {
//        val timestamp: Long = 1661602378


        //        val sdf = SimpleDateFormat("EEEE")
//        val dayString = sdf.format(Date())

        val locale = Locale("en", "US")
//        val weekdayNameFormat: DateFormat = SimpleDateFormat("uu-MM-dd-yyyy-HH:mm:ss.SSSZ-EEE", locale)
        val dateFormat: DateFormat = SimpleDateFormat(format, locale)
        val cal = Calendar.getInstance()
        cal.timeInMillis = timestamp * 1000
//        val dayInt = cal[Calendar.DAY_OF_WEEK]
//        weekdayNameFormat.format(cal)

        return dateFormat.format(cal.time)
    }
}