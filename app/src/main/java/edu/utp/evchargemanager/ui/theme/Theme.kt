package edu.utp.evchargemanager.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Se definen también los niveles "surfaceContainer*" porque Card, NavigationBar y otros
// componentes de Material 3 los usan; si no se indican, aparecerían con el tono morado
// por defecto y romperían la identidad visual.
private val LightColorScheme = lightColorScheme(
    primary = EvGreen,
    onPrimary = Color.White,
    primaryContainer = EvGreenLight,
    onPrimaryContainer = EvGreenDark,
    secondary = EvBlue,
    onSecondary = Color.White,
    secondaryContainer = EvBlueLight,
    onSecondaryContainer = EvBlueDark,
    tertiary = EvAmber,
    onTertiary = EvOnAmber,
    background = EvBackground,
    onBackground = EvText,
    surface = EvSurface,
    onSurface = EvText,
    surfaceVariant = EvSurfaceVariant,
    onSurfaceVariant = EvTextVariant,
    surfaceContainerLowest = Color.White,
    surfaceContainerLow = Color.White,
    surfaceContainer = Color(0xFFEAF4EF),
    surfaceContainerHigh = Color(0xFFE2EDE7),
    surfaceContainerHighest = Color(0xFFDAE6E0),
    outline = EvOutline,
    error = EvError
)

private val DarkColorScheme = darkColorScheme(
    primary = EvGreenOnDark,
    onPrimary = EvOnGreenOnDark,
    primaryContainer = EvGreenContainerDark,
    onPrimaryContainer = EvGreenLight,
    secondary = EvBlueOnDark,
    onSecondary = EvBlueDark,
    secondaryContainer = Color(0xFF0B4A82),
    onSecondaryContainer = EvBlueLight,
    tertiary = EvAmber,
    onTertiary = EvOnAmber,
    background = EvBackgroundDark,
    onBackground = EvTextDark,
    surface = EvSurfaceDark,
    onSurface = EvTextDark,
    surfaceVariant = EvSurfaceVariantDark,
    onSurfaceVariant = EvTextVariantDark,
    surfaceContainerLowest = Color(0xFF0B110E),
    surfaceContainerLow = EvSurfaceDark,
    surfaceContainer = Color(0xFF1B2620),
    surfaceContainerHigh = Color(0xFF25312B),
    surfaceContainerHighest = EvSurfaceVariantDark,
    outline = Color(0xFF8A9B93),
    error = EvErrorDark
)

/**
 * Tema de la aplicación. No se usa el color dinámico del sistema a propósito:
 * la paleta propia es parte de la identidad visual de Voltio.
 */
@Composable
fun EvChargeManagerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content
    )
}
