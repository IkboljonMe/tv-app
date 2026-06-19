package com.hotel.kitchenpos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.hotel.kitchenpos.data.AppSession
import com.hotel.kitchenpos.ui.login.LoginScreen
import com.hotel.kitchenpos.ui.pos.PosBoard
import com.hotel.kitchenpos.ui.theme.KitchenPosTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KitchenPosTheme {
                var loggedIn by remember { mutableStateOf(AppSession.isLoggedIn) }
                if (loggedIn) {
                    PosBoard(onLoggedOut = { loggedIn = false })
                } else {
                    LoginScreen(onLoggedIn = { loggedIn = true })
                }
            }
        }
    }
}
