package com.example.p3test_pokedex.presentation.list

import androidx.paging.PagingSource
import com.example.p3test_pokedex.data.paging.PokemonPagingSource
import com.example.p3test_pokedex.domain.model.Pokemon
import com.example.p3test_pokedex.domain.model.PokemonDetail
import com.example.p3test_pokedex.domain.repository.PokemonRepository
import com.example.p3test_pokedex.domain.usecase.GetPokemonListPagedUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
        viewModel = PokemonListViewModel(getPokemonListPagedUseCase)
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
        assertEquals(null, page.nextKey) // Since response size (3) is less than loadSize (20)
    }

    @Test
    fun `PokemonPagingSource load returns Error on failure`() = runTest(testDispatcher) {
        val failingRepository = object : PokemonRepository {
            override suspend fun getPokemonList(limit: Int, offset: Int): List<Pokemon> {
                throw RuntimeException("Network Error")
            }
            override suspend fun getPokemonDetail(id: Int): PokemonDetail = throw NotImplementedError()
            override suspend fun getPokemonDetailByName(name: String): PokemonDetail = throw NotImplementedError()
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
