package com.karuhun.feature.home.ui.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.RoomService
import androidx.compose.ui.graphics.vector.ImageVector
import com.karuhun.launcher.core.designsystem.icon.NetflixSvgrepoCom
import com.karuhun.launcher.core.designsystem.icon.WifiSvgrepoCom
import com.karuhun.launcher.core.designsystem.icon.YoutubeTvSvgrepoCom

data class MenuItem(
    val title: String,
    val icon: ImageVector,
) {
    companion object {
        /** Quick-access apps shown as the small tiles on the home dock. */
        val quickApps = listOf(
            MenuItem("Reseption", Icons.Filled.RoomService),
            MenuItem("Alarm", Icons.Filled.Alarm),
            MenuItem("YouTube", YoutubeTvSvgrepoCom),
            MenuItem("Netflix", NetflixSvgrepoCom),
            MenuItem("Wi-Fi", WifiSvgrepoCom),
        )
    }
}
