package com.hotel.kitchenpos.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hotel.kitchenpos.data.AppSession
import com.hotel.kitchenpos.data.KitchenApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUiState(
    val serverUrl: String = AppSession.baseUrl,
    val password: String = "",
    val submitting: Boolean = false,
    val error: String? = null,
)

class LoginViewModel : ViewModel() {
    private val _state = MutableStateFlow(LoginUiState())
    val state: StateFlow<LoginUiState> = _state.asStateFlow()

    fun onServerUrlChange(value: String) =
        _state.update { it.copy(serverUrl = value, error = null) }

    fun onPasswordChange(value: String) =
        _state.update { it.copy(password = value, error = null) }

    fun submit(onSuccess: () -> Unit) {
        val current = _state.value
        if (current.submitting) return
        if (current.password.isBlank()) {
            _state.update { it.copy(error = "Parolni kiriting") }
            return
        }
        _state.update { it.copy(submitting = true, error = null) }
        AppSession.baseUrl = current.serverUrl
        viewModelScope.launch {
            runCatching { KitchenApi.login(current.password) }
                .onSuccess {
                    _state.update { it.copy(submitting = false) }
                    onSuccess()
                }
                .onFailure { e ->
                    _state.update {
                        it.copy(
                            submitting = false,
                            error = e.message ?: "Kirishda xatolik",
                        )
                    }
                }
        }
    }
}
