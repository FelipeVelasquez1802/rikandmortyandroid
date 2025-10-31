package com.infinitum.labs.rickandmorty_android.ui.theme

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

private val DarkColorScheme = darkColorScheme(
    primary = GreenPortal,
    onPrimary = GrayDark,
    primaryContainer = GreenDark,
    onPrimaryContainer = WhiteSmoke,

    secondary = PurpleCosmic,
    onSecondary = WhiteSmoke,
    secondaryContainer = PurpleCosmicVariant,
    onSecondaryContainer = WhiteSmoke,

    tertiary = GreenPortalVariant,
    onTertiary = GrayDark,
    tertiaryContainer = GreenDarkVariant,
    onTertiaryContainer = WhiteSmoke,

    background = GrayDark,
    onBackground = WhiteSmoke,
    surface = GrayDarkVariant,
    onSurface = WhiteSmoke,
    surfaceVariant = GrayDarkVariant,
    onSurfaceVariant = WhiteSmokeVariant,

    outline = PurpleCosmic,
    outlineVariant = GreenDark,

    error = ErrorRed,
    onError = WhiteSmoke
)

private val LightColorScheme = lightColorScheme(
    primary = GreenDark,
    onPrimary = WhiteSmoke,
    primaryContainer = GreenPortal,
    onPrimaryContainer = GrayDark,

    secondary = PurpleCosmicVariant,
    onSecondary = WhiteSmoke,
    secondaryContainer = PurpleCosmic,
    onSecondaryContainer = GrayDark,

    tertiary = GreenDarkVariant,
    onTertiary = WhiteSmoke,
    tertiaryContainer = GreenPortalVariant,
    onTertiaryContainer = GrayDark,

    background = WhiteSmoke,
    onBackground = GrayDark,
    surface = WhiteSmokeVariant,
    onSurface = GrayDark,
    surfaceVariant = WhiteSmokeVariant,
    onSurfaceVariant = GrayDarkVariant,

    outline = PurpleCosmic,
    outlineVariant = GreenDark,

    error = ErrorRed,
    onError = WhiteSmoke
)

@Composable
fun RickAndMortyAndroidTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
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