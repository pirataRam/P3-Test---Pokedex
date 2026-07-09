package com.example.p3test_pokedex.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.p3test_pokedex.domain.model.Pokemon
import com.example.p3test_pokedex.domain.usecase.CheckInternetConnectionUseCase
import com.example.p3test_pokedex.domain.usecase.GetFavoriteListUseCase
import com.example.p3test_pokedex.domain.usecase.GetPokemonDetailUseCase
import com.example.p3test_pokedex.domain.usecase.GetPokemonListPagedUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Sealed interface representing the possible states of a Pokémon search operation.
 *
 * Consumers should observe [PokemonListViewModel.searchResultState] and update
 * the search results UI based on which variant is emitted.
 *
 * @see PokemonListViewModel
 */
sealed interface SearchResultState {
    /**
     * The initial idle state before the user has performed any search.
     * No search results are displayed, and the default paginated list is shown.
     */
    object Idle : SearchResultState

    /**
     * Indicates that a search operation is currently in progress.
     * The UI should display a loading indicator.
     */
    object Loading : SearchResultState

    /**
     * Indicates that the search operation completed successfully and a matching Pokémon was found.
     *
     * @param pokemon The [Pokemon] domain model matching the search query.
     */
    data class Success(val pokemon: Pokemon) : SearchResultState

    /**
     * Indicates that the search operation failed or no matching Pokémon was found.
     *
     * @param message A human-readable error message describing what went wrong
     *   (e.g., "Pokémon not found" or "No internet connection").
     */
    data class Error(val message: String) : SearchResultState
}

/**
 * ViewModel for managing the Pokémon list screen state, search functionality,
 * favorites management, and network connectivity checks.
 *
 * This ViewModel is responsible for:
 * - Providing a paginated flow of Pokémon data via [pokemonPagingDataFlow] using Paging 3.
 * - Managing the search query state and executing searches by name or ID.
 * - Loading and exposing the user's list of favorite Pokémon.
 * - Validating internet connectivity before navigating to detail screens (for non-favorite items).
 * - Emitting one-shot navigation events via [navigateToDetail] SharedFlow.
 *
 * The favorites list is loaded upon initialization via the `init` block.
 *
 * @param getPokemonListPagedUseCase      Use case for fetching the paginated Pokémon list.
 * @param getPokemonDetailUseCase         Use case for fetching Pokémon details (used for search).
 * @param getFavoriteListUseCase          Use case for retrieving the user's favorite Pokémon list.
 * @param checkInternetConnectionUseCase  Use case for checking network connectivity status.
 *
 * @see SearchResultState
 * @see PokemonListFragment
 */
class PokemonListViewModel(
    private val getPokemonListPagedUseCase: GetPokemonListPagedUseCase,
    private val getPokemonDetailUseCase: GetPokemonDetailUseCase,
    private val getFavoriteListUseCase: GetFavoriteListUseCase,
    private val checkInternetConnectionUseCase: CheckInternetConnectionUseCase
) : ViewModel() {

    /**
     * Paginated flow of [Pokemon] items for infinite-scroll rendering via Paging 3.
     *
     * The flow is cached in [viewModelScope] to survive configuration changes
     * and prevent redundant data fetches.
     */
    // Paginated list flow (Infinite Scroll) using Paging3
    val pokemonPagingDataFlow: Flow<PagingData<Pokemon>> = getPokemonListPagedUseCase()
        .cachedIn(viewModelScope)

    /**
     * The backing mutable state for the current search query text.
     */
    // Search Query State
    private val _searchQuery = MutableStateFlow("")

    /**
     * Observable state of the current search query string.
     * Empty string indicates no active search.
     */
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    /**
     * The backing mutable state for the search operation result.
     */
    // Search Result State
    private val _searchResultState = MutableStateFlow<SearchResultState>(SearchResultState.Idle)

    /**
     * Observable state of the search result, emitting one of the [SearchResultState] variants.
     * Defaults to [SearchResultState.Idle] when no search has been initiated.
     */
    val searchResultState: StateFlow<SearchResultState> = _searchResultState.asStateFlow()

    /**
     * The backing mutable state for the user's list of favorite Pokémon.
     */
    // Favorites List State
    private val _favoritesList = MutableStateFlow<List<Pokemon>>(emptyList())

    /**
     * Observable state of the user's favorite Pokémon list.
     * Emits an empty list if no favorites exist or if loading fails.
     */
    val favoritesList: StateFlow<List<Pokemon>> = _favoritesList.asStateFlow()

    /**
     * The backing mutable state controlling the visibility of the no-internet dialog.
     */
    // Connection dialog visibility state
    private val _showNoInternetDialog = MutableStateFlow(false)

    /**
     * Observable boolean state indicating whether the "No Internet Connection" dialog
     * should be displayed. `true` means the dialog is visible.
     */
    val showNoInternetDialog: StateFlow<Boolean> = _showNoInternetDialog.asStateFlow()

    /**
     * The backing mutable shared flow for one-shot navigation events.
     */
    // Shared Flow to emit single-time navigation actions to the view
    private val _navigateToDetail = MutableSharedFlow<Int>()

    /**
     * One-shot shared flow that emits the Pokémon ID to navigate to the detail screen.
     *
     * Collectors (typically a Fragment's [LaunchedEffect]) should handle each emission
     * exactly once by performing a navigation action. Using [SharedFlow] ensures that
     * navigation events are not replayed on configuration changes.
     */
    val navigateToDetail: SharedFlow<Int> = _navigateToDetail.asSharedFlow()

    init {
        loadFavorites()
    }

    /**
     * Handles a Pokémon click event. Validates internet connectivity for non-favorite tabs
     * before emitting a navigation event.
     *
     * If the click occurs from the favorites tab or the device has internet access,
     * the Pokémon ID is emitted via [navigateToDetail]. Otherwise, the no-internet
     * dialog is displayed by setting [showNoInternetDialog] to `true`.
     *
     * @param id            The unique numeric identifier of the clicked Pokémon.
     * @param isFavoriteTab `true` if the click originates from the favorites tab
     *   (which uses cached data and does not require internet).
     */
    fun onPokemonClicked(id: Int, isFavoriteTab: Boolean) {
        viewModelScope.launch {
            if (isFavoriteTab || checkInternetConnectionUseCase()) {
                _navigateToDetail.emit(id)
            } else {
                _showNoInternetDialog.value = true
            }
        }
    }

    /**
     * Dismisses the "No Internet Connection" dialog by setting [showNoInternetDialog] to `false`.
     */
    fun onDismissNoInternetDialog() {
        _showNoInternetDialog.value = false
    }

    /**
     * Updates the current search query and triggers search execution.
     *
     * If the trimmed query is empty, the search state resets to [SearchResultState.Idle],
     * restoring the default paginated list view. Otherwise, [executeSearch] is called
     * with the trimmed query.
     *
     * @param query The new search query string entered by the user.
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
     * Loads the list of favorite Pokémon from the local Room database.
     *
     * Updates [favoritesList] with the retrieved data. On failure, falls back
     * to an empty list.
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
     * Executes the search operation against the repository's smart cache.
     *
     * Determines whether the query is a numeric ID or a name and calls
     * [GetPokemonDetailUseCase] accordingly. The resulting [PokemonDetail]
     * is mapped to a basic [Pokemon] model for display in the search results.
     *
     * On failure, distinguishes between network errors and "not found" errors
     * to provide appropriate user-facing messages.
     *
     * @param query The trimmed, non-empty search query (either a numeric ID or a Pokémon name).
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
                val errorMessage = if (e is java.io.IOException || e.localizedMessage?.contains("Unable to resolve host", ignoreCase = true) == true) {
                    "Conéctate a internet para reintentar tu búsqueda"
                } else {
                    "Pokémon no encontrado. Verifica el nombre o número y vuelve a intentarlo."
                }
                _searchResultState.value = SearchResultState.Error(errorMessage)
            }
        }
    }
}
