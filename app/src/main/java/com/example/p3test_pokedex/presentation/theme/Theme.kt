package com.example.p3test_pokedex.presentation.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// ──────────────────────────────────────────────
//  Extended Pokédex Color Tokens
// ──────────────────────────────────────────────

/**
 * Custom extended colors not covered by Material3's [darkColorScheme]/[lightColorScheme].
 * Provided via [LocalPokedexColors] to all composable consumers.
 */
data class PokedexExtendedColors(
    val favoriteActive: Color,
    val favoriteInactive: Color,
    val shimmerBase: Color,
    val shimmerHighlight: Color,
    val placeholderIcon: Color,
    val placeholderText: Color,
    val emptyStar: Color,
    val emptyText: Color,
)

val LightPokedexExtendedColors = PokedexExtendedColors(
    favoriteActive = FavoriteGold,
    favoriteInactive = FavoriteInactiveLight,
    shimmerBase = ShimmerBaseLight,
    shimmerHighlight = ShimmerHighlightLight,
    placeholderIcon = PlaceholderIconLight,
    placeholderText = PlaceholderTextLight,
    emptyStar = EmptyStarLight,
    emptyText = EmptyTextLight,
)

val DarkPokedexExtendedColors = PokedexExtendedColors(
    favoriteActive = PokedexGoldDark,
    favoriteInactive = FavoriteInactiveDark,
    shimmerBase = ShimmerBaseDark,
    shimmerHighlight = ShimmerHighlightDark,
    placeholderIcon = PlaceholderIconDark,
    placeholderText = PlaceholderTextDark,
    emptyStar = EmptyStarDark,
    emptyText = EmptyTextDark,
)

val LocalPokedexColors = staticCompositionLocalOf { LightPokedexExtendedColors }

// ──────────────────────────────────────────────
//  Material3 Color Schemes
// ──────────────────────────────────────────────

private val LightColorScheme = lightColorScheme(
    primary = PokedexRed,
    onPrimary = OnPokedexRed,
    secondary = PokedexBlue,
    onSecondary = OnPokedexBlue,
    tertiary = PokedexGold,
    onTertiary = OnPokedexGold,
    background = LightBackground,
    onBackground = LightOnSurface,
    surface = LightSurface,
    onSurface = LightOnSurface,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightOnSurfaceVariant,
    error = ErrorLight,
    onError = OnErrorLight,
    outline = OutlineLight,
)

private val DarkColorScheme = darkColorScheme(
    primary = PokedexRedDark,
    onPrimary = OnPokedexRed,
    secondary = PokedexBlueDark,
    onSecondary = OnPokedexBlue,
    tertiary = PokedexGoldDark,
    onTertiary = OnPokedexGold,
    background = DarkBackground,
    onBackground = DarkOnSurface,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkOnSurfaceVariant,
    error = ErrorDark,
    onError = OnErrorDark,
    outline = OutlineDark,
)

// ──────────────────────────────────────────────
//  Theme Composable
// ──────────────────────────────────────────────

@Composable
fun P3TestPokedexTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val extendedColors = if (darkTheme) DarkPokedexExtendedColors else LightPokedexExtendedColors

    CompositionLocalProvider(LocalPokedexColors provides extendedColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
