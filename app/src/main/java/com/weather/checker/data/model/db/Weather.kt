package com.weather.checker.data.model.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.weather.checker.ui.main.cart.ShoppingCartViewModel
import com.weather.checker.utils.AppConstants

@Entity(tableName = AppConstants.TABLE_NAME_WEATHER)
class Weather(
    val location: String = "",
    val description: String = "",
    val temperature: Int = 0,
    val iconPath: String = "",
    val temperatureMin: Int? = null,
    val temperatureMax: Int? = null,
    val weekdayName: String = "",
    val monthDay: String = "",
    val coloringType: ShoppingCartViewModel.ColoringType = ShoppingCartViewModel.ColoringType.TYPE_DEFAULT
) {
    @PrimaryKey(autoGenerate = true)
    var primaryId: Int = 0
}