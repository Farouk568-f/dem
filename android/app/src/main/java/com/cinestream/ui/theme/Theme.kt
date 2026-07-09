package com.cinestream.ui.theme

import androidx.compose.foundation.isSystemInDarkMode
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = Red900,
    onPrimary = White,
    primaryContainer = Red800,
    onPrimaryContainer = Red100,
    secondary = Amber800,
    onSecondary = White,
    secondaryContainer = Amber700,
    onSecondaryContainer = Amber100,
    tertiary = Cyan800,
    onTertiary = White,
    tertiaryContainer = Cyan700,
    onTertiaryContainer = Cyan100,
    error = Red600,
    onError = White,
    errorContainer = Red900,
    onErrorContainer = Red100,
    background = Gray950,
    onBackground = White,
    surface = Gray900,
    onSurface = White,
    surfaceVariant = Gray800,
    onSurfaceVariant = Gray100,
    outline = Gray700,
    outlineVariant = Gray600,
    scrim = Black
)

private val LightColorScheme = lightColorScheme(
    primary = Red900,
    onPrimary = White,
    primaryContainer = Red100,
    onPrimaryContainer = Red900,
    secondary = Amber800,
    onSecondary = White,
    secondaryContainer = Amber100,
    onSecondaryContainer = Amber900,
    tertiary = Cyan800,
    onTertiary = White,
    tertiaryContainer = Cyan100,
    onTertiaryContainer = Cyan900,
    error = Red600,
    onError = White,
    errorContainer = Red100,
    onErrorContainer = Red900,
    background = White,
    onBackground = Gray950,
    surface = Gray50,
    onSurface = Gray950,
    surfaceVariant = Gray100,
    onSurfaceVariant = Gray700,
    outline = Gray500,
    outlineVariant = Gray300,
    scrim = Black
)

@Composable
fun CineStreamTheme(
    darkTheme: Boolean = isSystemInDarkMode(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = CineStreamTypography,
        content = content
    )
}
