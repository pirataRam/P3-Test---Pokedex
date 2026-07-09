package com.example.p3test_pokedex.domain.usecase

import com.example.p3test_pokedex.domain.model.Pokemon
import com.example.p3test_pokedex.domain.repository.PokemonRepository

/**
 * Use case that encapsulates the business rule of adding a Pokémon to the
 * user's favorites list.
 *
 * **Business rule:** Any Pokémon displayed in the Pokédex can be saved as
 * a favorite. If the Pokémon is already a favorite, the operation is
 * idempotent and completes without error.
 *
 * This class follows the single-responsibility principle: it delegates
 * persistence to [PokemonRepository.addFavorite] and exposes the operation
 * as a callable `operator fun invoke`.
 *
 * @property repository The [PokemonRepository] used to persist the favorite entry.
 * @see RemoveFavoriteUseCase
 * @see IsFavoriteUseCase
 * @see GetFavoriteListUseCase
 */
class AddFavoriteUseCase(private val repository: PokemonRepository) {
    /**
     * Adds the specified Pokémon to the favorites list.
     *
     * This is a suspending function and must be called from a coroutine scope.
     *
     * @param pokemon The [Pokemon] domain model to mark as a favorite.
     */
    suspend operator fun invoke(pokemon: Pokemon) {
        repository.addFavorite(pokemon)
    }
}
