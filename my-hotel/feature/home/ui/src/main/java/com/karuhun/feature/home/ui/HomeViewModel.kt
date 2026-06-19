package com.karuhun.feature.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.karuhun.core.common.onFailure
import com.karuhun.core.common.onSuccess
import com.karuhun.core.domain.usecase.GetHotelProfileUseCase
import com.karuhun.core.domain.usecase.GetRoomDetailUseCase
import com.karuhun.core.ui.navigation.delegate.mvi.MVI
import com.karuhun.core.ui.navigation.delegate.mvi.mvi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    private val getHotelProfileUseCase: GetHotelProfileUseCase,
    private val getRoomDetailUseCase: GetRoomDetailUseCase,
) : ViewModel(),
    MVI<HomeContract.UiState, HomeContract.UiAction, HomeContract.UiEffect> by mvi(initialState = HomeContract.UiState()) {

    init {
        onAction(HomeContract.UiAction.LoadMenuItems)
        onAction(HomeContract.UiAction.LoadRoomDetail)
    }
    override fun onAction(action: HomeContract.UiAction) {
        when (action) {
            HomeContract.UiAction.LoadMenuItems -> {
                loadMenuItems()
            }

            HomeContract.UiAction.OnMenuItemClick -> {}
            HomeContract.UiAction.OnMoreClick -> {}
            HomeContract.UiAction.LoadRoomDetail -> { loadRoomDetail() }
        }
    }

    private fun loadMenuItems() = viewModelScope.launch {
        updateUiState { copy(isLoading = true) }
        getHotelProfileUseCase().collect { hotelProfile ->
            updateUiState {
                copy(
                    isLoading = false,
                    hotelProfile = hotelProfile,
                )
            }
        }
    }

    private fun loadRoomDetail() = viewModelScope.launch {
        getRoomDetailUseCase().onSuccess { roomDetail ->

        }
            .onSuccess {
                updateUiState {
                    copy(
                        roomDetail = it
                    )
                }
            }
            .onFailure { error ->

            }
    }
}