package com.example.social.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


private val DarkColorScheme = darkColorScheme(
    primary = Color.LightGray,
    secondary = PurpleGrey80,
    tertiary = Pink80,

    background = Color(0xFF1C1C1C), // Màu nền tối
    surface = Color(0xFF121212),
    onPrimary = Color.White, // Màu chữ trên primary
    onSecondary = Color.Black,
    onBackground = Color(0xFFFFFBFE), // Màu chữ trên nền tối
    onSurface = Color(0xFFFFFBFE),

)

private val LightColorScheme = lightColorScheme(
    primary = Color.LightGray,
    secondary = PurpleGrey40,
    tertiary = Pink40,

    //Other default colors to override
    background = Color(0xFFFFFBFE),//màu nền trắng
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.Black,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),//màu chữ trên nền sáng
    onSurface = Color(0xFF1C1B1F),

)

@Composable
fun SocialTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}




