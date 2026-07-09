package com.example.p3test_pokedex.presentation.list

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.DialogProperties
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.p3test_pokedex.R
import com.example.p3test_pokedex.presentation.theme.P3TestPokedexTheme
import com.example.p3test_pokedex.presentation.ui.FavoritesScreen
import com.example.p3test_pokedex.presentation.ui.PokemonListScreen
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Enumeration representing the available bottom navigation tabs in the main screen.
 *
 * @see PokemonListFragment
 */
enum class MainTab {
    /** The primary Pokédex tab showing the paginated list of all Pokémon. */
    Pokedex,

    /** The favorites tab showing only the user's bookmarked Pokémon. */
    Favorites
}

/**
 * Fragment that hosts the main application screen with a bottom navigation bar.
 *
 * This fragment is responsible for:
 * - Rendering a [Scaffold] with a [NavigationBar] containing two tabs: Pokédex and Favorites.
 * - Displaying the [PokemonListScreen] when the [MainTab.Pokedex] tab is selected.
 * - Displaying the [FavoritesScreen] when the [MainTab.Favorites] tab is selected.
 * - Collecting single-shot navigation events from [PokemonListViewModel.navigateToDetail]
 *   and navigating to the detail fragment via the Navigation Component.
 * - Showing a non-cancelable "No Internet Connection" [AlertDialog] when the ViewModel
 *   signals that the device lacks network connectivity, with options to open Wi-Fi settings
 *   or dismiss the dialog.
 *
 * The ViewModel is injected via Koin's `viewModel()` delegate and is scoped to this Fragment.
 * All business logic (network checks, search, pagination) is delegated to [PokemonListViewModel],
 * keeping this UI layer strictly visual and state-bound.
 *
 * @see PokemonListViewModel
 * @see PokemonListScreen
 * @see FavoritesScreen
 */
class PokemonListFragment : Fragment() {

    /**
     * The ViewModel responsible for managing the Pokémon list, search, favorites,
     * and network connectivity state. Injected lazily by Koin.
     */
    private val viewModel: PokemonListViewModel by viewModel()

    /**
     * Creates and returns the view hierarchy associated with this fragment.
     *
     * Returns a [ComposeView] that hosts the entire main screen UI wrapped inside
     * the application's [P3TestPokedexTheme]. The Compose content includes:
     * - A bottom [NavigationBar] with Pokédex and Favorites tabs.
     * - A [LaunchedEffect] that reloads favorites when switching to the Favorites tab.
     * - A [LaunchedEffect] that collects navigation events and navigates to the detail fragment.
     * - A non-cancelable [AlertDialog] for the no-internet-connection state.
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
        return ComposeView(requireContext()).apply {
            setContent {
                P3TestPokedexTheme {
                    var currentTab by remember { mutableStateOf(MainTab.Pokedex) }
                    val favorites by viewModel.favoritesList.collectAsState()
                    val showNoInternetDialog by viewModel.showNoInternetDialog.collectAsState()
                    val context = LocalContext.current

                    LaunchedEffect(currentTab) {
                        if (currentTab == MainTab.Favorites) {
                            viewModel.loadFavorites()
                        }
                    }

                    // Collect single-time navigation actions emitted by ViewModel
                    LaunchedEffect(Unit) {
                        viewModel.navigateToDetail.collect { id ->
                            val bundle = bundleOf("pokemonId" to id)
                            findNavController().navigate(
                                R.id.action_pokemonListFragment_to_pokemonDetailFragment,
                                bundle
                            )
                        }
                    }

                    // Non-cancelable No Internet Connection Dialog
                    if (showNoInternetDialog) {
                        AlertDialog(
                            onDismissRequest = { /* Non-cancelable */ },
                            properties = DialogProperties(
                                dismissOnBackPress = false,
                                dismissOnClickOutside = false
                            ),
                            title = { Text(text = "Sin conexión a internet") },
                            text = { Text(text = "No estás conectado. Conéctate a internet para reintentarlo.") },
                            confirmButton = {
                                TextButton(
                                    onClick = {
                                        try {
                                            val intent = Intent(Settings.ACTION_WIFI_SETTINGS).apply {
                                                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                            }
                                            context.startActivity(intent)
                                        } catch (e: Exception) {
                                            val intent = Intent(Settings.ACTION_SETTINGS).apply {
                                                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                            }
                                            context.startActivity(intent)
                                        }
                                        viewModel.onDismissNoInternetDialog()
                                    }
                                ) {
                                    Text("Ir a Ajustes")
                                }
                            },
                            dismissButton = {
                                TextButton(
                                    onClick = { viewModel.onDismissNoInternetDialog() }
                                ) {
                                    Text("Aceptar")
                                }
                            }
                        )
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
                                            viewModel.onPokemonClicked(id, isFavoriteTab = false)
                                        }
                                    )
                                }
                                MainTab.Favorites -> {
                                    FavoritesScreen(
                                        favorites = favorites,
                                        onPokemonClick = { id ->
                                            viewModel.onPokemonClicked(id, isFavoriteTab = true)
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
