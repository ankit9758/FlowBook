package com.example.flowbook.ui.theme

import android.app.Activity
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
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Blue80,
    primaryContainer = Purple40.copy(alpha = 0.3f),
    secondaryContainer = PurpleGrey40.copy(alpha = 0.3f),
    surface = Color(0xFF1A1A1A),
    surfaceVariant = Color(0xFF2D2D2D),
    onSurface = Color(0xFFE0E0E0),
    onSurfaceVariant = Color(0xFFB0B0B0),
    error = ExpenseRed,
    onError = Color.White,
    errorContainer = ExpenseRed.copy(alpha = 0.1f),
    onErrorContainer = ExpenseRed
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Blue40,
    primaryContainer = Purple40.copy(alpha = 0.1f),
    secondaryContainer = PurpleGrey40.copy(alpha = 0.1f),
    surface = Color(0xFFFFFBFE),
    surfaceVariant = Color(0xFFF3F0F4),
    onSurface = Color(0xFF1C1B1F),
    onSurfaceVariant = Color(0xFF49454F),
    error = ExpenseRed,
    onError = Color.White,
    errorContainer = ExpenseRed.copy(alpha = 0.1f),
    onErrorContainer = ExpenseRed
)

@Composable
fun FlowBookTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
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