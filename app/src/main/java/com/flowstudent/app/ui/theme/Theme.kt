package com.flowstudent.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Paleta Zent - Modo Oscuro
val ZentDarkColorScheme = darkColorScheme(
    primary = Color(0xFFD4AF37), // Dorado
    onPrimary = Color(0xFF000000), // Negro para texto en botones dorados
    background = Color(0xFF0A0A0A), // Negro profundo
    onBackground = Color(0xFFFFFFFF), // Blanco para texto principal
    surface = Color(0xFF141414), // Negro suave para tarjetas
    onSurface = Color(0xFFFFFFFF),
    outline = Color(0xFF2A2A2A), // Gris oscuro para bordes
    secondary = Color(0xFF9E9E9E), // Gris claro para texto secundario
    error = Color(0xFFCF6679) // Rojo suave
)

// Paleta Zent - Modo Claro
val ZentLightColorScheme = lightColorScheme(
    primary = Color(0xFFD4AF37), // Dorado
    onPrimary = Color(0xFF000000),
    background = Color(0xFFFFFFFF), // Blanco
    onBackground = Color(0xFF000000), // Negro para texto principal
    surface = Color(0xFFF5F5F5), // Blanco humo para tarjetas
    onSurface = Color(0xFF000000),
    outline = Color(0xFFE0E0E0), // Gris muy claro para bordes
    secondary = Color(0xFF616161), // Gris oscuro para texto secundario
    error = Color(0xFFCF6679)
)

@Composable
fun ZentTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) ZentDarkColorScheme else ZentLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),
        content = content
    )
}
