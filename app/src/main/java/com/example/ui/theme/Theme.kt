package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val GoogleMapsColorScheme =
  lightColorScheme(
    primary = GMapBlue,
    onPrimary = GMapSurface,
    primaryContainer = GMapBlueLight,
    onPrimaryContainer = GMapBlueDark,
    secondary = GMapGreen,
    onSecondary = GMapSurface,
    secondaryContainer = GMapGreenLight,
    onSecondaryContainer = GMapGreen,
    tertiary = GMapYellow,
    onTertiary = GMapSurface,
    background = GMapBackground,
    onBackground = GMapTextPrimary,
    surface = GMapSurface,
    onSurface = GMapTextPrimary,
    surfaceVariant = GMapBackground,
    onSurfaceVariant = GMapTextSecondary,
    outline = GMapBorder
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = false,
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  MaterialTheme(
    colorScheme = GoogleMapsColorScheme,
    typography = Typography,
    content = content
  )
}
