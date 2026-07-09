package com.example.p3test_pokedex.data.remote.model

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object representing a Pokémon type entry.
 *
 * @property slot The order details of the type.
 * @property type The type details.
 */
data class PokemonTypeSlotDto(
    @SerializedName("slot") val slot: Int,
    @SerializedName("type") val type: PokemonTypeDto
)
