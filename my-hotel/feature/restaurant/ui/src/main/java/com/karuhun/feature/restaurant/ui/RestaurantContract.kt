package com.karuhun.feature.restaurant.ui

import com.karuhun.core.model.Food
import com.karuhun.core.model.FoodCategory

internal object RestaurantContract {
    data class UiState(
        val foodCategories: List<FoodCategory>? = emptyList(),
        val foods: List<Food> = emptyList()
    )
    sealed interface UiAction {
        data object LoadCategory : UiAction
        data class LoadFood(val categoryId: Int) : UiAction
    }
    sealed interface UiEffect {
        data class ShowError(val message: String)
    }
}