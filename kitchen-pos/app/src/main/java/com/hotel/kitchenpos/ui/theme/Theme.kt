package com.hotel.kitchenpos.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val KitchenColorScheme = darkColorScheme(
    primary = Palette.Brand600,
    onPrimary = Palette.White,
    secondary = Palette.Brand500,
    background = Palette.Slate900,
    onBackground = Palette.White,
    surface = Palette.Slate900,
    onSurface = Palette.White,
    error = Palette.Rose600,
)

@Composable
fun KitchenPosTheme(
    @Suppress("UNUSED_PARAMETER") darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    // The board is intentionally dark-only, matching the web POS.
    MaterialTheme(
        colorScheme = KitchenColorScheme,
        typography = KitchenTypography,
        content = content,
    )
}
