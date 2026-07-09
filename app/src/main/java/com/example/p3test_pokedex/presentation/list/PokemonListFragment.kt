package com.example.p3test_pokedex.presentation.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.p3test_pokedex.R
import com.example.p3test_pokedex.presentation.theme.P3TestPokedexTheme
import com.example.p3test_pokedex.presentation.ui.FavoritesScreen
import com.example.p3test_pokedex.presentation.ui.PokemonListScreen
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Enums representing the active bottom bar tabs.
 */
enum class MainTab {
    Pokedex,
    Favorites
}

/**
 * Fragment hosting the Pokémon List Screen and Favorites Screen inside a Bottom Navigation.
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
                    var currentTab by remember { mutableStateOf(MainTab.Pokedex) }
                    val favorites by viewModel.favoritesList.collectAsState()

                    LaunchedEffect(currentTab) {
                        if (currentTab == MainTab.Favorites) {
                            viewModel.loadFavorites()
                        }
                    }

                    Scaffold(
                        bottomBar = {
                            NavigationBar {
                                NavigationBarItem(
                                    selected = currentTab == MainTab.Pokedex,
                                    onClick = { currentTab = MainTab.Pokedex },
                                    icon = { Icon(Icons.Default.List, contentDescription = "Pokedex") },
                                    label = { Text("Pokédex") }
                                )
                                NavigationBarItem(
                                    selected = currentTab == MainTab.Favorites,
                                    onClick = { currentTab = MainTab.Favorites },
                                    icon = { Icon(Icons.Default.Star, contentDescription = "Favoritos") },
                                    label = { Text("Favoritos") }
                                )
                            }
                        }
                    ) { innerPadding ->
                        Box(modifier = Modifier.padding(innerPadding)) {
                            when (currentTab) {
                                MainTab.Pokedex -> {
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
                                MainTab.Favorites -> {
                                    FavoritesScreen(
                                        favorites = favorites,
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
            }
        }
    }
}
