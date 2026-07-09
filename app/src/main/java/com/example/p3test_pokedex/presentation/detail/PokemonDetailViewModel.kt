package com.example.p3test_pokedex.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.p3test_pokedex.domain.model.Pokemon
import com.example.p3test_pokedex.domain.model.PokemonDetail
import com.example.p3test_pokedex.domain.usecase.AddFavoriteUseCase
import com.example.p3test_pokedex.domain.usecase.GetPokemonDetailUseCase
import com.example.p3test_pokedex.domain.usecase.IsFavoriteUseCase
import com.example.p3test_pokedex.domain.usecase.RemoveFavoriteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Represent states of the Pokémon Detail Screen.
 */
sealed interface PokemonDetailUiState {
    data object Loading : PokemonDetailUiState
    data class Success(val pokemonDetail: PokemonDetail) : PokemonDetailUiState
    data class Error(val message: String) : PokemonDetailUiState
}

/**
 * ViewModel for managing the details state and favorite status of a specific Pokémon.
 */
class PokemonDetailViewModel(
    private val getPokemonDetailUseCase: GetPokemonDetailUseCase,
    private val isFavoriteUseCase: IsFavoriteUseCase,
    private val addFavoriteUseCase: AddFavoriteUseCase,
    private val removeFavoriteUseCase: RemoveFavoriteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<PokemonDetailUiState>(PokemonDetailUiState.Loading)
    val uiState: StateFlow<PokemonDetailUiState> = _uiState.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()

    /**
     * Loads the details of a Pokémon using its unique identifier.
     * Also checks its favorite status.
     *
     * @param id The unique identifier of the Pokémon.
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
     * Also checks its favorite status.
     *
     * @param name The name of the Pokémon.
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
     * Checks if the Pokémon is a favorite and updates [_isFavorite].
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
     * Adds the Pokémon to the favorites list.
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
     * Removes the Pokémon from the favorites list.
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
