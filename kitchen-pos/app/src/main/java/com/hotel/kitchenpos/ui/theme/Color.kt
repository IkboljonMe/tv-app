package com.hotel.kitchenpos.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Palette mirrored from the hotel-menu web POS (Tailwind tokens) so the
 * kitchen screen looks identical across web and Android.
 */
object Palette {
    // brand (orange) — Tailwind "brand" scale used by hotel-menu.
    val Brand50 = Color(0xFFFFF7ED)
    val Brand100 = Color(0xFFFFEDD5)
    val Brand500 = Color(0xFFF97316)
    val Brand600 = Color(0xFFEA580C)
    val Brand700 = Color(0xFFC2410C)

    // slate — board background and surfaces.
    val Slate100 = Color(0xFFF1F5F9)
    val Slate400 = Color(0xFF94A3B8)
    val Slate600 = Color(0xFF475569)
    val Slate700 = Color(0xFF334155)
    val Slate800 = Color(0xFF1E293B)
    val Slate900 = Color(0xFF0F172A)

    // status accents.
    val Amber500 = Color(0xFFF59E0B)
    val Amber50 = Color(0xFFFFFBEB)
    val Amber800 = Color(0xFF92400E)
    val Blue500 = Color(0xFF3B82F6)
    val Emerald400 = Color(0xFF34D399)
    val Emerald500 = Color(0xFF10B981)
    val Rose400 = Color(0xFFFB7185)
    val Rose50 = Color(0xFFFFF1F2)
    val Rose600 = Color(0xFFE11D48)

    val White = Color(0xFFFFFFFF)
}
