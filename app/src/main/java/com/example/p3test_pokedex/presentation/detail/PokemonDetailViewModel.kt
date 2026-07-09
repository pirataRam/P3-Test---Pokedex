package com.example.p3test_pokedex.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.p3test_pokedex.domain.model.PokemonDetail
import com.example.p3test_pokedex.domain.usecase.GetPokemonDetailUseCase
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
 * ViewModel for managing the details state of a specific Pokémon.
 *
 * @property getPokemonDetailUseCase Use case for fetching the detailed Pokémon data.
 */
class PokemonDetailViewModel(
    private val getPokemonDetailUseCase: GetPokemonDetailUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<PokemonDetailUiState>(PokemonDetailUiState.Loading)
    val uiState: StateFlow<PokemonDetailUiState> = _uiState.asStateFlow()

    /**
     * Loads the details of a Pokémon using its unique identifier.
     *
     * @param id The unique identifier of the Pokémon.
     */
    fun loadPokemonDetail(id: Int) {
        viewModelScope.launch {
            _uiState.value = PokemonDetailUiState.Loading
            try {
                val detail = getPokemonDetailUseCase(id)
                _uiState.value = PokemonDetailUiState.Success(detail)
            } catch (e: Exception) {
                _uiState.value = PokemonDetailUiState.Error(e.localizedMessage ?: "Failed to fetch Pokémon details")
            }
        }
    }

    /**
     * Loads the details of a Pokémon using its name.
     *
     * @param name The name of the Pokémon.
     */
    fun loadPokemonDetailByName(name: String) {
        viewModelScope.launch {
            _uiState.value = PokemonDetailUiState.Loading
            try {
                val detail = getPokemonDetailUseCase(name)
                _uiState.value = PokemonDetailUiState.Success(detail)
            } catch (e: Exception) {
                _uiState.value = PokemonDetailUiState.Error(e.localizedMessage ?: "Failed to fetch Pokémon details")
            }
        }
    }
}
