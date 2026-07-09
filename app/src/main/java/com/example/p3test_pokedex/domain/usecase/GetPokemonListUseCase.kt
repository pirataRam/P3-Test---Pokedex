package com.example.p3test_pokedex.domain.usecase

import com.example.p3test_pokedex.domain.model.Pokemon
import com.example.p3test_pokedex.domain.repository.PokemonRepository

/**
 * Use case to fetch a paginated list of Pokemon from the repository.
 *
 * @property pokemonRepository The repository used to fetch Pokemon data.
 */
class GetPokemonListUseCase(private val pokemonRepository: PokemonRepository) {
    /**
     * Executes the use case to retrieve a paginated list of Pokemon.
     *
     * @param limit The maximum number of items to retrieve.
     * @param offset The pagination offset.
     * @return A list of [Pokemon].
     */
    suspend operator fun invoke(limit: Int, offset: Int): List<Pokemon> {
        return pokemonRepository.getPokemonList(limit, offset)
    }
}
