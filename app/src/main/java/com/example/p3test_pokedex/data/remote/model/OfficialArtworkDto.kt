package com.example.p3test_pokedex.data.remote.model

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object representing official artwork sprites.
 *
 * @property frontDefault The official front default image URL.
 */
data class OfficialArtworkDto(
    @SerializedName("front_default") val frontDefault: String?
)
