package com.karuhun.feature.onboarding.presentation

import com.karuhun.core.datastore.HotelProfile
import com.karuhun.feature.onboarding.presentation.cache.VideoCacheManager
import com.karuhun.feature.onboarding.presentation.model.VideoConfig

object OnBoardingContract {
    data class UiState(
        val isLoading: Boolean = false,
        val hotelProfile: HotelProfile? = null,
        val videoConfig: VideoConfig? = null,
        val isVideoPlaying: Boolean = false,
        val errorMessage: String? = null,
        val videoCacheManager: VideoCacheManager? = null,
        val isCached: Boolean = false,
        val isBuffering: Boolean = false,
    )
    sealed interface UiAction {
        object LoadScreenSaver : UiAction
        object PlayVideo : UiAction
        object PauseVideo : UiAction
        data class OnVideoError(val message: String) : UiAction
        data class PreCacheVideo(val uri: String) : UiAction
        data class OnBufferingStateChanged(val isBuffering: Boolean) : UiAction
    }
    sealed interface UiEffect {
        data class ShowError(val message: String) : UiEffect
    }
}