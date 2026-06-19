package com.karuhun.feature.home.ui

import com.karuhun.core.datastore.HotelProfile
import com.karuhun.core.model.Hotel
import com.karuhun.core.model.RoomDetail

internal object HomeContract {
    data class UiState(
        val isLoading: Boolean = false,
        val hotelProfile: HotelProfile? = HotelProfile.Empty,
        val roomDetail: RoomDetail? = RoomDetail.Empty
    )
    sealed interface UiAction {
        data object OnMenuItemClick : UiAction
        data object OnMoreClick : UiAction
        data object LoadMenuItems : UiAction
        data object LoadRoomDetail : UiAction
    }

    sealed interface UiEffect {
        data class ShowError(val message: String) : UiEffect
    }
}