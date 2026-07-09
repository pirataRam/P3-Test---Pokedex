package com.example.p3test_pokedex.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.p3test_pokedex.presentation.theme.*
import java.util.Locale

/**
 * A capsule-shaped tag with custom background colors mapped to Pokémon types.
 * Colors are sourced from the centralized [Color.kt] palette.
 *
 * @param type The Pokémon type (e.g. "grass", "fire", "water").
 * @param modifier The modifier to be applied to the tag.
 */
@Composable
fun TypeTag(
    type: String,
    modifier: Modifier = Modifier
) {
    val color = when (type.lowercase(Locale.getDefault())) {
        "grass" -> TypeGrass
        "fire" -> TypeFire
        "water" -> TypeWater
        "bug" -> TypeBug
        "normal" -> TypeNormal
        "poison" -> TypePoison
        "electric" -> TypeElectric
        "ground" -> TypeGround
        "fairy" -> TypeFairy
        "fighting" -> TypeFighting
        "psychic" -> TypePsychic
        "rock" -> TypeRock
        "ghost" -> TypeGhost
        "ice" -> TypeIce
        "dragon" -> TypeDragon
        "dark" -> TypeDark
        "steel" -> TypeSteel
        "flying" -> TypeFlying
        else -> MaterialTheme.colorScheme.outline
    }

    Text(
        text = type.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
        color = Color.White,
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.labelMedium,
        modifier = modifier
            .background(color = color, shape = RoundedCornerShape(16.dp))
            .padding(horizontal = 16.dp, vertical = 6.dp)
    )
}
