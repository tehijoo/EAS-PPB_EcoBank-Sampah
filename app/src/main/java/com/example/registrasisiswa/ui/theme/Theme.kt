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
    primary = RoseGold,
    onPrimary = OnRoseGold,
    primaryContainer = RoseGoldLight,
    onPrimaryContainer = OnRoseGoldLight,
    secondary = WarmMauve,
    onSecondary = OnWarmMauve,
    secondaryContainer = WarmMauveLight,
    onSecondaryContainer = Color(0xFF340B27),
    tertiary = GoldenBrown,
    onTertiary = Color.White,
    tertiaryContainer = GoldenBrownLight,
    onTertiaryContainer = Color(0xFF2C1700),
    background = BackgroundCream,
    onBackground = DarkText,
    surface = SurfaceWhite,
    onSurface = DarkText,
    surfaceVariant = SurfaceVariantRose,
    onSurfaceVariant = MediumText,
    error = ErrorRed,
    onError = Color.White,
    errorContainer = ErrorRedBg,
    onErrorContainer = Color(0xFF410002),
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
