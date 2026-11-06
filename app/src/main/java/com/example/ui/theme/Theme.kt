package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

import androidx.compose.material3.lightColorScheme

private val WinzoColorScheme = lightColorScheme(
    primary = WinzoCrimson,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFEBEE),
    onPrimaryContainer = WinzoCrimson,
    secondary = WinzoGold,
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFFFFF8E1),
    onSecondaryContainer = Color(0xFFE65100),
    tertiary = WinzoEmerald,
    onTertiary = Color.White,
    background = WinzoBgDark,
    onBackground = WinzoTextPrimary,
    surface = WinzoSurfaceDark,
    onSurface = WinzoTextPrimary,
    surfaceVariant = Color(0xFFFFFFFF),
    onSurfaceVariant = WinzoTextSecondary,
    outline = WinzoBorderSubtle,
    outlineVariant = WinzoBorderGold
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = WinzoColorScheme,
        typography = Typography,
        content = content
    )
}
