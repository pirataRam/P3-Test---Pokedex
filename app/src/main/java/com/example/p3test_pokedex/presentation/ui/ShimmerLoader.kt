package com.example.p3test_pokedex.presentation.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import com.example.p3test_pokedex.presentation.theme.LocalPokedexColors

/**
 * A beautiful, reusable Shimmer effect Composable that acts as a skeleton loader placeholder.
 * Uses the extended Pokédex theme tokens for proper light/dark mode support.
 *
 * @param modifier The modifier to be applied to the layout.
 */
@Composable
fun ShimmerLoader(
    modifier: Modifier = Modifier
) {
    val colors = LocalPokedexColors.current
    val shimmerColors = listOf(
        colors.shimmerBase,
        colors.shimmerHighlight,
        colors.shimmerBase,
    )

    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_translation"
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnim.value, y = translateAnim.value)
    )

    Box(
        modifier = modifier
            .background(brush)
    )
}
