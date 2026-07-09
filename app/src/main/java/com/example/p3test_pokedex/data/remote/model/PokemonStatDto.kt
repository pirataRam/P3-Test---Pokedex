package com.example.p3test_pokedex.data.remote.model

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object representing details of a specific Pokémon stat.
 *
 * @property name The name of the stat.
 * @property url The URL referencing the details of the stat.
 */
data class PokemonStatDto(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String
)
