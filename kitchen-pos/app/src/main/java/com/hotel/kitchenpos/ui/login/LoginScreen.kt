package com.hotel.kitchenpos.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hotel.kitchenpos.ui.theme.Palette

@Composable
fun LoginScreen(
    onLoggedIn: () -> Unit,
    viewModel: LoginViewModel = viewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Palette.Slate900)
            .systemBarsPadding()
            .padding(24.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.width(380.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Palette.Brand600),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Filled.Restaurant,
                    contentDescription = null,
                    tint = Palette.White,
                    modifier = Modifier.size(30.dp),
                )
            }

            Spacer(Modifier.height(20.dp))
            Text(
                text = "Oshxona POS",
                color = Palette.White,
                style = androidx.compose.material3.MaterialTheme.typography.titleLarge,
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = "Buyurtmalarni ko‘rish uchun tizimga kiring.",
                color = Palette.Slate400,
                textAlign = TextAlign.Center,
                style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
            )

            Spacer(Modifier.height(28.dp))

            OutlinedTextField(
                value = state.serverUrl,
                onValueChange = viewModel::onServerUrlChange,
                label = { Text("Server manzili") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
                colors = fieldColors(),
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(Modifier.height(14.dp))

            OutlinedTextField(
                value = state.password,
                onValueChange = viewModel::onPasswordChange,
                label = { Text("Parol") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Go,
                ),
                keyboardActions = KeyboardActions(onGo = { viewModel.submit(onLoggedIn) }),
                colors = fieldColors(),
                isError = state.error != null,
                modifier = Modifier.fillMaxWidth(),
            )

            if (state.error != null) {
                Spacer(Modifier.height(10.dp))
                Text(
                    text = state.error!!,
                    color = Palette.Rose400,
                    style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
                )
            }

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = { viewModel.submit(onLoggedIn) },
                enabled = !state.submitting,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Palette.Brand600,
                    contentColor = Palette.White,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
            ) {
                if (state.submitting) {
                    CircularProgressIndicator(
                        color = Palette.White,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(20.dp),
                    )
                } else {
                    Text("Kirish")
                }
            }
        }
    }
}

@Composable
private fun fieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = Palette.Brand500,
    unfocusedBorderColor = Palette.Slate700,
    focusedLabelColor = Palette.Brand500,
    unfocusedLabelColor = Palette.Slate400,
    focusedTextColor = Palette.White,
    unfocusedTextColor = Palette.White,
    cursorColor = Palette.Brand500,
    focusedContainerColor = Palette.Slate800,
    unfocusedContainerColor = Palette.Slate800,
)
