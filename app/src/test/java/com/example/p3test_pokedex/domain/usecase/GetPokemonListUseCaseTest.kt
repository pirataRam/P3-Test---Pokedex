package com.example.p3test_pokedex.domain.usecase

import com.example.p3test_pokedex.domain.model.Pokemon
import com.example.p3test_pokedex.domain.model.PokemonDetail
import com.example.p3test_pokedex.domain.repository.PokemonRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Unit test suite for the [GetPokemonListUseCase] class.
 */
class GetPokemonListUseCaseTest {

    private val fakeRepository = FakePokemonRepository()
    private val getPokemonListUseCase = GetPokemonListUseCase(fakeRepository)

    @Test
    fun `invoke with limit and offset returns list of Pokémon from repository`() = runTest {
        // Given
        val limit = 20
        val offset = 0

        // When
        val result = getPokemonListUseCase(limit, offset)

        // Then
        assertEquals(3, result.size)
        assertEquals("bulbasaur", result[0].name)
        assertEquals("ivysaur", result[1].name)
        assertEquals("venusaur", result[2].name)
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
