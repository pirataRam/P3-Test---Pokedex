package com.example.p3test_pokedex.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.p3test_pokedex.domain.model.Pokemon
import com.example.p3test_pokedex.presentation.list.PokemonListViewModel

/**
 * Main Composable that observes the PokemonListViewModel and renders states accordingly using Paging3.
 *
 * @param viewModel The PokemonListViewModel state provider.
 * @param onPokemonClick Callback when a Pokémon card is clicked, returning its ID.
 * @param modifier The modifier to be applied to the layout.
 */
@Composable
fun PokemonListScreen(
    viewModel: PokemonListViewModel,
    onPokemonClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val lazyPokemonItems = viewModel.pokemonPagingDataFlow.collectAsLazyPagingItems()
    PokemonListScreen(
        lazyPokemonItems = lazyPokemonItems,
        onPokemonClick = onPokemonClick,
        modifier = modifier
    )
}

/**
 * Stateless implementation of the Pokémon list screen with infinite scroll / Paging3 support.
 *
 * @param lazyPokemonItems The lazy paging items of Pokémon.
 * @param onPokemonClick Callback when a Pokémon card is clicked, returning its ID.
 * @param modifier The modifier to be applied to the layout.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonListScreen(
    lazyPokemonItems: LazyPagingItems<Pokemon>,
    onPokemonClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Pokédex",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            val refreshState = lazyPokemonItems.loadState.refresh

            when {
                refreshState is LoadState.Loading -> {
                    // Initial loader shimmer effect
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(8) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(1.0f)
                                    .clip(RoundedCornerShape(16.dp))
                            ) {
                                ShimmerLoader(modifier = Modifier.fillMaxSize())
                            }
                        }
                    }
                }
                refreshState is LoadState.Error -> {
                    val error = refreshState.error
                    ErrorStateView(
                        message = error.localizedMessage ?: "Failed to fetch Pokémon list",
                        onRetry = { lazyPokemonItems.retry() }
                    )
                }
                refreshState is LoadState.NotLoading && lazyPokemonItems.itemCount == 0 -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No Pokémon found.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                else -> {
                    // Success list with appending infinite scroll loader
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(
                            count = lazyPokemonItems.itemCount,
                            key = { index ->
                                val pokemon = lazyPokemonItems[index]
                                pokemon?.id ?: index
                            }
                        ) { index ->
                            val pokemon = lazyPokemonItems[index]
                            if (pokemon != null) {
                                PokemonCard(
                                    pokemon = pokemon,
                                    onClick = { onPokemonClick(pokemon.id) }
                                )
                            }
                        }

                        // Bottom progress/error indicators for infinite scroll
                        val appendState = lazyPokemonItems.loadState.append
                        if (appendState is LoadState.Loading) {
                            item(span = { GridItemSpan(2) }) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        } else if (appendState is LoadState.Error) {
                            item(span = { GridItemSpan(2) }) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Button(onClick = { lazyPokemonItems.retry() }) {
                                        Text("Retry")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
