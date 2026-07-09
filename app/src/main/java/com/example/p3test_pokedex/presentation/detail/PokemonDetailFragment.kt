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
 * Fragment that hosts the Pokémon detail screen.
 *
 * This fragment is responsible for:
 * - Extracting the `pokemonId` argument passed via Navigation Component.
 * - Delegating data loading to [PokemonDetailViewModel].
 * - Rendering the [PokemonDetailScreen] Composable inside a [ComposeView] wrapped
 *   with the [P3TestPokedexTheme].
 * - Providing a back-navigation callback that pops the current destination from the back stack.
 *
 * The ViewModel is injected using Koin's `viewModel()` delegate, which scopes the instance
 * to this Fragment's lifecycle.
 *
 * @see PokemonDetailViewModel
 * @see PokemonDetailScreen
 */
class PokemonDetailFragment : Fragment() {

    /**
     * The ViewModel responsible for managing the Pokémon detail UI state and favorite status.
     * Injected lazily by Koin and scoped to this Fragment.
     */
    private val viewModel: PokemonDetailViewModel by viewModel()

    /**
     * Called when the fragment is first created.
     *
     * Extracts the `pokemonId` from the fragment's arguments bundle and triggers
     * [PokemonDetailViewModel.loadPokemonDetail] if the ID is valid and the UI state
     * has not already been loaded successfully. This prevents redundant network calls
     * on configuration changes.
     *
     * @param savedInstanceState If the fragment is being re-created from a previous
     *   saved state, this is the state. May be `null`.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pokemonId = arguments?.getInt("pokemonId") ?: -1
        if (pokemonId != -1 && viewModel.uiState.value !is PokemonDetailUiState.Success) {
            viewModel.loadPokemonDetail(pokemonId)
        }
    }

    /**
     * Creates and returns the view hierarchy associated with this fragment.
     *
     * Returns a [ComposeView] that hosts the [PokemonDetailScreen] Composable, wrapped
     * inside the application's [P3TestPokedexTheme]. The `pokemonId` argument is passed
     * to the screen, and the back button callback uses the Navigation Component's
     * [findNavController] to navigate up.
     *
     * @param inflater           The [LayoutInflater] object used to inflate views.
     * @param container          The parent view that this fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a
     *   previous saved state.
     * @return The root [View] (a [ComposeView]) for the fragment's UI.
     */
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
