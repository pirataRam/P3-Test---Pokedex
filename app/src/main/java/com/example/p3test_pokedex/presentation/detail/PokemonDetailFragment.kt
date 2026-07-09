package com.example.p3test_pokedex.presentation.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.p3test_pokedex.presentation.ui.PokemonDetailScreen
import com.example.p3test_pokedex.presentation.theme.P3TestPokedexTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Fragment hosting the Pokémon Details Screen.
 * Extracts the pokemonId from arguments and requests the ViewModel to load details.
 */
class PokemonDetailFragment : Fragment() {

    private val viewModel: PokemonDetailViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pokemonId = arguments?.getInt("pokemonId") ?: -1
        if (pokemonId != -1 && viewModel.uiState.value !is PokemonDetailUiState.Success) {
            viewModel.loadPokemonDetail(pokemonId)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val pokemonId = arguments?.getInt("pokemonId") ?: -1
        return ComposeView(requireContext()).apply {
            setContent {
                P3TestPokedexTheme {
                    PokemonDetailScreen(
                        viewModel = viewModel,
                        pokemonId = pokemonId,
                        onBackClick = { findNavController().navigateUp() }
                    )
                }
            }
        }
    }
}
