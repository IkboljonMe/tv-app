package com.karuhun.core.ui.navigation.delegate.mvi

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlin.getValue

class MVIDelegate<UiState, UIAction, UiEffect>(
    initialState: UiState,
) : MVI<UiState, UIAction, UiEffect> {

    private val _uiState by lazy { MutableStateFlow(initialState) }
    override val uiState: StateFlow<UiState> by lazy { _uiState.asStateFlow() }

    override val currentUiState: UiState
        get() = uiState.value

    private val _uiEffect by lazy { Channel<UiEffect>() }
    override val uiEffect: Flow<UiEffect> by lazy { _uiEffect.receiveAsFlow() }

    override fun onAction(action: UIAction) = Unit

    override fun updateUiState(block: UiState.() -> UiState) =
        _uiState.update(block)

    override suspend fun emitUiEffect(uiEffect: UiEffect) =
        _uiEffect.send(uiEffect)
}