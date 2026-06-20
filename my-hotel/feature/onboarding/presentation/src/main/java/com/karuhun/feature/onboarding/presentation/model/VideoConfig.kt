package com.karuhun.feature.onboarding.presentation.model

data class VideoConfig(
    val uri: String,
    val isAutoPlay: Boolean = true,
    val isMuted: Boolean = true,
    val isLooping: Boolean = true
)
