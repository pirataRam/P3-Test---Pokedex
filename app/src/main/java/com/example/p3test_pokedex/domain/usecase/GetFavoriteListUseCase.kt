package com.example.p3test_pokedex.domain.usecase

import com.example.p3test_pokedex.domain.model.Pokemon
import com.example.p3test_pokedex.domain.repository.PokemonRepository

/**
 * Use case to retrieve the list of all favorite Pokémon.
 *
 * @property repository The repository to manage Pokemon data.
 */
class GetFavoriteListUseCase(private val repository: PokemonRepository) {
    /**
     * Executes the use case.
     *
     * @return The list of favorite Pokémon.
     */
    suspend operator fun invoke(): List<Pokemon> {
        return repository.getFavorites()
    }
}
