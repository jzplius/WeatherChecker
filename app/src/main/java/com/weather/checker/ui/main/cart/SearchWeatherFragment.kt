package com.weather.checker.ui.main.cart

import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.lifecycle.SavedStateHandle
import com.google.android.material.snackbar.Snackbar
import com.weather.checker.BR
import com.weather.checker.R
import com.weather.checker.databinding.FragmentSearchWeatherBinding
import com.weather.checker.ui.base.BaseFragment
import com.weather.checker.utils.NetworkUtils
import com.weather.checker.utils.ViewUtils.hideKeyboard
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class SearchWeatherFragment :
    BaseFragment<FragmentSearchWeatherBinding, ShoppingCartViewModel>() {

    private val shoppingCartViewModel: ShoppingCartViewModel by viewModel {
        parametersOf(
            SavedStateHandle(mapOf())
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
        setUpObservation()
    }

    private fun initUI() {
        // Initially view needs to be GONE, later on VISIBLE/INVISIBLE
        getViewDataBinding().cardViewWeatherCard.visibility = View.GONE

        // Input capitalization is needed just to look nicer and more consistent in UI
        var tIEDLcocation = getViewDataBinding().textInputEditTextLocation
        tIEDLcocation.filters = tIEDLcocation.filters + InputFilter.AllCaps()

        getViewDataBinding().textInputEditTextLocation.setOnEditorActionListener { v, actionId, event ->
            when(actionId){
                EditorInfo.IME_ACTION_DONE -> {
                    getViewModel().onSearchClicked()
                    true
                }
                else -> false
            }
        }
    }

    override val layoutId: Int
        get() = R.layout.fragment_search_weather
    override val bindingVariable: Int
        get() = BR.viewModel

    override fun getViewModel(): ShoppingCartViewModel {
        return shoppingCartViewModel
    }

    private fun setUpObservation() {
        getViewModel().viewStateReadOnly.observe(viewLifecycleOwner) { state ->
            when (state) {
                ShoppingCartViewModel.ViewState.Loading -> {

                }
                is ShoppingCartViewModel.ViewState.dataLoadFailed -> {
                    // Initially and after error view needs to be GONE
                    getViewDataBinding().cardViewWeatherCard.visibility = View.GONE

                    Snackbar.make(
                        getViewDataBinding().constraintLayoutContainer,
                        state.errorMessage ?: getText(R.string.error_couldnt_download),
                        Snackbar.LENGTH_SHORT
                    )
                        .setAnchorView(getViewDataBinding().guidelineAboveNavigationBar)
                        .setBackgroundTint(resources.getColor(R.color.white))
                        .setTextColor(resources.getColor(R.color.input_text_color))
                        .show()
                }
                is ShoppingCartViewModel.ViewState.dataLoaded -> {

                }
                ShoppingCartViewModel.ViewState.ValidationStarted -> {
                    this.hideKeyboard()
                    if (getViewModel().isFormValid(getViewDataBinding().textInputEditTextLocation.text.toString())) {
                        val input = getViewDataBinding().textInputEditTextLocation.text!!

                        // no need for else block if form invalid, as ViewModel, Observables
                        // and databinding will take care of the UI
                        if (NetworkUtils.isNetworkAvailable(requireContext())) {
                            getViewModel().fetchData(input.trim().toString())
                        } else {
                            Snackbar.make(
                                getViewDataBinding().constraintLayoutContainer,
                                R.string.error_no_network,
                                Snackbar.LENGTH_LONG
                            )
                                .setAnchorView(getViewDataBinding().guidelineAboveNavigationBar)
                                .setBackgroundTint(resources.getColor(R.color.white))
                                .setTextColor(resources.getColor(R.color.input_text_color))
                                .show()
                        }
                    } else {
                        Snackbar.make(
                            getViewDataBinding().constraintLayoutContainer,
                            R.string.error_city_name_empty,
                            Snackbar.LENGTH_LONG
                        )
                            .setAnchorView(getViewDataBinding().guidelineAboveNavigationBar)
                            .setBackgroundTint(resources.getColor(R.color.white))
                            .setTextColor(resources.getColor(R.color.input_text_color))
                            .show()
                    }
                }
            }
        }
    }
}