package com.example.p3test_pokedex.domain.usecase

import com.example.p3test_pokedex.domain.repository.PokemonRepository

/**
 * Use case to remove a Pokémon from the favorites list.
 *
 * @property repository The repository to manage Pokemon data.
 */
class RemoveFavoriteUseCase(private val repository: PokemonRepository) {
    /**
     * Executes the use case.
     *
     * @param id The unique identifier of the Pokémon.
     */
    suspend operator fun invoke(id: Int) {
        repository.removeFavorite(id)
    }
}
