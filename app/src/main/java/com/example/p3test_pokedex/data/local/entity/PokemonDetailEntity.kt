package com.example.p3test_pokedex.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.p3test_pokedex.domain.model.PokemonStat

/**
 * Room Database entity representing cached Pokémon details.
 *
 * @property id The unique identifier of the Pokémon.
 * @property name The name of the Pokémon.
 * @property imageUrl The image URL of the Pokémon.
 * @property types The list of type names (e.g., "fire", "flying").
 * @property abilities The list of ability names.
 * @property stats The list of Pokémon stats.
 * @property weight The weight of the Pokémon.
 * @property height The height of the Pokémon.
 * @property baseExperience The base experience of the Pokémon.
 */
@Entity(tableName = "pokemon_detail")
data class PokemonDetailEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val imageUrl: String,
    val types: List<String>,
    val abilities: List<String>,
    val stats: List<PokemonStat>,
    val weight: Int,
    val height: Int,
    val baseExperience: Int
)
