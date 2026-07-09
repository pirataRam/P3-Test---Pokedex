package com.example.p3test_pokedex.data.remote.model

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object representing a single Pokémon result inside the list response.
 *
 * @property name The name of the Pokémon.
 * @property url The URL pointing to the details of the Pokémon.
 */
data class PokemonResultDto(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String
)
