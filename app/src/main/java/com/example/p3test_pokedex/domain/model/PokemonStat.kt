package com.example.p3test_pokedex.domain.model

/**
 * Represents a single base-stat entry for a Pokémon.
 *
 * Each Pokémon has six core stats (HP, Attack, Defense, Special Attack,
 * Special Defense, and Speed). This model carries the stat's name together
 * with its base numeric value, and is used within [PokemonDetail.stats] to
 * render stat bars on the detail screen.
 *
 * @property name The canonical name of the stat as returned by the PokéAPI
 *                (e.g., "hp", "attack", "defense", "special-attack",
 *                "special-defense", "speed").
 * @property value The base value of the stat. Values typically range from 1 to 255,
 *                 with higher values indicating greater proficiency in that area.
 * @see PokemonDetail
 */
data class PokemonStat(
    val name: String,
    val value: Int
)
