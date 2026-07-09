package com.example.p3test_pokedex.data.remote.model

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object representing details of a specific Pokémon ability.
 *
 * @property name The name of the ability.
 * @property url The URL referencing the details of the ability.
 */
data class PokemonAbilityDto(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String
)
