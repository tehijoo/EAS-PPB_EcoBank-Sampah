package com.example.registrasisiswa.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val EcoBankColorScheme = lightColorScheme(
    primary = Color(0xFF427329),
    secondary = Color(0xFFA3CD91),
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    // Optional: map other fields to maintain a consistent look
    primaryContainer = Color(0xFFA3CD91),
    onPrimaryContainer = Color.Black,
    surfaceVariant = Color(0xFFF3E9D7), // Use Pengguna Card color as variant
    onSurfaceVariant = Color.Black
)

@Composable
fun EcoBankTheme(content: @Composable () -> Unit) {
    val colorScheme = EcoBankColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
