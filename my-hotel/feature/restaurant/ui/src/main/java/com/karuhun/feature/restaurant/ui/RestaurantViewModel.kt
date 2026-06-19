package com.karuhun.feature.restaurant.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.karuhun.core.domain.usecase.GetFoodCategories
import com.karuhun.core.domain.usecase.GetFoodsUseCase
import com.karuhun.core.ui.navigation.delegate.mvi.MVI
import com.karuhun.core.ui.navigation.delegate.mvi.mvi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class RestaurantViewModel @Inject constructor(
    private val getFoodCategories: GetFoodCategories,
    private val getFoodsUseCase: GetFoodsUseCase
) : ViewModel(),
    MVI<RestaurantContract.UiState, RestaurantContract.UiAction, RestaurantContract.UiEffect> by mvi(
        initialState = RestaurantContract.UiState(),
    ) {

    init {
        onAction(RestaurantContract.UiAction.LoadCategory)
    }

    override fun onAction(action: RestaurantContract.UiAction) {
        when (action) {
            is RestaurantContract.UiAction.LoadCategory -> {
                loadCategory()
            }

            is RestaurantContract.UiAction.LoadFood -> {
                loadFood(action.categoryId)
            }
        }
    }

    private fun loadCategory() = viewModelScope.launch {
        getFoodCategories().collect {
            updateUiState {
                copy(
                    foodCategories = it
                )
            }
        }
    }

    private fun loadFood(categoryId: Int) = viewModelScope.launch {
        getFoodsUseCase(categoryId).collect {
            updateUiState {
                copy(
                    foods = it
                )
            }
        }
    }
}