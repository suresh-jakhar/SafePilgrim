package com.example.sp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Azure,
    secondary = Teal,
    tertiary = Amber,
    background = Color(0xFF0B1220),
    surface = Color(0xFF101827)
)

private val LightColorScheme = lightColorScheme(
    primary = Azure,
    secondary = Teal,
    tertiary = Amber,
    background = Color(0xFFF8FAFC),
    surface = Color(0xFFFFFFFF)
)

@Composable
fun SPTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Use our vibrant custom colors for a consistent brand feel
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}