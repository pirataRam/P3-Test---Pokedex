package com.example.p3test_pokedex.presentation.list

import androidx.paging.PagingSource
import com.example.p3test_pokedex.data.paging.PokemonPagingSource
import com.example.p3test_pokedex.domain.model.Pokemon
import com.example.p3test_pokedex.domain.model.PokemonDetail
import com.example.p3test_pokedex.domain.repository.PokemonRepository
import com.example.p3test_pokedex.domain.usecase.FakePokemonRepository
import com.example.p3test_pokedex.domain.usecase.GetFavoriteListUseCase
import com.example.p3test_pokedex.domain.usecase.GetPokemonDetailUseCase
import com.example.p3test_pokedex.domain.usecase.GetPokemonListPagedUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Unit test suite for the [PokemonListViewModel] class and Paging components.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class PokemonListViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val fakeRepository = FakePokemonRepository()
    private val getPokemonListPagedUseCase = GetPokemonListPagedUseCase(fakeRepository)
    private val getPokemonDetailUseCase = GetPokemonDetailUseCase(fakeRepository)
    private val getFavoriteListUseCase = GetFavoriteListUseCase(fakeRepository)
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
    fun `viewModel exposes paging flow successfully`() = runTest(testDispatcher) {
        viewModel = PokemonListViewModel(
            getPokemonListPagedUseCase,
            getPokemonDetailUseCase,
            getFavoriteListUseCase
        )
        val flow = viewModel.pokemonPagingDataFlow
        assertNotNull(flow)
    }

    @Test
    fun `PokemonPagingSource load returns Page when successful`() = runTest(testDispatcher) {
        val pagingSource = PokemonPagingSource(fakeRepository)

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        assertTrue(result is PagingSource.LoadResult.Page)
        val page = result as PagingSource.LoadResult.Page
        assertEquals(3, page.data.size)
        assertEquals("bulbasaur", page.data[0].name)
        assertEquals(null, page.prevKey)
        assertEquals(null, page.nextKey)
    }

    @Test
    fun `PokemonPagingSource load returns Error on failure`() = runTest(testDispatcher) {
        val failingRepository = object : PokemonRepository {
            override suspend fun getPokemonList(limit: Int, offset: Int): List<Pokemon> {
                throw RuntimeException("Network Error")
            }
            override suspend fun getPokemonDetail(id: Int): PokemonDetail = throw NotImplementedError()
            override suspend fun getPokemonDetailByName(name: String): PokemonDetail = throw NotImplementedError()
            override suspend fun getFavorites(): List<Pokemon> = throw NotImplementedError()
            override suspend fun addFavorite(pokemon: Pokemon) = throw NotImplementedError()
            override suspend fun removeFavorite(id: Int) = throw NotImplementedError()
            override suspend fun isFavorite(id: Int): Boolean = throw NotImplementedError()
        }
        val pagingSource = PokemonPagingSource(failingRepository)

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        assertTrue(result is PagingSource.LoadResult.Error)
        val errorResult = result as PagingSource.LoadResult.Error
        assertEquals("Network Error", errorResult.throwable.message)
    }

    @Test
    fun `viewModel searches successfully and exposes Success state`() = runTest(testDispatcher) {
        viewModel = PokemonListViewModel(
            getPokemonListPagedUseCase,
            getPokemonDetailUseCase,
            getFavoriteListUseCase
        )

        // When
        viewModel.onSearchQueryChanged("pikachu")
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertTrue(viewModel.searchResultState.value is SearchResultState.Success)
        val successState = viewModel.searchResultState.value as SearchResultState.Success
        assertEquals("pikachu", successState.pokemon.name)
    }

    @Test
    fun `viewModel loads favorites list successfully`() = runTest(testDispatcher) {
        viewModel = PokemonListViewModel(
            getPokemonListPagedUseCase,
            getPokemonDetailUseCase,
            getFavoriteListUseCase
        )

        // Given
        val fav = Pokemon(25, "pikachu", "https://example.com/25.png")
        fakeRepository.addFavorite(fav)

        // When
        viewModel.loadFavorites()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals(1, viewModel.favoritesList.value.size)
        assertEquals(fav, viewModel.favoritesList.value.first())
    }
}
