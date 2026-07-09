package com.example.p3test_pokedex.presentation.list

import com.example.p3test_pokedex.domain.model.Pokemon
import com.example.p3test_pokedex.domain.model.PokemonDetail
import com.example.p3test_pokedex.domain.repository.PokemonRepository
import com.example.p3test_pokedex.domain.usecase.GetPokemonListUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Unit test suite for the [PokemonListViewModel] class.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class PokemonListViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val fakeRepository = FakePokemonRepository()
    private val getPokemonListUseCase = GetPokemonListUseCase(fakeRepository)
    private lateinit var viewModel: PokemonListViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadPokemonList successfully updates uiState to Success`() = runTest(testDispatcher) {
        // Given
        viewModel = PokemonListViewModel(getPokemonListUseCase)

        // When
        advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertTrue(state is PokemonListUiState.Success)
        assertEquals(3, (state as PokemonListUiState.Success).pokemonList.size)
    }

    @Test
    fun `loadPokemonList with empty repository updates uiState to Empty`() = runTest(testDispatcher) {
        // Given
        val emptyUseCase = GetPokemonListUseCase(object : PokemonRepository {
            override suspend fun getPokemonList(limit: Int, offset: Int): List<Pokemon> = emptyList()
            override suspend fun getPokemonDetail(id: Int): PokemonDetail = throw NotImplementedError()
            override suspend fun getPokemonDetailByName(name: String): PokemonDetail = throw NotImplementedError()
        })
        viewModel = PokemonListViewModel(emptyUseCase)

        // When
        advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertEquals(PokemonListUiState.Empty, state)
    }
}

/**
 * Fake repository implementation for testing.
 */
private class FakePokemonRepository : PokemonRepository {
    private val fakeList = listOf(
        Pokemon(1, "bulbasaur", "https://example.com/1.png"),
        Pokemon(2, "ivysaur", "https://example.com/2.png"),
        Pokemon(3, "venusaur", "https://example.com/3.png")
    )

    override suspend fun getPokemonList(limit: Int, offset: Int): List<Pokemon> {
        return fakeList
    }

    override suspend fun getPokemonDetail(id: Int): PokemonDetail {
        throw NotImplementedError()
    }

    override suspend fun getPokemonDetailByName(name: String): PokemonDetail {
        throw NotImplementedError()
    }
}
