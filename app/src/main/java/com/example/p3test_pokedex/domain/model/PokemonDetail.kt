package com.example.p3test_pokedex.domain.model

/**
 * Represents the full detailed information of a specific Pokémon.
 *
 * This domain model is used on the detail screen and contains everything
 * needed to display a Pokémon's profile, including its typing, abilities,
 * base stats, and physical measurements. It is retrieved via
 * [com.example.p3test_pokedex.domain.repository.PokemonRepository.getPokemonDetail]
 * or [com.example.p3test_pokedex.domain.repository.PokemonRepository.getPokemonDetailByName].
 *
 * @property id The unique numeric identifier of the Pokémon as defined by the PokéAPI
 *              (e.g., 1 for Bulbasaur, 25 for Pikachu).
 * @property name The display name of the Pokémon (e.g., "bulbasaur"). Typically returned
 *                in lowercase from the API.
 * @property imageUrl The fully-qualified URL pointing to the official artwork or sprite
 *                    image of the Pokémon.
 * @property types The list of type names this Pokémon belongs to (e.g., ["fire", "flying"]).
 *                 A Pokémon can have one or two types.
 * @property abilities The list of ability names this Pokémon can possess
 *                     (e.g., ["overgrow", "chlorophyll"]). Includes both regular and
 *                     hidden abilities.
 * @property stats The list of base [PokemonStat] entries for this Pokémon, covering
 *                 attributes such as HP, Attack, Defense, Special Attack, Special Defense,
 *                 and Speed.
 * @property weight The weight of the Pokémon in hectograms (e.g., 69 means 6.9 kg).
 *                  Divide by 10 to convert to kilograms.
 * @property height The height of the Pokémon in decimetres (e.g., 7 means 0.7 m).
 *                  Divide by 10 to convert to metres.
 * @property baseExperience The base experience points gained when this Pokémon is defeated
 *                          in battle. Used as a general indicator of the Pokémon's strength.
 * @see Pokemon
 * @see PokemonStat
 */
data class PokemonDetail(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val types: List<String>,
    val abilities: List<String>,
    val stats: List<PokemonStat>,
    val weight: Int,
    val height: Int,
    val baseExperience: Int
)
