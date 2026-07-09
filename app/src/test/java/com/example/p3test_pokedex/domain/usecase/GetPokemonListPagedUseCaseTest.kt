package com.example.p3test_pokedex.domain.usecase

import com.example.p3test_pokedex.domain.model.Pokemon
import com.example.p3test_pokedex.domain.model.PokemonDetail
import com.example.p3test_pokedex.domain.repository.PokemonRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotNull
import org.junit.Test

/**
 * Unit test suite for the [GetPokemonListPagedUseCase] class.
 */
class GetPokemonListPagedUseCaseTest {

    private val fakeRepository = FakePokemonRepository()
    private val getPokemonListPagedUseCase = GetPokemonListPagedUseCase(fakeRepository)

    @Test
    fun `invoke returns a non-null Flow of PagingData`() = runTest {
        // When
        val flow = getPokemonListPagedUseCase()

        // Then
        assertNotNull(flow)

        // Retrieve the first emission from the flow to confirm it's instantiated correctly
        val pagingData = flow.first()
        assertNotNull(pagingData)
    }
}

/**
 * Fake implementation of the [PokemonRepository] for testing purposes.
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
