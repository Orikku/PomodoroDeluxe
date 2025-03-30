package com.mmb.ui_compose.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorPalette = lightColors(
    primary = OneUIBlue500,
    primaryVariant = OneUIBlue400,
    secondary = OneUIGray500,
    surface = OneUIWhite,
    onPrimary = Color.Black,
    onSecondary = Grey200,
    onBackground = OneUIFont1,
    onSurface = OneUIFont2
)

private val DarkColorPalette = darkColors(
    primary = OneDarkUIBlue500,
    primaryVariant = OneDarkUIBlue400,
    secondary = OneDarkUIGray500,
    surface = OneUIWhite,
    onPrimary = Color.Black,
    onSecondary = Grey200,
    background = Color.Black,
    onBackground = OneDarkUIFont1,
    onSurface = OneDarkUIFont2
)

@Composable
fun PomodoroTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        shapes = Shapes,
        typography = Typography,
        content = content,
    )
}