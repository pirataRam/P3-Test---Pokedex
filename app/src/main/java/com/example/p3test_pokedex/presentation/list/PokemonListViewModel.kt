package com.example.p3test_pokedex.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.p3test_pokedex.domain.model.Pokemon
import com.example.p3test_pokedex.domain.usecase.GetPokemonListUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Represent states of the Pokémon List Screen.
 */
sealed interface PokemonListUiState {
    data object Loading : PokemonListUiState
    data class Success(val pokemonList: List<Pokemon>) : PokemonListUiState
    data class Error(val message: String) : PokemonListUiState
    data object Empty : PokemonListUiState
}

/**
 * ViewModel for managing the Pokémon list state and actions.
 *
 * @property getPokemonListUseCase Use case for fetching the list of Pokémon.
 */
class PokemonListViewModel(
    private val getPokemonListUseCase: GetPokemonListUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<PokemonListUiState>(PokemonListUiState.Loading)
    val uiState: StateFlow<PokemonListUiState> = _uiState.asStateFlow()

    init {
        loadPokemonList()
    }

    /**
     * Loads the initial list of Pokémon (limit 20, offset 0).
     */
    fun loadPokemonList() {
        viewModelScope.launch {
            _uiState.value = PokemonListUiState.Loading
            try {
                val list = getPokemonListUseCase(limit = 20, offset = 0)
                if (list.isEmpty()) {
                    _uiState.value = PokemonListUiState.Empty
                } else {
                    _uiState.value = PokemonListUiState.Success(list)
                }
            } catch (e: Exception) {
                _uiState.value = PokemonListUiState.Error(e.localizedMessage ?: "Failed to fetch Pokémon list")
            }
        }
    }
}
