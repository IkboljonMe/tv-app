package com.karuhun.feature.onboarding.presentation.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.karuhun.feature.onboarding.presentation.OnBoardingScreen
import com.karuhun.feature.onboarding.presentation.OnBoardingViewModel
import kotlinx.serialization.Serializable

@Serializable
data object OnBoarding

fun NavGraphBuilder.onboardingGraph(
    onNavigateToHome: () -> Unit
) {
    composable<OnBoarding> {
        val viewModel = hiltViewModel<OnBoardingViewModel>()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val uiEffect = viewModel.uiEffect
        val onAction = viewModel::onAction
        OnBoardingScreen(
            uiState = uiState,
            uiEffect = uiEffect,
            onAction = onAction,
            onNavigateToHome = onNavigateToHome
        )
    }
}