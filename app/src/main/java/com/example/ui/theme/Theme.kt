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

private val CampusBiteLightColorScheme = lightColorScheme(
    primary = OrangePrimary,
    onPrimary = Color.White,
    primaryContainer = OrangeContainer,
    onPrimaryContainer = OnOrangeContainer,
    secondary = CharcoalMedium,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFF1F5F9),
    onSecondaryContainer = Color(0xFF1E293B),
    tertiary = GreenSuccess,
    onTertiary = Color.White,
    tertiaryContainer = GreenSuccessContainer,
    onTertiaryContainer = OnGreenSuccessContainer,
    background = CreamBackground,
    onBackground = CharcoalDark,
    surface = CreamSurface,
    onSurface = CharcoalDark,
    surfaceVariant = CreamSurfaceVariant,
    onSurfaceVariant = SlateGray,
    outline = Color(0xFFE2E8F0),
    outlineVariant = Color(0xFFCBD5E1)
)

private val CampusBiteDarkColorScheme = darkColorScheme(
    primary = OrangeLight,
    onPrimary = Color(0xFF431B00),
    primaryContainer = Color(0xFF6B2D00),
    onPrimaryContainer = Color(0xFFFFDCC7),
    secondary = Color(0xFF94A3B8),
    onSecondary = Color(0xFF0F172A),
    secondaryContainer = Color(0xFF1E293B),
    onSecondaryContainer = Color(0xFFE2E8F0),
    tertiary = Color(0xFF4ADE80),
    onTertiary = Color(0xFF052E16),
    tertiaryContainer = Color(0xFF14532D),
    onTertiaryContainer = Color(0xFFBBF7D0),
    background = Color(0xFF121417),
    onBackground = Color(0xFFEDEFEF),
    surface = Color(0xFF1A1D20),
    onSurface = Color(0xFFEDEFEF),
    surfaceVariant = Color(0xFF24282D),
    onSurfaceVariant = Color(0xFF9CA3AF),
    outline = Color(0xFF374151),
    outlineVariant = Color(0xFF4B5563)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep CampusBite warm orange identity consistent
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> CampusBiteDarkColorScheme
        else -> CampusBiteLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
