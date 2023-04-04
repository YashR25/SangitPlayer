package com.example.sangitplayer.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@SuppressLint("ConflictingOnColor")
private val DarkColorPalette = darkColors(
    primary = Color(0xFF24293E),
    primaryVariant = Color(0xFF8EBBFF),
    secondary = Color(0xFFCCCCCC),
    onPrimary = Color(0xFFF4F5FC),
)

private val LightColorPalette = lightColors(
    primary = Color(0xFF24293E),
    primaryVariant = Color(0xFF8EBBFF),
    secondary = Color(0xFFCCCCCC),
    onPrimary = Color(0xFFF4F5FC)

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun SangitPlayerTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}