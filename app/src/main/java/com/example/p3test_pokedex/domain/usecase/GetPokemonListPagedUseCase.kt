package com.example.p3test_pokedex.domain.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.p3test_pokedex.data.paging.PokemonPagingSource
import com.example.p3test_pokedex.domain.model.Pokemon
import com.example.p3test_pokedex.domain.repository.PokemonRepository
import kotlinx.coroutines.flow.Flow

/**
 * Use case to fetch a paginated Flow of Pokemon using Paging3.
 */
class GetPokemonListPagedUseCase(private val pokemonRepository: PokemonRepository) {
    /**
     * Executes the use case to retrieve a Flow of PagingData.
     *
     * @return A Flow emitting [PagingData] of [Pokemon].
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
