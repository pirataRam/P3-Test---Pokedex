package com.example.p3test_pokedex.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.p3test_pokedex.domain.model.Pokemon
import com.example.p3test_pokedex.domain.model.PokemonDetail
import com.example.p3test_pokedex.domain.repository.AudioPlayer
import com.example.p3test_pokedex.domain.usecase.AddFavoriteUseCase
import com.example.p3test_pokedex.domain.usecase.GetPokemonDetailUseCase
import com.example.p3test_pokedex.domain.usecase.IsFavoriteUseCase
import com.example.p3test_pokedex.domain.usecase.RemoveFavoriteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Sealed interface representing the possible UI states of the Pokémon detail screen.
 *
 * Consumers should observe [PokemonDetailViewModel.uiState] and render UI accordingly
 * based on which variant is emitted.
 *
 * @see PokemonDetailViewModel
 */
sealed interface PokemonDetailUiState {
    /**
     * Indicates that the Pokémon detail data is currently being fetched.
     * The UI should display a loading indicator or shimmer placeholder.
     */
    data object Loading : PokemonDetailUiState

    /**
     * Indicates that the Pokémon detail data has been successfully loaded.
     *
     * @param pokemonDetail The fully loaded [PokemonDetail] domain model containing
     *   all information about the Pokémon (stats, abilities, types, images, etc.).
     */
    data class Success(val pokemonDetail: PokemonDetail) : PokemonDetailUiState

    /**
     * Indicates that an error occurred while fetching the Pokémon detail data.
     *
     * @param message A human-readable error message describing what went wrong.
     */
    data class Error(val message: String) : PokemonDetailUiState
}

/**
 * ViewModel for managing the Pokémon detail screen state and favorite status.
 *
 * This ViewModel is responsible for:
 * - Loading Pokémon details by ID or name via [GetPokemonDetailUseCase].
 * - Tracking the favorite status of the currently displayed Pokémon via [IsFavoriteUseCase].
 * - Adding/removing Pokémon from the favorites list via [AddFavoriteUseCase] and [RemoveFavoriteUseCase].
 * - Playing Pokémon cry audio files via [AudioPlayer], keeping media logic decoupled from the UI.
 *
 * The ViewModel releases audio resources in [onCleared] to prevent memory leaks.
 *
 * @param getPokemonDetailUseCase Use case for fetching detailed Pokémon information.
 * @param isFavoriteUseCase       Use case for checking whether a Pokémon is marked as favorite.
 * @param addFavoriteUseCase      Use case for adding a Pokémon to the favorites list.
 * @param removeFavoriteUseCase   Use case for removing a Pokémon from the favorites list.
 * @param audioPlayer             Abstraction layer for playing audio from network URLs.
 *
 * @see PokemonDetailUiState
 * @see PokemonDetailFragment
 */
class PokemonDetailViewModel(
    private val getPokemonDetailUseCase: GetPokemonDetailUseCase,
    private val isFavoriteUseCase: IsFavoriteUseCase,
    private val addFavoriteUseCase: AddFavoriteUseCase,
    private val removeFavoriteUseCase: RemoveFavoriteUseCase,
    private val audioPlayer: AudioPlayer
) : ViewModel() {

    /**
     * The backing mutable state flow for the detail screen UI state.
     * Starts in [PokemonDetailUiState.Loading].
     */
    private val _uiState = MutableStateFlow<PokemonDetailUiState>(PokemonDetailUiState.Loading)

    /**
     * Observable UI state for the Pokémon detail screen.
     *
     * Emits one of [PokemonDetailUiState.Loading], [PokemonDetailUiState.Success],
     * or [PokemonDetailUiState.Error] depending on the current data-loading status.
     */
    val uiState: StateFlow<PokemonDetailUiState> = _uiState.asStateFlow()

    /**
     * The backing mutable state flow for the favorite status of the current Pokémon.
     */
    private val _isFavorite = MutableStateFlow(false)

    /**
     * Observable boolean state indicating whether the currently displayed Pokémon
     * is marked as a favorite. `true` if the Pokémon is in the user's favorites list,
     * `false` otherwise.
     */
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()

    /**
     * Plays the Pokémon cry sound from the specified network URL.
     *
     * Delegates audio playback to [AudioPlayer], keeping MediaPlayer logic
     * completely decoupled from Composable view code.
     *
     * @param url The network URL pointing to the Pokémon cry audio file (typically an `.ogg` file).
     */
    fun playPokemonCry(url: String) {
        audioPlayer.play(url)
    }

    /**
     * Called when the ViewModel is being destroyed.
     *
     * Releases all audio resources held by the [AudioPlayer] to prevent
     * memory leaks and dangling media connections.
     */
    override fun onCleared() {
        super.onCleared()
        audioPlayer.release()
    }

    /**
     * Loads the details of a Pokémon using its unique identifier.
     *
     * Sets the UI state to [PokemonDetailUiState.Loading], fetches the data via
     * [GetPokemonDetailUseCase], and updates the state to [PokemonDetailUiState.Success]
     * or [PokemonDetailUiState.Error]. Also checks the favorite status of the loaded Pokémon.
     *
     * @param id The unique numeric identifier of the Pokémon (e.g., 1 for Bulbasaur).
     */
    fun loadPokemonDetail(id: Int) {
        viewModelScope.launch {
            _uiState.value = PokemonDetailUiState.Loading
            try {
                val detail = getPokemonDetailUseCase(id)
                _uiState.value = PokemonDetailUiState.Success(detail)
                checkFavoriteStatus(detail.id)
            } catch (e: Exception) {
                _uiState.value = PokemonDetailUiState.Error(
                    e.localizedMessage ?: "Error al cargar los detalles del Pokémon"
                )
            }
        }
    }

    /**
     * Loads the details of a Pokémon using its name.
     *
     * Sets the UI state to [PokemonDetailUiState.Loading], fetches the data via
     * [GetPokemonDetailUseCase], and updates the state to [PokemonDetailUiState.Success]
     * or [PokemonDetailUiState.Error]. Also checks the favorite status of the loaded Pokémon.
     *
     * @param name The name of the Pokémon (e.g., "pikachu"). Case-sensitive as expected by the API.
     */
    fun loadPokemonDetailByName(name: String) {
        viewModelScope.launch {
            _uiState.value = PokemonDetailUiState.Loading
            try {
                val detail = getPokemonDetailUseCase(name)
                _uiState.value = PokemonDetailUiState.Success(detail)
                checkFavoriteStatus(detail.id)
            } catch (e: Exception) {
                _uiState.value = PokemonDetailUiState.Error(
                    e.localizedMessage ?: "Error al cargar los detalles del Pokémon"
                )
            }
        }
    }

    /**
     * Checks if the Pokémon with the given [id] is in the user's favorites list
     * and updates [_isFavorite] accordingly.
     *
     * If the check fails (e.g., database error), defaults to `false`.
     *
     * @param id The unique numeric identifier of the Pokémon to check.
     */
    private fun checkFavoriteStatus(id: Int) {
        viewModelScope.launch {
            try {
                _isFavorite.value = isFavoriteUseCase(id)
            } catch (e: Exception) {
                _isFavorite.value = false
            }
        }
    }

    /**
     * Adds the specified Pokémon to the user's favorites list.
     *
     * On success, updates [isFavorite] to `true`. Errors are silently ignored.
     *
     * @param pokemon The [Pokemon] domain model to add to the favorites list.
     */
    fun addToFavorites(pokemon: Pokemon) {
        viewModelScope.launch {
            try {
                addFavoriteUseCase(pokemon)
                _isFavorite.value = true
            } catch (e: Exception) {
                // Handle silently or ignore
            }
        }
    }

    /**
     * Removes the Pokémon with the given [id] from the user's favorites list.
     *
     * On success, updates [isFavorite] to `false`. Errors are silently ignored.
     *
     * @param id The unique numeric identifier of the Pokémon to remove from favorites.
     */
    fun removeFromFavorites(id: Int) {
        viewModelScope.launch {
            try {
                removeFavoriteUseCase(id)
                _isFavorite.value = false
            } catch (e: Exception) {
                // Handle silently or ignore
            }
        }
    }
}
