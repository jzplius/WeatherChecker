package com.weather.checker.ui.main.cart

import android.os.Handler
import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.weather.checker.data.DataManager
import com.weather.checker.data.model.api.WeatherResponse
import com.weather.checker.data.model.db.Weather
import com.weather.checker.ui.base.BaseViewModel
import com.weather.checker.ui.main.cart.ShoppingCartViewModel.ColoringType.*
import com.weather.checker.ui.main.cart.ShoppingCartViewModel.FormErrors.MISSING_NAME
import com.weather.checker.utils.CalendarUtils
import kotlinx.coroutines.launch
import java.util.*
import com.weather.checker.data.model.Result

class ShoppingCartViewModel(
    dataManager: DataManager, saveStateHandle: SavedStateHandle
) : BaseViewModel(dataManager, saveStateHandle) {

    private val _viewState = MutableLiveData<ViewState>()
    val viewStateReadOnly: LiveData<ViewState> = _viewState

    private val _weather = MutableLiveData(Weather())
    val weather: LiveData<Weather> = _weather

    val dataAvailable = ObservableBoolean(false)

    val formErrors = ObservableArrayList<FormErrors>()

    fun fetchData(location: String) {
        dataAvailable.set(false)
        setIsLoading(true)
        _viewState.value =
            ViewState.Loading

        // To test layout behaviour on temperature - uncomment 1 test case bellow
        // NOTE: uncomment only 1 test case at a time, by commenting out remaining ones
//        testWithTemperature(9)
//        testWithTemperature(15)
//        testWithTemperature(21)
        // To work on production - comment out testWithTemperature's and uncomment method below
        performWeatherApiRequest(location)
        // TODO uncomment above method in production
        // TODO comment out test cases above in production
    }

    private fun performWeatherApiRequest(location: String) {
        viewModelScope.launch() {
            val result =
                getDataManager().getApiRepository().fetchResponse(location)

            when (result) {
                is Result.Success<WeatherResponse> -> {
                    dataAvailable.set(true)
                    setIsLoading(false)

                    val weatherData = Weather(
                        result.data.name,
                        result.data.weather[0].description,
                        result.data.main.temp.toInt(),
                        result.data.weather[0].icon,
                        result.data.main.tempMin.toInt(),
                        result.data.main.tempMax.toInt(),
                        CalendarUtils.format(
                            CalendarUtils.PATTERN_WEEK_DAY_NAME, result.data.dt.toLong()
                        ).toUpperCase(),
                        CalendarUtils.format(
                            CalendarUtils.PATTERN_MONTH_DAY, result.data.dt.toLong()
                        ),
                        identifyColoringType(result.data.main.temp.toInt())
                    )

                    _weather.value = weatherData
                    _viewState.value =
                        ViewState.dataLoaded(weatherData)
                }
                is Error -> {
                    dataAvailable.set(false)
                    setIsLoading(false)
                    _viewState.value =
                        ViewState.dataLoadFailed(
                            result.message
                        )
                }
                else -> {
                    dataAvailable.set(false)
                    setIsLoading(false)
                    _viewState.value =
                        ViewState.dataLoadFailed(
                            null
                        )
                }
            }
        }
    }

    /**
     * function that is used to mock response with specific temperature. Provided
     *  "Response" creates view in layout, that can be observed for debugging purposes.
     *  "Response" is delayed to immitate data loading
     *  @param temperature - any air temperature
     */
    private fun testWithTemperature(temperature: Int) {
        Handler().postDelayed({
            dataAvailable.set(true)
            setIsLoading(false)

            val weatherData = Weather(
                "location",
                "air description",
                temperature,
                "-",
                null,
                null,
                CalendarUtils.format(
                    CalendarUtils.PATTERN_WEEK_DAY_NAME, Calendar.getInstance().timeInMillis / 1000
                ).toUpperCase(),
                CalendarUtils.format(
                    CalendarUtils.PATTERN_MONTH_DAY, Calendar.getInstance().timeInMillis / 1000
                ),
                identifyColoringType(temperature)
            )

            _weather.value = weatherData
            _viewState.value =
                ViewState.dataLoaded(weatherData)

        }, 2000)
    }

    fun onSearchClicked() {
        _viewState.value = ViewState.ValidationStarted
    }

    fun isFormValid(value: String): Boolean {
        formErrors.clear()
        if (value.isEmpty()) {
            formErrors.add(MISSING_NAME)
        }
        return formErrors.isEmpty()
    }

    /**
     * Based on weather temperature determine type of Colors needed to show
     */
    private fun identifyColoringType(temperature: Int): ColoringType =
        when {
            temperature < 10 -> {
                TYPE1
            }
            temperature in 11..19 -> {
                TYPE2
            }
            temperature > 20 -> {
                TYPE3
            }
            else -> {
                TYPE_DEFAULT
            }
        }

    /** Indicates types of colors needed to show. Used to filter results.
     * Used together with @see CardAttribute */
    enum class ColoringType {
        TYPE1, TYPE2, TYPE3, TYPE_DEFAULT
    }

    /** Indicates CardView layout items, that need to react to color tinting. Used
     * to filter results. Used together with @see ColoringType */
    enum class CardAttribute {
        LOCATION, TEMPERATURE, DATE
    }

    enum class FormErrors {
        MISSING_NAME
    }

    sealed class ViewState() {
        object ValidationStarted : ViewState()
        object Loading : ViewState()
        data class dataLoaded(val data: Any) : ViewState()
        data class dataLoadFailed(val errorMessage: String?) : ViewState()
    }
}




