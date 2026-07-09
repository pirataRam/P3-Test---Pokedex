package com.example.p3test_pokedex.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.p3test_pokedex.domain.model.Pokemon
import com.example.p3test_pokedex.presentation.list.PokemonListViewModel
import com.example.p3test_pokedex.presentation.list.SearchResultState

/**
 * Main Composable that observes the PokemonListViewModel and renders states accordingly using Paging3.
 * Includes a search bar and handles both paging list and search result states.
 */
@Composable
fun PokemonListScreen(
    viewModel: PokemonListViewModel,
    onPokemonClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val lazyPokemonItems = viewModel.pokemonPagingDataFlow.collectAsLazyPagingItems()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchResultState by viewModel.searchResultState.collectAsState()

    PokemonListScreen(
        lazyPokemonItems = lazyPokemonItems,
        searchQuery = searchQuery,
        searchResultState = searchResultState,
        onSearchQueryChanged = { viewModel.onSearchQueryChanged(it) },
        onPokemonClick = onPokemonClick,
        modifier = modifier
    )
}

/**
 * Stateless implementation of the Pokémon list screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonListScreen(
    lazyPokemonItems: LazyPagingItems<Pokemon>,
    searchQuery: String,
    searchResultState: SearchResultState,
    onSearchQueryChanged: (String) -> Unit,
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
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Search Bar (fixed at top)
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChanged,
                placeholder = { Text("Buscar por nombre o número...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchQueryChanged("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Limpiar")
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            // Content Area
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                if (searchResultState !is SearchResultState.Idle) {
                    // Show Search Results
                    when (searchResultState) {
                        is SearchResultState.Loading -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                        is SearchResultState.Success -> {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                contentPadding = PaddingValues(16.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                modifier = Modifier.fillMaxSize()
                            ) {
                                item {
                                    PokemonCard(
                                        pokemon = searchResultState.pokemon,
                                        onClick = { onPokemonClick(searchResultState.pokemon.id) }
                                    )
                                }
                            }
                        }
                        is SearchResultState.Error -> {
                            ErrorStateView(
                                message = searchResultState.message,
                                onRetry = { onSearchQueryChanged(searchQuery) }
                            )
                        }
                        else -> {}
                    }
                } else {
                    // Show Infinite Scroll Paging List
                    val refreshState = lazyPokemonItems.loadState.refresh

                    when {
                        refreshState is LoadState.Loading -> {
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
                                message = error.localizedMessage ?: "Error de red al cargar el listado",
                                onRetry = { lazyPokemonItems.retry() }
                            )
                        }
                        refreshState is LoadState.NotLoading && lazyPokemonItems.itemCount == 0 -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No se encontraron Pokémon.",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        else -> {
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
                                                Text("Reintentar")
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
    }
}
