package com.karuhun.feature.mainmenu.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.karuhun.core.common.fold
import com.karuhun.core.domain.usecase.GetApplicationsUseCase
import com.karuhun.core.domain.usecase.GetContentsUseCase
import com.karuhun.core.domain.usecase.LaunchApplicationUseCase
import com.karuhun.core.ui.navigation.delegate.mvi.MVI
import com.karuhun.core.ui.navigation.delegate.mvi.mvi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainMenuViewModel @Inject constructor(
    private val getContentsUseCase: GetContentsUseCase,
    private val getApplicationsUseCase: GetApplicationsUseCase,
    private val launchApplicationUseCase: LaunchApplicationUseCase,
) : ViewModel(),
    MVI<MainMenuContract.UiState, MainMenuContract.UiAction, MainMenuContract.UiEffect> by mvi(
        initialState = MainMenuContract.UiState(),
    ) {

    init {
        onAction(MainMenuContract.UiAction.LoadContents)
        onAction(MainMenuContract.UiAction.LoadApplications)
    }

    override fun onAction(action: MainMenuContract.UiAction) {
        when (action) {
            is MainMenuContract.UiAction.LoadContents -> { loadContents() }
            is MainMenuContract.UiAction.OnMenuItemClick -> {}
            is MainMenuContract.UiAction.LoadApplications -> { loadApplications() }
            is MainMenuContract.UiAction.OnApplicationClicked -> {
                onApplicationClicked(action.application.packageName.orEmpty())
            }
        }
    }

    private fun onApplicationClicked(packageName: String) = viewModelScope.launch {
        launchApplicationUseCase(packageName).fold(
            onSuccess = {

            },
            onError = { error ->
                emitUiEffect(
                    MainMenuContract.UiEffect.ShowError(error.message ?: "Unknown Error")
                )
            }
        )
    }

    private fun loadContents() = viewModelScope.launch {
        updateUiState { copy(isLoading = true) }
        getContentsUseCase().collect { contents ->
            updateUiState { copy(isLoading = false, contents = contents) }
        }
    }

    private fun loadApplications() = viewModelScope.launch {
        getApplicationsUseCase().collect { applications ->
            updateUiState {
                copy(
                    isLoading = false,
                    applications = applications
                )
            }
        }
    }
}