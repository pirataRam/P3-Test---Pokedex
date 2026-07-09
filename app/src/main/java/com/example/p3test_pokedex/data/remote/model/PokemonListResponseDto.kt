package com.example.p3test_pokedex.data.remote.model

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object representing the API response for the Pokémon list endpoint.
 *
 * @property count The total number of Pokémon available in the API.
 * @property next The URL for the next page of results, or null if there is none.
 * @property previous The URL for the previous page of results, or null if there is none.
 * @property results The list of basic Pokémon results in this page.
 */
data class PokemonListResponseDto(
    @SerializedName("count") val count: Int,
    @SerializedName("next") val next: String?,
    @SerializedName("previous") val previous: String?,
    @SerializedName("results") val results: List<PokemonResultDto>
)
