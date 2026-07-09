package com.example.p3test_pokedex.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room Database entity representing a user's favorite Pokémon.
 *
 * @property id The unique identifier of the Pokémon.
 * @property name The name of the Pokémon.
 * @property imageUrl The image URL of the Pokémon.
 */
@Entity(tableName = "favorite_pokemon")
data class FavoritePokemonEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val imageUrl: String
)
