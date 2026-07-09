package com.example.p3test_pokedex.data.remote.model

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object representing a Pokémon stat entry.
 *
 * @property baseStat The base value of the stat.
 * @property effort The effort value (EV) details.
 * @property stat The stat details.
 */
data class PokemonStatSlotDto(
    @SerializedName("base_stat") val baseStat: Int,
    @SerializedName("effort") val effort: Int,
    @SerializedName("stat") val stat: PokemonStatDto
)
