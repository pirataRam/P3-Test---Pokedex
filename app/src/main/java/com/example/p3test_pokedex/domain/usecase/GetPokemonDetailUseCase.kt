package com.example.p3test_pokedex.domain.usecase

import com.example.p3test_pokedex.domain.model.PokemonDetail
import com.example.p3test_pokedex.domain.repository.PokemonRepository

/**
 * Use case that encapsulates the business rule of fetching the detailed
 * profile of a specific Pokémon.
 *
 * **Business rule:** When the user navigates to a Pokémon's detail screen,
 * the application must load the full profile — including types, abilities,
 * base stats, weight, and height — so it can be rendered in the UI.
 * The Pokémon may be looked up either by its numeric ID or by its name,
 * which is why two `invoke` overloads are provided.
 *
 * This class delegates data fetching to [PokemonRepository] and exposes
 * the operation as a callable `operator fun invoke`.
 *
 * @property pokemonRepository The [PokemonRepository] used to fetch detailed
 *                             Pokémon data from the remote API or local cache.
 * @see PokemonDetail
 * @see GetPokemonListPagedUseCase
 */
class GetPokemonDetailUseCase(private val pokemonRepository: PokemonRepository) {
    /**
     * Fetches the detailed profile of a Pokémon by its unique numeric ID.
     *
     * This is a suspending function and must be called from a coroutine scope.
     *
     * @param id The unique identifier of the Pokémon (e.g., 25 for Pikachu).
     * @return The [PokemonDetail] containing all profile information.
     * @throws Exception if no Pokémon with the given [id] exists or if a
     *         network/data-access error occurs.
     */
    suspend operator fun invoke(id: Int): PokemonDetail {
        return pokemonRepository.getPokemonDetail(id)
    }

    /**
     * Fetches the detailed profile of a Pokémon by its name.
     *
     * This is a suspending function and must be called from a coroutine scope.
     *
     * @param name The name of the Pokémon (e.g., "pikachu"). The lookup is
     *             typically case-insensitive.
     * @return The [PokemonDetail] containing all profile information.
     * @throws Exception if no Pokémon with the given [name] exists or if a
     *         network/data-access error occurs.
     */
    suspend operator fun invoke(name: String): PokemonDetail {
        return pokemonRepository.getPokemonDetailByName(name)
    }
}
