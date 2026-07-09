package com.example.p3test_pokedex.data.remote

import com.example.p3test_pokedex.data.remote.model.PokemonDetailDto
import com.example.p3test_pokedex.data.remote.model.PokemonListResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit service definition for the PokéAPI.
 */
interface PokeApiService {

    /**
     * Fetches a list of Pokémon with limit and offset pagination.
     *
     * @param limit The maximum number of Pokémon to fetch.
     * @param offset The starting position in the list.
     * @return The DTO containing the list of Pokémon results.
     */
    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): PokemonListResponseDto

    /**
     * Fetches details of a specific Pokémon by its ID.
     *
     * @param id The ID of the Pokémon.
     * @return The DTO containing the Pokémon's detailed data.
     */
    @GET("pokemon/{id}")
    suspend fun getPokemonDetail(
        @Path("id") id: Int
    ): PokemonDetailDto

    /**
     * Fetches details of a specific Pokémon by its name.
     *
     * @param name The name of the Pokémon.
     * @return The DTO containing the Pokémon's detailed data.
     */
    @GET("pokemon/{name}")
    suspend fun getPokemonDetailByName(
        @Path("name") name: String
    ): PokemonDetailDto
}
