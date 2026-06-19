package com.karuhun.launcher.model

import androidx.compose.ui.graphics.vector.ImageVector
import com.karuhun.launcher.core.designsystem.icon.TvRounded
import com.karuhun.launcher.core.designsystem.icon.WifiSvgrepoCom

data class MenuItem(
    val title: String,
    val icon: ImageVector,
){
    companion object {
        val items = listOf(
            MenuItem("TV", TvRounded),
            MenuItem("WIFI", WifiSvgrepoCom),
            MenuItem("YOUTUBE", TvRounded),
        )
    }
}
