package com.karuhun.launcher.core.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Border
import androidx.tv.material3.Card
import androidx.tv.material3.CardBorder
import androidx.tv.material3.CardColors
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.MaterialTheme

@Composable
fun LauncherCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    isSelected: Boolean = false,
    radius: Dp = 12.dp,
    color: CardColors = CardDefaults.colors(
        containerColor = Color.Black.copy(alpha = 0.60f),
        focusedContentColor = Color.White,
        contentColor = Color.White,
        pressedContentColor = Color.White
    ),
    borderColor: Color = MaterialTheme.colorScheme.primary,
    borderWidth: Dp = 1.dp,
    content: @Composable () -> Unit,
) {
    val border = Border(
        border = BorderStroke(
            width = borderWidth,
            color = borderColor
        )
    )

    Card(
        onClick = onClick,
        modifier = modifier,
        border = CardDefaults.border(
            focusedBorder = border,
            border = if (isSelected) border else Border.None
        ),
        scale = CardDefaults.scale(
            focusedScale = 1.05f
        ),
        colors = color,
        shape = CardDefaults.shape(RoundedCornerShape(radius))
    ) {
        content()
    }
}