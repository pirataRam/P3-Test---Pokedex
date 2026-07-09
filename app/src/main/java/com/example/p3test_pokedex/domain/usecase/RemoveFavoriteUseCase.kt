package com.example.p3test_pokedex.domain.usecase

import com.example.p3test_pokedex.domain.repository.PokemonRepository

/**
 * Use case that encapsulates the business rule of removing a Pokémon
 * from the user's favorites list.
 *
 * **Business rule:** The user can un-favorite a Pokémon from either the
 * detail screen or the favorites list. If the Pokémon is not currently
 * a favorite, the operation completes silently without error.
 *
 * This class delegates the deletion to [PokemonRepository.removeFavorite]
 * and exposes the operation as a callable `operator fun invoke`.
 *
 * @property repository The [PokemonRepository] used to delete the favorite entry.
 * @see AddFavoriteUseCase
 * @see IsFavoriteUseCase
 * @see GetFavoriteListUseCase
 */
class RemoveFavoriteUseCase(private val repository: PokemonRepository) {
    /**
     * Removes the Pokémon with the given ID from the favorites list.
     *
     * This is a suspending function and must be called from a coroutine scope.
     * If the Pokémon is not a favorite, the operation is a no-op.
     *
     * @param id The unique identifier of the Pokémon to remove from favorites.
     */
    suspend operator fun invoke(id: Int) {
        repository.removeFavorite(id)
    }
}
