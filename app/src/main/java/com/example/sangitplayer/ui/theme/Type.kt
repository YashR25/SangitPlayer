package com.example.sangitplayer.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.sangitplayer.R

// Set of Material typography styles to start with
val Typography: Typography
    get() = Typography(
        h1 = TextStyle(
            fontSize = 64.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = markaziText,
        ),
        h2 = TextStyle(
            color = charcoal,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        ),
        body1 = TextStyle(
            fontWeight = FontWeight.Bold,
            fontFamily = karlaText,
            fontSize = 18.sp
        ),
        body2 = TextStyle(
            color = Color.Gray,
            fontWeight = FontWeight.Light,
            fontFamily = karlaText,
            fontSize = 16.sp
        ),
        button = TextStyle(
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        ),
        subtitle1 = TextStyle(
            fontSize = 40.sp,
            fontFamily = markaziText,
            fontWeight = FontWeight.Bold
        ),
        subtitle2 = TextStyle(
            fontSize = 20.sp,
            fontFamily = karlaText,
            fontWeight = FontWeight.Bold
        )
    )

val markaziText = FontFamily(
    Font(R.font.markazi_text_bold, FontWeight.Bold),
    Font(R.font.markazi_text_regular, FontWeight.Normal),
    Font(R.font.markazi_text_semi_bold, FontWeight.SemiBold),
    Font(R.font.markazi_text_medium, FontWeight.Medium)
)

val karlaText = FontFamily(
    Font(R.font.karla_bold, FontWeight.Bold),
    Font(R.font.karla_regular, FontWeight.Normal),
    Font(R.font.karla_semi_bold, FontWeight.SemiBold),
    Font(R.font.markazi_text_medium, FontWeight.Medium),
    Font(R.font.karla_light, FontWeight.Light)
)