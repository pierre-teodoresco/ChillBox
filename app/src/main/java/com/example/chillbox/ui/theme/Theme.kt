package com.example.chillbox.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColorScheme(
    primary = Blue500,
    secondary = Blue700,
    tertiary = Blue200,
    background = Gray900,
    surface = Gray900,
    surfaceVariant = Green200,
    onPrimary = Gray50,
    onBackground = Gray50,
    onSurface = Gray50
)

private val LightColorPalette = lightColorScheme(
    primary = Blue500,
    secondary = Blue700,
    tertiary = Blue200,
    background = Gray50,
    surface = Gray50,
    surfaceVariant = Green200,
    onPrimary = Gray900,
    onBackground = Gray900,
    onSurface = Gray900
)

@Composable
fun ChillBoxTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme= if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
