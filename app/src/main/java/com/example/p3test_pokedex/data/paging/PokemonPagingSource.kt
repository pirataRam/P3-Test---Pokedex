package com.example.p3test_pokedex.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.p3test_pokedex.domain.model.Pokemon
import com.example.p3test_pokedex.domain.repository.PokemonRepository

/**
 * PagingSource for loading Pokemon pages from the PokemonRepository.
 * Uses the offset (Int) as the pagination key.
 */
class PokemonPagingSource(
    private val pokemonRepository: PokemonRepository
) : PagingSource<Int, Pokemon>() {

    override fun getRefreshKey(state: PagingState<Int, Pokemon>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(state.config.pageSize)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(state.config.pageSize)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Pokemon> {
        val offset = params.key ?: 0
        val limit = params.loadSize

        return try {
            val response = pokemonRepository.getPokemonList(limit, offset)
            val nextKey = if (response.isEmpty() || response.size < limit) {
                null
            } else {
                offset + response.size
            }

            LoadResult.Page(
                data = response,
                prevKey = if (offset == 0) null else (offset - limit).coerceAtLeast(0),
                nextKey = nextKey
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }
}
