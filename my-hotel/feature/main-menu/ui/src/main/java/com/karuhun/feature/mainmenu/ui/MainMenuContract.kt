package com.karuhun.feature.mainmenu.ui

import com.karuhun.core.model.Application
import com.karuhun.core.model.Content
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

object MainMenuContract {
    data class UiState(
        val isLoading: Boolean = false,
        val contents: List<Content> = emptyList(),
        val applications: List<Application> = emptyList()
    )

    sealed interface UiAction {
        data class OnApplicationClicked(val application: Application) : UiAction
        data object OnMenuItemClick : UiAction
        data object LoadContents : UiAction
        data object LoadApplications : UiAction
    }

    sealed interface UiEffect {
        data class ShowError(val message: String) : UiEffect
    }
}