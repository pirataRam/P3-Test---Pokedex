package com.example.p3test_pokedex.data.remote.model

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object representing a Pokémon ability entry.
 *
 * @property slot The order details of the ability.
 * @property isHidden Whether this ability is hidden.
 * @property ability The ability details.
 */
data class PokemonAbilitySlotDto(
    @SerializedName("slot") val slot: Int,
    @SerializedName("is_hidden") val isHidden: Boolean,
    @SerializedName("ability") val ability: PokemonAbilityDto
)
