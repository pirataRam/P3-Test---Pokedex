package com.example.p3test_pokedex.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.p3test_pokedex.domain.model.Pokemon
import com.example.p3test_pokedex.domain.usecase.GetFavoriteListUseCase
import com.example.p3test_pokedex.domain.usecase.GetPokemonDetailUseCase
import com.example.p3test_pokedex.domain.usecase.GetPokemonListPagedUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Sealed interface representing search results.
 */
sealed interface SearchResultState {
    object Idle : SearchResultState
    object Loading : SearchResultState
    data class Success(val pokemon: Pokemon) : SearchResultState
    data class Error(val message: String) : SearchResultState
}

/**
 * ViewModel for managing the Pokémon list state, search, and favorites.
 */
class PokemonListViewModel(
    private val getPokemonListPagedUseCase: GetPokemonListPagedUseCase,
    private val getPokemonDetailUseCase: GetPokemonDetailUseCase,
    private val getFavoriteListUseCase: GetFavoriteListUseCase
) : ViewModel() {

    // Paginated list flow (Infinite Scroll) using Paging3
    val pokemonPagingDataFlow: Flow<PagingData<Pokemon>> = getPokemonListPagedUseCase()
        .cachedIn(viewModelScope)

    // Search Query State
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Search Result State
    private val _searchResultState = MutableStateFlow<SearchResultState>(SearchResultState.Idle)
    val searchResultState: StateFlow<SearchResultState> = _searchResultState.asStateFlow()

    // Favorites List State
    private val _favoritesList = MutableStateFlow<List<Pokemon>>(emptyList())
    val favoritesList: StateFlow<List<Pokemon>> = _favoritesList.asStateFlow()

    init {
        loadFavorites()
    }

    /**
     * Updates the search query and triggers search execution.
     */
    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        if (query.trim().isEmpty()) {
            _searchResultState.value = SearchResultState.Idle
        } else {
            executeSearch(query.trim())
        }
    }

    /**
     * Loads the list of favorite Pokémon from Room database.
     */
    fun loadFavorites() {
        viewModelScope.launch {
            try {
                val favorites = getFavoriteListUseCase()
                _favoritesList.value = favorites
            } catch (e: Exception) {
                _favoritesList.value = emptyList()
            }
        }
    }

    /**
     * Executes the search operation. Uses the repository's Smart Cache.
     */
    private fun executeSearch(query: String) {
        _searchResultState.value = SearchResultState.Loading
        viewModelScope.launch {
            try {
                // Try searching by ID if it's a number, otherwise search by Name
                val pokemonDetail = if (query.all { it.isDigit() }) {
                    getPokemonDetailUseCase(query.toInt())
                } else {
                    getPokemonDetailUseCase(query.lowercase())
                }
                
                // Map the result details to a basic Pokemon item
                val pokemon = Pokemon(
                    id = pokemonDetail.id,
                    name = pokemonDetail.name,
                    imageUrl = pokemonDetail.imageUrl
                )
                _searchResultState.value = SearchResultState.Success(pokemon)
            } catch (e: Exception) {
                _searchResultState.value = SearchResultState.Error(
                    e.localizedMessage ?: "Pokémon no encontrado"
                )
            }
        }
    }
}
