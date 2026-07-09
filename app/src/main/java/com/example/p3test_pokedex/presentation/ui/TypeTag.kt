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
import com.example.p3test_pokedex.presentation.theme.TypeGrass
import com.example.p3test_pokedex.presentation.theme.TypeFire
import com.example.p3test_pokedex.presentation.theme.TypeWater
import com.example.p3test_pokedex.presentation.theme.TypeBug
import com.example.p3test_pokedex.presentation.theme.TypeNormal
import com.example.p3test_pokedex.presentation.theme.TypePoison
import com.example.p3test_pokedex.presentation.theme.TypeElectric
import com.example.p3test_pokedex.presentation.theme.TypeGround
import com.example.p3test_pokedex.presentation.theme.TypeFairy
import com.example.p3test_pokedex.presentation.theme.TypeFighting
import com.example.p3test_pokedex.presentation.theme.TypePsychic
import com.example.p3test_pokedex.presentation.theme.TypeRock
import com.example.p3test_pokedex.presentation.theme.TypeGhost
import com.example.p3test_pokedex.presentation.theme.TypeIce
import com.example.p3test_pokedex.presentation.theme.TypeDragon
import com.example.p3test_pokedex.presentation.theme.TypeDark
import com.example.p3test_pokedex.presentation.theme.TypeSteel
import com.example.p3test_pokedex.presentation.theme.TypeFlying
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
