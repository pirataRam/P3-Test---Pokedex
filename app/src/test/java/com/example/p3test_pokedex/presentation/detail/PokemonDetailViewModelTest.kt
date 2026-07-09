package com.example.p3test_pokedex.presentation.detail

import com.example.p3test_pokedex.domain.model.Pokemon
import com.example.p3test_pokedex.domain.repository.AudioPlayer
import com.example.p3test_pokedex.domain.usecase.AddFavoriteUseCase
import com.example.p3test_pokedex.domain.usecase.FakePokemonRepository
import com.example.p3test_pokedex.domain.usecase.GetPokemonDetailUseCase
import com.example.p3test_pokedex.domain.usecase.IsFavoriteUseCase
import com.example.p3test_pokedex.domain.usecase.RemoveFavoriteUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Unit test suite for the [PokemonDetailViewModel] class.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class PokemonDetailViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val fakeRepository = FakePokemonRepository()
    private val getPokemonDetailUseCase = GetPokemonDetailUseCase(fakeRepository)
    private val isFavoriteUseCase = IsFavoriteUseCase(fakeRepository)
    private val addFavoriteUseCase = AddFavoriteUseCase(fakeRepository)
    private val removeFavoriteUseCase = RemoveFavoriteUseCase(fakeRepository)

    private var playCalledWithUrl: String? = null
    private var releaseCalled = false
    private val fakeAudioPlayer = object : AudioPlayer {
        override fun play(url: String) {
            playCalledWithUrl = url
        }
        override fun release() {
            releaseCalled = true
        }
    }

    private lateinit var viewModel: PokemonDetailViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        playCalledWithUrl = null
        releaseCalled = false
        viewModel = PokemonDetailViewModel(
            getPokemonDetailUseCase,
            isFavoriteUseCase,
            addFavoriteUseCase,
            removeFavoriteUseCase,
            fakeAudioPlayer
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadPokemonDetail sets Success state and checks favorites`() = runTest(testDispatcher) {
        // When
        viewModel.loadPokemonDetail(1)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertTrue(viewModel.uiState.value is PokemonDetailUiState.Success)
        val success = viewModel.uiState.value as PokemonDetailUiState.Success
        assertEquals("pokemon-1", success.pokemonDetail.name)
        assertFalse(viewModel.isFavorite.value)
    }

    @Test
    fun `addToFavorites updates favorite state`() = runTest(testDispatcher) {
        val pokemon = Pokemon(1, "bulbasaur", "https://example.com/1.png")

        // When
        viewModel.addToFavorites(pokemon)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertTrue(viewModel.isFavorite.value)
    }

    @Test
    fun `removeFromFavorites updates favorite state`() = runTest(testDispatcher) {
        val pokemon = Pokemon(1, "bulbasaur", "https://example.com/1.png")
        fakeRepository.addFavorite(pokemon)

        // When
        viewModel.removeFromFavorites(1)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertFalse(viewModel.isFavorite.value)
    }

    @Test
    fun `playPokemonCry delegates cry url to AudioPlayer`() = runTest(testDispatcher) {
        // When
        viewModel.playPokemonCry("https://example.com/cry.ogg")

        // Then
        assertEquals("https://example.com/cry.ogg", playCalledWithUrl)
    }
}
