package com.example.p3test_pokedex.data.remote.model

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object representing the API response for the Pokémon detail endpoint.
 *
 * @property id The unique identifier of the Pokémon.
 * @property name The name of the Pokémon.
 * @property weight The weight of the Pokémon in hectograms.
 * @property height The height of the Pokémon in decimetres.
 * @property baseExperience The base experience gained for defeating this Pokémon.
 * @property sprites The sprites available for this Pokémon.
 * @property types The list of types this Pokémon belongs to.
 * @property abilities The list of abilities this Pokémon possesses.
 * @property stats The list of base stats for this Pokémon.
 */
data class PokemonDetailDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("weight") val weight: Int,
    @SerializedName("height") val height: Int,
    @SerializedName("base_experience") val baseExperience: Int,
    @SerializedName("sprites") val sprites: SpritesDto,
    @SerializedName("types") val types: List<PokemonTypeSlotDto>,
    @SerializedName("abilities") val abilities: List<PokemonAbilitySlotDto>,
    @SerializedName("stats") val stats: List<PokemonStatSlotDto>
)
