package com.weather.checker.di.module

import androidx.lifecycle.SavedStateHandle
import com.weather.checker.data.DataManager
import com.weather.checker.ui.main.FullScreenViewModel
import com.weather.checker.ui.main.cart.ShoppingCartViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    single { provideDataManager() }

    viewModel { (handle: SavedStateHandle) -> FullScreenViewModel(get(), handle) }
    viewModel { (handle: SavedStateHandle) -> ShoppingCartViewModel(get(), handle) }
}

private fun provideDataManager() = DataManager()
