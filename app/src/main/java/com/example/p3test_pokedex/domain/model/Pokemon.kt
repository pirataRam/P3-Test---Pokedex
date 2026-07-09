package com.example.p3test_pokedex.domain.model

/**
 * Represents a basic Pokemon in the Pokédex list.
 *
 * @property id The unique identifier of the Pokemon.
 * @property name The name of the Pokemon.
 * @property imageUrl The image URL of the Pokemon.
 */
data class Pokemon(
    val id: Int,
    val name: String,
    val imageUrl: String
)
