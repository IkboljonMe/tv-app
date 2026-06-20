package com.karuhun.feature.screensaver.ui

import com.karuhun.core.datastore.HotelProfile
import com.karuhun.core.model.Hotel
import com.karuhun.feature.screensaver.ui.cache.VideoCacheManager
import com.karuhun.feature.screensaver.ui.model.VideoConfig

object ScreenSaverContract {
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