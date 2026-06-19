package com.karuhun.launcher.core.designsystem.component

import android.widget.TextClock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.tv.material3.Text
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun TextClock(
    modifier: Modifier = Modifier
) {
    var time by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        while (true){
            val currentTime = Calendar.getInstance().time
            time = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(currentTime)
            delay(1000L)
        }
    }

    Text(
        text = time,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFFEFEFEF),
    )
}