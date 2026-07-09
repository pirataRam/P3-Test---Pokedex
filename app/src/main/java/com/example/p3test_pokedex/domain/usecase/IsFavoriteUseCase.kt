package com.example.p3test_pokedex.domain.usecase

import com.example.p3test_pokedex.domain.repository.PokemonRepository

/**
 * Use case that encapsulates the business rule of checking whether a
 * specific Pokémon is currently in the user's favorites list.
 *
 * **Business rule:** The detail screen must visually indicate the favorite
 * status of the displayed Pokémon (e.g., a filled vs. outlined heart icon).
 * This use case allows the UI layer to query that status without
 * directly accessing the repository.
 *
 * This class delegates the lookup to [PokemonRepository.isFavorite] and
 * exposes the operation as a callable `operator fun invoke`.
 *
 * @property repository The [PokemonRepository] used to query the favorite status.
 * @see AddFavoriteUseCase
 * @see RemoveFavoriteUseCase
 * @see GetFavoriteListUseCase
 */
class IsFavoriteUseCase(private val repository: PokemonRepository) {
    /**
     * Checks whether the Pokémon with the given ID is a favorite.
     *
     * This is a suspending function and must be called from a coroutine scope.
     *
     * @param id The unique identifier of the Pokémon to check.
     * @return `true` if the Pokémon is currently in the favorites list;
     *         `false` otherwise.
     */
    suspend operator fun invoke(id: Int): Boolean {
        return repository.isFavorite(id)
    }
}
