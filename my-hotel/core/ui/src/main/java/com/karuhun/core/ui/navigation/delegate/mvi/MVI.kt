package com.karuhun.core.ui.navigation.delegate.mvi

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface MVI<UiState, UIAction, UiEffect> {
    val uiState: StateFlow<UiState>
    val currentUiState: UiState
    val uiEffect: Flow<UiEffect>
    fun onAction(action: UIAction)
    fun updateUiState(block: UiState.() -> UiState)
    suspend fun emitUiEffect(uiEffect: UiEffect)
}

fun <UiState, UIAction, UiEffect> mvi(
    initialState: UiState,
) : MVI<UiState, UIAction, UiEffect> = MVIDelegate(initialState)