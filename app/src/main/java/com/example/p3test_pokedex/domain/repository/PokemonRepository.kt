package com.example.p3test_pokedex.domain.repository

import com.example.p3test_pokedex.domain.model.Pokemon
import com.example.p3test_pokedex.domain.model.PokemonDetail

/**
 * Repository contract that defines all data operations related to Pokémon.
 *
 * This interface represents the boundary between the domain layer and the
 * data layer. Implementations are responsible for deciding whether to fetch
 * data from a remote API, a local database, or an in-memory cache, and for
 * mapping raw data transfer objects into domain models.
 *
 * All methods are `suspend` functions and must be called from a coroutine scope.
 *
 * @see Pokemon
 * @see PokemonDetail
 */
interface PokemonRepository {
    /**
     * Fetches a paginated list of Pokémon summaries.
     *
     * This method supports offset-based pagination as defined by the PokéAPI.
     * Callers should increment [offset] by [limit] for each successive page.
     *
     * @param limit The maximum number of Pokémon to return in a single page.
     *              Must be a positive integer (e.g., 20).
     * @param offset The zero-based starting index for the page. For the first
     *               page, pass 0; for the second page with a limit of 20, pass 20.
     * @return A [List] of [Pokemon] matching the pagination parameters. Returns
     *         an empty list if the offset exceeds the total number of Pokémon.
     * @throws Exception if a network or data-access error occurs.
     */
    suspend fun getPokemonList(limit: Int, offset: Int): List<Pokemon>

    /**
     * Fetches the full detail record of a Pokémon by its unique numeric ID.
     *
     * @param id The unique identifier of the Pokémon (e.g., 25 for Pikachu).
     * @return The [PokemonDetail] containing types, abilities, stats, and measurements.
     * @throws Exception if no Pokémon with the given [id] exists or if a
     *         network/data-access error occurs.
     */
    suspend fun getPokemonDetail(id: Int): PokemonDetail

    /**
     * Fetches the full detail record of a Pokémon by its name.
     *
     * @param name The name of the Pokémon (e.g., "pikachu"). The name is
     *             case-insensitive in most API implementations.
     * @return The [PokemonDetail] containing types, abilities, stats, and measurements.
     * @throws Exception if no Pokémon with the given [name] exists or if a
     *         network/data-access error occurs.
     */
    suspend fun getPokemonDetailByName(name: String): PokemonDetail

    /**
     * Retrieves the complete list of Pokémon that the user has marked as favorites.
     *
     * The list is typically sourced from a local database and is returned
     * in insertion order.
     *
     * @return A [List] of [Pokemon] currently saved as favorites. Returns an
     *         empty list if no favorites exist.
     */
    suspend fun getFavorites(): List<Pokemon>

    /**
     * Adds the given Pokémon to the user's favorites list.
     *
     * If the Pokémon already exists in the favorites list, implementations
     * should handle the duplicate gracefully (e.g., no-op or upsert).
     *
     * @param pokemon The [Pokemon] instance to add to favorites.
     */
    suspend fun addFavorite(pokemon: Pokemon)

    /**
     * Removes a Pokémon from the user's favorites list by its unique ID.
     *
     * If no favorite with the given [id] exists, the operation should
     * complete silently without throwing an exception.
     *
     * @param id The unique identifier of the Pokémon to remove from favorites.
     */
    suspend fun removeFavorite(id: Int)

    /**
     * Checks whether a Pokémon with the given ID is currently in the
     * user's favorites list.
     *
     * @param id The unique identifier of the Pokémon to look up.
     * @return `true` if the Pokémon is marked as a favorite; `false` otherwise.
     */
    suspend fun isFavorite(id: Int): Boolean
}
