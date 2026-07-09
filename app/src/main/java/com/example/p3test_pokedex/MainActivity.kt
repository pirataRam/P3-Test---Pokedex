package com.example.p3test_pokedex

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.DialogProperties
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.p3test_pokedex.presentation.detail.PokemonDetailViewModel
import com.example.p3test_pokedex.presentation.list.MainTab
import com.example.p3test_pokedex.presentation.list.PokemonListViewModel
import com.example.p3test_pokedex.presentation.theme.P3TestPokedexTheme
import com.example.p3test_pokedex.presentation.ui.FavoritesScreen
import com.example.p3test_pokedex.presentation.ui.PokemonDetailScreen
import com.example.p3test_pokedex.presentation.ui.PokemonListScreen
import org.koin.androidx.compose.koinViewModel

/**
 * Main Activity of the Pokédex application hosting Jetpack Compose-only Navigation.
 * Installs the official Android SplashScreen before super.onCreate to display a themed Pokeball screen.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            P3TestPokedexTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "pokemon_list"
                ) {
                    composable("pokemon_list") {
                        val viewModel: PokemonListViewModel = koinViewModel()
                        var currentTab by rememberSaveable { mutableStateOf(MainTab.Pokedex) }
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
                                navController.navigate("pokemon_detail/$id")
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
                                title = { Text(text = stringResource(R.string.no_internet_title)) },
                                text = { Text(text = stringResource(R.string.no_internet_msg)) },
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
                                        Text(stringResource(R.string.go_to_settings))
                                    }
                                },
                                dismissButton = {
                                    TextButton(
                                        onClick = { viewModel.onDismissNoInternetDialog() }
                                    ) {
                                        Text(stringResource(R.string.accept))
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
                                        icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = stringResource(R.string.pokedex_tab)) },
                                        label = { Text(stringResource(R.string.pokedex_tab)) }
                                    )
                                    NavigationBarItem(
                                        selected = currentTab == MainTab.Favorites,
                                        onClick = { currentTab = MainTab.Favorites },
                                        icon = { Icon(Icons.Default.Star, contentDescription = stringResource(R.string.favorites_tab)) },
                                        label = { Text(stringResource(R.string.favorites_tab)) }
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

                    composable(
                        route = "pokemon_detail/{pokemonId}",
                        arguments = listOf(navArgument("pokemonId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val pokemonId = backStackEntry.arguments?.getInt("pokemonId") ?: -1
                        val viewModel: PokemonDetailViewModel = koinViewModel()

                        PokemonDetailScreen(
                            viewModel = viewModel,
                            pokemonId = pokemonId,
                            onBackClick = { navController.navigateUp() }
                        )
                    }
                }
            }
        }
    }
}
