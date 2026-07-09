package com.example.p3test_pokedex.domain.model

/**
 * Represents a summary-level Pokémon entry used in list views throughout the application.
 *
 * This is the lightweight domain model that carries only the essential information
 * needed to render a Pokémon card in the main Pokédex list or the favorites list.
 * For the full set of attributes (types, abilities, stats, etc.), see [PokemonDetail].
 *
 * @property id The unique numeric identifier of the Pokémon as defined by the PokéAPI
 *              (e.g., 1 for Bulbasaur, 25 for Pikachu).
 * @property name The display name of the Pokémon (e.g., "bulbasaur"). Names are typically
 *                returned in lowercase from the API and may be formatted for display in the UI.
 * @property imageUrl The fully-qualified URL pointing to the official artwork or sprite
 *                    image of the Pokémon, used for rendering in list items and detail screens.
 * @see PokemonDetail
 */
data class Pokemon(
    val id: Int,
    val name: String,
    val imageUrl: String
)
