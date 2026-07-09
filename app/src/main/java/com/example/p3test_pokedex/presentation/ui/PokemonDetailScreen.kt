package com.example.p3test_pokedex.presentation.ui

import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.p3test_pokedex.domain.model.PokemonDetail
import com.example.p3test_pokedex.presentation.detail.PokemonDetailUiState
import com.example.p3test_pokedex.presentation.detail.PokemonDetailViewModel
import java.util.Locale

/**
 * Main Pokémon detail screen container that hooks up ViewModel StateFlow to the stateless view.
 * It also manages a MediaPlayer instance for reproducing Pokémon cries and a HorizontalPager
 * for swiping left (previous Pokémon) and right (next Pokémon).
 *
 * @param viewModel The PokemonDetailViewModel.
 * @param pokemonId The unique identifier of the initial Pokémon.
 * @param onBackClick Callback triggered when user navigates back.
 * @param modifier The modifier to be applied to the layout.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonDetailScreen(
    viewModel: PokemonDetailViewModel,
    pokemonId: Int,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Standard National Pokédex limit in generation 9 is 1025.
    // We map pokemonId (1-based index) to page index (0-based).
    val pagerState = rememberPagerState(
        initialPage = (pokemonId - 1).coerceAtLeast(0),
        pageCount = { 1025 }
    )

    // Trigger loading new Pokémon details whenever the user swipes to a different page
    LaunchedEffect(pagerState.currentPage) {
        val currentId = pagerState.currentPage + 1
        viewModel.loadPokemonDetail(currentId)
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Remember a MediaPlayer instance that will be released automatically when disposed
    val mediaPlayer = remember { MediaPlayer() }

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer.release()
        }
    }

    val playCry: (String) -> Unit = { url ->
        try {
            mediaPlayer.reset()
            mediaPlayer.setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            mediaPlayer.setDataSource(url)
            mediaPlayer.setOnPreparedListener { mp ->
                mp.start()
            }
            mediaPlayer.setOnErrorListener { mp, _, _ ->
                mp.reset()
                true
            }
            mediaPlayer.prepareAsync()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Details",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) { paddingValues ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) { page ->
            val targetId = page + 1

            when (val state = uiState) {
                is PokemonDetailUiState.Success -> {
                    // Only show the details if they match the currently swiped page targetId
                    if (state.pokemonDetail.id == targetId) {
                        PokemonDetailContent(
                            pokemon = state.pokemonDetail,
                            onPlayCry = playCry,
                            onRetry = { viewModel.loadPokemonDetail(targetId) }
                        )
                    } else {
                        PokemonDetailShimmer()
                    }
                }
                is PokemonDetailUiState.Loading -> {
                    PokemonDetailShimmer()
                }
                is PokemonDetailUiState.Error -> {
                    ErrorStateView(
                        message = state.message,
                        onRetry = { viewModel.loadPokemonDetail(targetId) }
                    )
                }
            }
        }
    }
}

/**
 * Shimmer placeholder content displayed during loading/transition states.
 */
@Composable
fun PokemonDetailShimmer() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .aspectRatio(1.0f)
                .clip(RoundedCornerShape(16.dp))
        ) {
            ShimmerLoader(modifier = Modifier.fillMaxSize())
        }
        Spacer(modifier = Modifier.height(24.dp))
        Box(
            modifier = Modifier
                .width(150.dp)
                .height(28.dp)
                .clip(RoundedCornerShape(8.dp))
        ) {
            ShimmerLoader(modifier = Modifier.fillMaxSize())
        }
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(16.dp))
        ) {
            ShimmerLoader(modifier = Modifier.fillMaxSize())
        }
    }
}

/**
 * Stateless detailed content view of a successfully loaded Pokémon.
 *
 * @param pokemon The loaded PokemonDetail domain object.
 * @param onPlayCry Callback trigger to play OGG sound from URL.
 * @param onRetry Callback when re-fetching is requested.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PokemonDetailContent(
    pokemon: PokemonDetail,
    onPlayCry: (String) -> Unit,
    onRetry: () -> Unit
) {
    val latestCryUrl = "https://raw.githubusercontent.com/PokeAPI/cries/main/cries/pokemon/latest/${pokemon.id}.ogg"
    val legacyCryUrl = "https://raw.githubusercontent.com/PokeAPI/cries/main/cries/pokemon/legacy/${pokemon.id}.ogg"

    // Auto-play the modern/latest Pokémon cry when the Pokémon details screen is successfully loaded/swiped
    LaunchedEffect(pokemon.id) {
        onPlayCry(latestCryUrl)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .aspectRatio(1.0f)
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(24.dp)
                )
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = pokemon.imageUrl,
                contentDescription = pokemon.name,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = String.format(Locale.getDefault(), "#%03d", pokemon.id),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = pokemon.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Types Row
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.weight(1f))
            pokemon.types.forEach { type ->
                TypeTag(type = type)
            }
            Spacer(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Physical Properties Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${pokemon.weight / 10.0} kg",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Weight",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${pokemon.height / 10.0} m",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Height",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${pokemon.baseExperience} XP",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Base XP",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Stats Title
        Text(
            text = "Base Stats",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Stats bars
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            pokemon.stats.forEach { stat ->
                StatBar(name = stat.name, value = stat.value)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Abilities Title
        Text(
            text = "Abilities",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Abilities FlowRow
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            pokemon.abilities.forEach { ability ->
                AbilityTag(ability = ability)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Pokémon Cries Section
        Text(
            text = "Gritos de Pokémon",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = { onPlayCry(latestCryUrl) },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Reproducir Grito Moderno"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Moderno",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            Button(
                onClick = { onPlayCry(legacyCryUrl) },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Reproducir Grito Retro"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Retro (Legacy)",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
