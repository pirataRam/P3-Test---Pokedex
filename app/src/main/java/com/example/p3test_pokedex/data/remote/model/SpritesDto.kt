package com.example.p3test_pokedex.data.remote.model

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object representing the sprites available for a Pokémon.
 *
 * @property frontDefault The default front image URL.
 * @property other Other sprite styles (such as official artwork).
 */
data class SpritesDto(
    @SerializedName("front_default") val frontDefault: String?,
    @SerializedName("other") val other: OtherSpritesDto?
)
