package com.example.p3test_pokedex.domain.usecase

import com.example.p3test_pokedex.domain.model.Pokemon
import com.example.p3test_pokedex.domain.repository.PokemonRepository

/**
 * Use case that encapsulates the business rule of retrieving the user's
 * complete list of favorite Pokémon.
 *
 * **Business rule:** The favorites list displays all Pokémon that the user
 * has previously marked as favorites. The list is typically persisted in a
 * local database so it remains available offline.
 *
 * This class delegates data retrieval to [PokemonRepository.getFavorites]
 * and exposes the operation as a callable `operator fun invoke`.
 *
 * @property repository The [PokemonRepository] used to query favorite entries.
 * @see AddFavoriteUseCase
 * @see RemoveFavoriteUseCase
 * @see IsFavoriteUseCase
 */
class GetFavoriteListUseCase(private val repository: PokemonRepository) {
    /**
     * Retrieves the complete list of Pokémon marked as favorites.
     *
     * This is a suspending function and must be called from a coroutine scope.
     *
     * @return A [List] of [Pokemon] that the user has saved as favorites.
     *         Returns an empty list if no favorites exist.
     */
    suspend operator fun invoke(): List<Pokemon> {
        return repository.getFavorites()
    }
}
