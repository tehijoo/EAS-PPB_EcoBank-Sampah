// ui/theme/Theme.kt

package com.example.registrasisiswa.ui.theme

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
    primary = Brown80,
    secondary = BrownGrey80,
    tertiary = Cream80
)

private val LightColorScheme = lightColorScheme(
    primary = Brown40,          // Cokelat Tua untuk area atas & tombol
    onPrimary = Color.White,    // Teks putih untuk area atas & tombol
    secondary = BrownGrey40,
    tertiary = Cream80,
    background = Cream40,       // Cream untuk latar belakang layar
    surface = Cream40,
    onBackground = Brown40,     // Teks cokelat di atas background cream
    onSurface = Brown40
)

@Composable
fun RegistrasiSiswaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Paksa false agar dynamic color tidak mengacaukan tema cokelat
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