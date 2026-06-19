package com.karuhun.feature.restaurant.ui.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.karuhun.feature.restaurant.ui.RestaurantCategoryScreen
import com.karuhun.feature.restaurant.ui.RestaurantViewModel
import kotlinx.serialization.Serializable

@Serializable
data object RestaurantCategory

fun NavGraphBuilder.restaurantGraph(

) {
    composable<RestaurantCategory> {
        val viewModel = hiltViewModel<RestaurantViewModel>()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val uiEffect = viewModel.uiEffect
        val onAction = viewModel::onAction
        RestaurantCategoryScreen(
            uiState = uiState,
            uiEffect = uiEffect,
            onAction = onAction
        )
    }
}