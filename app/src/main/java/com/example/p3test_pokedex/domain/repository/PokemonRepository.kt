package com.example.p3test_pokedex.domain.repository

import com.example.p3test_pokedex.domain.model.Pokemon
import com.example.p3test_pokedex.domain.model.PokemonDetail

/**
 * Interface detailing the data operations related to Pokemon.
 * This represents the contract that must be implemented by the data layer.
 */
interface PokemonRepository {
    /**
     * Fetches a paginated list of Pokemon.
     *
     * @param limit The maximum number of items to return.
     * @param offset The starting offset for pagination.
     * @return A list of [Pokemon] matching the pagination parameters.
     */
    suspend fun getPokemonList(limit: Int, offset: Int): List<Pokemon>

    /**
     * Fetches detailed information of a Pokemon by its unique ID.
     *
     * @param id The unique identifier of the Pokemon.
     * @return The [PokemonDetail] object.
     */
    suspend fun getPokemonDetail(id: Int): PokemonDetail

    /**
     * Fetches detailed information of a Pokemon by its name.
     *
     * @param name The name of the Pokemon.
     * @return The [PokemonDetail] object.
     */
    suspend fun getPokemonDetailByName(name: String): PokemonDetail
}
