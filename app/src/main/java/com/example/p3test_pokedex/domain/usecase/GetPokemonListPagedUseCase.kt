package com.example.p3test_pokedex.domain.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.p3test_pokedex.data.paging.PokemonPagingSource
import com.example.p3test_pokedex.domain.model.Pokemon
import com.example.p3test_pokedex.domain.repository.PokemonRepository
import kotlinx.coroutines.flow.Flow

/**
 * Use case that encapsulates the business rule of fetching a paginated,
 * reactive stream of Pokémon for the main Pokédex list.
 *
 * **Business rule:** The Pokédex list must load Pokémon incrementally as
 * the user scrolls, fetching pages of 20 items at a time to balance
 * responsiveness with network efficiency. Paging is handled transparently
 * by the Jetpack Paging 3 library.
 *
 * Internally, this use case creates a [Pager] configured with:
 * - **pageSize = 20** — each page request fetches 20 Pokémon.
 * - **enablePlaceholders = false** — the list does not show placeholder items.
 * - **prefetchDistance = 4** — the next page is requested when the user is
 *   within 4 items of the end of the currently loaded data.
 *
 * @property pokemonRepository The [PokemonRepository] used by the underlying
 *                             [PokemonPagingSource] to fetch each page of data.
 * @see PokemonPagingSource
 * @see GetPokemonDetailUseCase
 */
class GetPokemonListPagedUseCase(private val pokemonRepository: PokemonRepository) {
    /**
     * Creates and returns a cold [Flow] of [PagingData] containing [Pokemon] items.
     *
     * Each collection of the returned flow will create a new [PokemonPagingSource]
     * and begin loading pages from the beginning. The flow emits new [PagingData]
     * snapshots as pages are loaded or the data is invalidated.
     *
     * @return A [Flow] emitting [PagingData] of [Pokemon] that can be collected
     *         by a `PagingDataAdapter` or a Compose `LazyPagingItems`.
     */
    operator fun invoke(): Flow<PagingData<Pokemon>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
                prefetchDistance = 4
            ),
            pagingSourceFactory = { PokemonPagingSource(pokemonRepository) }
        ).flow
    }
}
