package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = NavyPrimary80,
    onPrimary = Color(0xFF0F172A),
    primaryContainer = Color(0xFF1E3A8A),
    onPrimaryContainer = Color(0xFFDBEAFE),
    secondary = NavySecondary80,
    onSecondary = Color(0xFF0F172A),
    secondaryContainer = Color(0xFF334155),
    onSecondaryContainer = Color(0xFFF1F5F9),
    tertiary = AmberAccent80,
    onTertiary = Color(0xFF451A03),
    tertiaryContainer = AmberDarkContainer,
    onTertiaryContainer = Color(0xFFFEF3C7),
    background = BackgroundDark,
    onBackground = OnSurfaceDark,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = OnSurfaceVariantDark,
    outline = OutlineDark
)

private val LightColorScheme = lightColorScheme(
    primary = NavyPrimary,
    onPrimary = Color.White,
    primaryContainer = SkyBlueContainer,
    onPrimaryContainer = OnSkyBlueContainer,
    secondary = SlateSecondary,
    onSecondary = Color.White,
    secondaryContainer = SlateSecondaryLight,
    onSecondaryContainer = Color(0xFF0F172A),
    tertiary = AmberAccent,
    onTertiary = Color.White,
    tertiaryContainer = AmberLightContainer,
    onTertiaryContainer = AmberDarkContainer,
    background = BackgroundLight,
    onBackground = OnSurfaceLight,
    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = OnSurfaceVariantLight,
    outline = OutlineLight
)

@Composable
fun MyApplicationTheme(
    themeMode: com.example.viewmodel.ThemeMode = com.example.viewmodel.ThemeMode.SYSTEM,
    darkTheme: Boolean = when (themeMode) {
        com.example.viewmodel.ThemeMode.SYSTEM -> isSystemInDarkTheme()
        com.example.viewmodel.ThemeMode.LIGHT -> false
        com.example.viewmodel.ThemeMode.DARK -> true
    },
    dynamicColor: Boolean = false, // Keep consistent branding colors
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

