package com.example.p3test_pokedex.data.remote.model

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object representing details of a specific Pokémon type.
 *
 * @property name The name of the type.
 * @property url The URL referencing the details of the type.
 */
data class PokemonTypeDto(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String
)
