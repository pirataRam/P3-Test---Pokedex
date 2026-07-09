package com.example.p3test_pokedex.domain.model

/**
 * Represents a stat value of a Pokemon.
 *
 * @property name The name of the stat (e.g., "hp", "attack").
 * @property value The base value of the stat.
 */
data class PokemonStat(
    val name: String,
    val value: Int
)
