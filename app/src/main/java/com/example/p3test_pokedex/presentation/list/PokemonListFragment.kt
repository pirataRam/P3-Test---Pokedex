package com.example.p3test_pokedex.presentation.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.p3test_pokedex.R
import com.example.p3test_pokedex.presentation.ui.PokemonListScreen
import com.example.p3test_pokedex.theme.P3TestPokedexTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Fragment hosting the Pokémon List Screen.
 * Uses Koin to inject the ViewModel and Navigation Controller to navigate on card click.
 */
class PokemonListFragment : Fragment() {

    private val viewModel: PokemonListViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                P3TestPokedexTheme {
                    PokemonListScreen(
                        viewModel = viewModel,
                        onPokemonClick = { id ->
                            val bundle = bundleOf("pokemonId" to id)
                            findNavController().navigate(
                                R.id.action_pokemonListFragment_to_pokemonDetailFragment,
                                bundle
                            )
                        }
                    )
                }
            }
        }
    }
}
