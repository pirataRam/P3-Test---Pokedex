package com.example.p3test_pokedex.domain.usecase

import com.example.p3test_pokedex.domain.repository.PokemonRepository

/**
 * Use case to check if a specific Pokémon is in the favorites list.
 *
 * @property repository The repository to manage Pokemon data.
 */
class IsFavoriteUseCase(private val repository: PokemonRepository) {
    /**
     * Executes the use case.
     *
     * @param id The unique identifier of the Pokémon.
     * @return True if the Pokémon is a favorite, false otherwise.
     */
    suspend operator fun invoke(id: Int): Boolean {
        return repository.isFavorite(id)
    }
}
