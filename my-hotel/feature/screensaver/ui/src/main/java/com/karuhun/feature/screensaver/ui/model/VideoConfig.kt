package com.karuhun.feature.screensaver.ui.model

data class VideoConfig(
    val uri: String,
    val isAutoPlay: Boolean = true,
    val isMuted: Boolean = true,
    val isLooping: Boolean = true
)
