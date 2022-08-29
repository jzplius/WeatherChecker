package com.weather.checker.utils

import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.weather.checker.R
import com.weather.checker.data.remote.network.ApiClient
import com.weather.checker.ui.main.cart.ShoppingCartViewModel
import com.weather.checker.ui.main.cart.ShoppingCartViewModel.CardAttribute.*
import com.weather.checker.ui.main.cart.ShoppingCartViewModel.ColoringType.*


object BindingAdaptersUtils {
    @Suppress("UNCHECKED_CAST")
    @JvmStatic
    @BindingAdapter("android:imageScr")
    fun setMoviePosterSrc(imageView: ImageView, path: String?) {
        val url =
            ApiClient.BASE_IMAGE_URL_PREFIX + path + ApiClient.BASE_IMAGE_URL_POSTFIX
        Picasso.get()
            .load(url)
            .fit()
            .into(imageView, object : Callback {
                override fun onSuccess() {
                    Log.d("setMoviePosterSrc()", "onSuccess()")
                }

                override fun onError(e: Exception?) {
                    Log.d("setMoviePosterSrc()", "onError")
                    imageView.setBackgroundResource(R.drawable.ic_default_weather)
                }
            })
    }

    /* NOTE: for some reason unable to declare "coloringType" variable (2nd enum
        class in single <data> block in layout) - using workaround */
    /**
     * Match ShoppingCartViewModel.CardAttribute with hoppingCartViewModel.ColoringType to get
     * combinations that require specific CardView tinting. If match is found, then specific color
     * is returned. Is no specific match is found, then regular color is returned.
     */
    @Suppress("UNCHECKED_CAST")
    @JvmStatic
    @BindingAdapter(value = ["app:pickTextColor", "android:tag"], requireAll = true)
    fun getAssociatedColor(
        view: TextView,
        attribute: ShoppingCartViewModel.CardAttribute,
        coloringType: String /*should be ShoppingCartViewModel.ColoringType - workaround here*/
    ): Unit {
        /*should be ShoppingCartViewModel.ColoringType - workaround here*/
        //Manually map to enum values
        val type: ShoppingCartViewModel.ColoringType
        type = when (coloringType) {
            "TYPE1" -> {
                TYPE1
            }
            "TYPE2" -> {
                TYPE2
            }
            "TYPE3" -> {
                TYPE3
            }
            else -> {
                TYPE_DEFAULT
            }
        }
        /*should be ShoppingCartViewModel.ColoringType - workaround here*/

        val colorToReturn = when (setOf(type, attribute)) {
            setOf(
                TYPE1,
                LOCATION
            ), setOf(
                TYPE2,
                TEMPERATURE
            ), setOf(
                TYPE3,
                DATE
            )
            -> ContextCompat.getColor(view.context, R.color.color_yellow_full)
            setOf(
                TYPE1,
                DATE
            ), setOf(
                TYPE2,
                LOCATION
            ), setOf(
                TYPE3,
                TEMPERATURE
            )
            -> ContextCompat.getColor(view.context, R.color.color_red_full)
            setOf(
                TYPE1,
                TEMPERATURE
            ), setOf(
                TYPE2,
                DATE
            ), setOf(
                TYPE3,
                LOCATION
            )
            -> ContextCompat.getColor(view.context, R.color.color_green_full)
            else // ColoringType.TYPE_DEFAULT
            -> ContextCompat.getColor(view.context, R.color.white)
        }
        view.setTextColor(colorToReturn)
    }
}