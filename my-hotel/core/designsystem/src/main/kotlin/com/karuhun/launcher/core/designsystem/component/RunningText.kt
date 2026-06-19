package com.karuhun.launcher.core.designsystem.component

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Text

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun RunningText(
    modifier: Modifier = Modifier,
    text: String
) {
    Box(
        modifier = modifier
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 16.sp,
            textAlign = TextAlign.Start,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.ExtraLight,
            modifier = Modifier.basicMarquee(
                iterations = Int.MAX_VALUE, // Loop tak terbatas
                velocity = 50.dp // Kecepatan scroll
            )
        )
    }
}