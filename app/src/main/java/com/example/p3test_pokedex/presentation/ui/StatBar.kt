package com.example.p3test_pokedex.presentation.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.p3test_pokedex.presentation.theme.StatHP
import com.example.p3test_pokedex.presentation.theme.StatAttack
import com.example.p3test_pokedex.presentation.theme.StatDefense
import com.example.p3test_pokedex.presentation.theme.StatSpecialAttack
import com.example.p3test_pokedex.presentation.theme.StatSpecialDefense
import com.example.p3test_pokedex.presentation.theme.StatSpeed
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.util.Locale

/**
 * A beautiful, animated progress bar visualizing Pokémon stats (HP, Attack, Defense, Speed, etc.).
 *
 * @param name The original stat name from PokeAPI.
 * @param value The value of the stat.
 * @param maxValue The maximum value used to calculate percentage. Defaults to 255.
 * @param modifier The modifier to be applied to the row layout.
 */
@Composable
fun StatBar(
    name: String,
    value: Int,
    maxValue: Int = 255,
    modifier: Modifier = Modifier
) {
    val statLabel = when (name.lowercase(Locale.getDefault())) {
        "hp" -> "HP"
        "attack" -> "ATK"
        "defense" -> "DEF"
        "special-attack" -> "SATK"
        "special-defense" -> "SDEF"
        "speed" -> "SPD"
        else -> name.uppercase(Locale.getDefault()).take(4)
    }

    val progressColor = when (name.lowercase(Locale.getDefault())) {
        "hp" -> StatHP
        "attack" -> StatAttack
        "defense" -> StatDefense
        "special-attack" -> StatSpecialAttack
        "special-defense" -> StatSpecialDefense
        "speed" -> StatSpeed
        else -> MaterialTheme.colorScheme.primary
    }

    var progressTarget by remember { mutableFloatStateOf(0f) }
    val animatedProgress by animateFloatAsState(
        targetValue = progressTarget,
        animationSpec = tween(durationMillis = 800),
        label = "stat_progress"
    )

    LaunchedEffect(value) {
        progressTarget = (value.toFloat() / maxValue.toFloat()).coerceIn(0f, 1f)
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = statLabel,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.width(48.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = value.toString(),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.End,
            modifier = Modifier.width(36.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier
                .weight(1f)
                .height(8.dp),
            color = progressColor,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
            strokeCap = StrokeCap.Round
        )
    }
}
