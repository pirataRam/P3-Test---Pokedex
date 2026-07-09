package com.example.p3test_pokedex.domain.model

/**
 * Represents detailed information of a specific Pokemon.
 *
 * @property id The unique identifier of the Pokemon.
 * @property name The name of the Pokemon.
 * @property imageUrl The image URL of the Pokemon.
 * @property types The list of types this Pokemon belongs to (e.g., "fire", "flying").
 * @property abilities The list of abilities this Pokemon possesses.
 * @property stats The list of stats for this Pokemon (e.g., hp, attack, defense).
 * @property weight The weight of the Pokemon in hectograms.
 * @property height The height of the Pokemon in decimetres.
 * @property baseExperience The base experience gained for defeating this Pokemon.
 */
data class PokemonDetail(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val types: List<String>,
    val abilities: List<String>,
    val stats: List<PokemonStat>,
    val weight: Int,
    val height: Int,
    val baseExperience: Int
)
