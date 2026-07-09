package com.example.p3test_pokedex.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.p3test_pokedex.domain.model.Pokemon
import com.example.p3test_pokedex.domain.usecase.GetPokemonListPagedUseCase
import kotlinx.coroutines.flow.Flow

/**
 * ViewModel for managing the Pokémon list state and actions.
 * Exposes a Flow of PagingData using Paging3.
 *
 * @property getPokemonListPagedUseCase Use case for fetching the paginated flow of Pokémon.
 */
class PokemonListViewModel(
    private val getPokemonListPagedUseCase: GetPokemonListPagedUseCase
) : ViewModel() {

    /**
     * Flow of PagingData emitting Pokémon items, cached in the ViewModel scope.
     */
    val pokemonPagingDataFlow: Flow<PagingData<Pokemon>> = getPokemonListPagedUseCase()
        .cachedIn(viewModelScope)
}
