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

/**
 * Rick and Morty Dark Color Scheme
 * Optimizado para modo oscuro con el tema de la serie
 */
private val DarkColorScheme = darkColorScheme(
    // Colores principales
    primary = GreenPortal,              // 💚 Verde portal para botones y acciones principales
    onPrimary = GrayDark,               // Texto sobre verde portal
    primaryContainer = GreenDark,        // 🟢 Verde oscuro para contenedores
    onPrimaryContainer = WhiteSmoke,     // Texto en contenedores

    // Colores secundarios
    secondary = PurpleCosmic,            // 💜 Morado cósmico para acciones secundarias
    onSecondary = WhiteSmoke,
    secondaryContainer = PurpleCosmicVariant,
    onSecondaryContainer = WhiteSmoke,

    // Colores terciarios
    tertiary = GreenPortalVariant,
    onTertiary = GrayDark,
    tertiaryContainer = GreenDarkVariant,
    onTertiaryContainer = WhiteSmoke,

    // Fondos y superficies
    background = GrayDark,               // ⚫ Gris oscuro para fondo general
    onBackground = WhiteSmoke,           // ⚪ Blanco humo para texto principal
    surface = GrayDarkVariant,           // Superficie (cards, etc)
    onSurface = WhiteSmoke,
    surfaceVariant = GrayDarkVariant,
    onSurfaceVariant = WhiteSmokeVariant,

    // Bordes y divisores
    outline = PurpleCosmic,              // 💜 Morado para bordes
    outlineVariant = GreenDark,

    // Error
    error = ErrorRed,
    onError = WhiteSmoke
)

/**
 * Rick and Morty Light Color Scheme
 * Versión clara del tema (menos común pero disponible)
 */
private val LightColorScheme = lightColorScheme(
    // Colores principales
    primary = GreenDark,                 // Verde oscuro como principal en tema claro
    onPrimary = WhiteSmoke,
    primaryContainer = GreenPortal,      // Verde portal para contenedores
    onPrimaryContainer = GrayDark,

    // Colores secundarios
    secondary = PurpleCosmicVariant,
    onSecondary = WhiteSmoke,
    secondaryContainer = PurpleCosmic,
    onSecondaryContainer = GrayDark,

    // Colores terciarios
    tertiary = GreenDarkVariant,
    onTertiary = WhiteSmoke,
    tertiaryContainer = GreenPortalVariant,
    onTertiaryContainer = GrayDark,

    // Fondos y superficies
    background = WhiteSmoke,
    onBackground = GrayDark,
    surface = WhiteSmokeVariant,
    onSurface = GrayDark,
    surfaceVariant = WhiteSmokeVariant,
    onSurfaceVariant = GrayDarkVariant,

    // Bordes y divisores
    outline = PurpleCosmic,
    outlineVariant = GreenDark,

    // Error
    error = ErrorRed,
    onError = WhiteSmoke
)

@Composable
fun RickAndMortyAndroidTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color deshabilitado para usar siempre nuestra paleta Rick and Morty
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