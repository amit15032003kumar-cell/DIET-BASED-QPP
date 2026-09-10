package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
  primary = Emerald500,
  onPrimary = Color.White,
  primaryContainer = Emerald800,
  onPrimaryContainer = Emerald100,
  secondary = Emerald100,
  onSecondary = Emerald900,
  tertiary = CarbsColor,
  background = DarkBg,
  surface = DarkSurface,
  surfaceVariant = DarkSurfaceVariant,
  onBackground = DarkOnSurface,
  onSurface = DarkOnSurface,
  onSurfaceVariant = DarkOnSurfaceVariant,
  outline = DarkOutline
)

private val LightColorScheme = lightColorScheme(
  primary = Emerald600,
  onPrimary = Color.White,
  primaryContainer = Emerald100,
  onPrimaryContainer = Emerald900,
  secondary = Emerald700,
  onSecondary = Color.White,
  tertiary = CarbsColor,
  background = LightBg,
  surface = LightSurface,
  surfaceVariant = LightSurfaceVariant,
  onBackground = LightOnSurface,
  onSurface = LightOnSurface,
  onSurfaceVariant = LightOnSurfaceVariant,
  outline = LightOutline
)

@Composable
fun NutriTrackTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
