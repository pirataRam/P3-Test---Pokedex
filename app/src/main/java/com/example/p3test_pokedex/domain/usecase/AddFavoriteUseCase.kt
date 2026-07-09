package com.example.p3test_pokedex.domain.usecase

import com.example.p3test_pokedex.domain.model.Pokemon
import com.example.p3test_pokedex.domain.repository.PokemonRepository

/**
 * Use case to add a Pokémon to the favorites list.
 *
 * @property repository The repository to manage Pokemon data.
 */
class AddFavoriteUseCase(private val repository: PokemonRepository) {
    /**
     * Executes the use case.
     *
     * @param pokemon The Pokémon model to add.
     */
    suspend operator fun invoke(pokemon: Pokemon) {
        repository.addFavorite(pokemon)
    }
}
