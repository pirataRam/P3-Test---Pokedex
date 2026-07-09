package com.example.p3test_pokedex.domain.usecase

import com.example.p3test_pokedex.domain.model.PokemonDetail
import com.example.p3test_pokedex.domain.repository.PokemonRepository

/**
 * Use case to fetch detailed information of a specific Pokemon from the repository.
 *
 * @property pokemonRepository The repository used to fetch Pokemon data.
 */
class GetPokemonDetailUseCase(private val pokemonRepository: PokemonRepository) {
    /**
     * Executes the use case to retrieve details of a Pokemon by its unique ID.
     *
     * @param id The unique identifier of the Pokemon.
     * @return The [PokemonDetail] object.
     */
    suspend operator fun invoke(id: Int): PokemonDetail {
        return pokemonRepository.getPokemonDetail(id)
    }

    /**
     * Executes the use case to retrieve details of a Pokemon by its name.
     *
     * @param name The name of the Pokemon.
     * @return The [PokemonDetail] object.
     */
    suspend operator fun invoke(name: String): PokemonDetail {
        return pokemonRepository.getPokemonDetailByName(name)
    }
}
