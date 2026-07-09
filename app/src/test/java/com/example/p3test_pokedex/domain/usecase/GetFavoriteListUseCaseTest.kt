package com.example.p3test_pokedex.domain.usecase

import com.example.p3test_pokedex.domain.model.Pokemon
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit test suite for the [GetFavoriteListUseCase] class.
 */
class GetFavoriteListUseCaseTest {

    private val fakeRepository = FakePokemonRepository()
    private val getFavoriteListUseCase = GetFavoriteListUseCase(fakeRepository)

    @Test
    fun `invoke initially returns empty list`() = runTest {
        // When
        val result = getFavoriteListUseCase()

        // Then
        assertTrue(result.isEmpty())
    }

    @Test
    fun `invoke after adding favorite returns populated list`() = runTest {
        // Given
        val pokemon = Pokemon(25, "pikachu", "https://example.com/25.png")
        fakeRepository.addFavorite(pokemon)

        // When
        val result = getFavoriteListUseCase()

        // Then
        assertEquals(1, result.size)
        assertEquals(pokemon, result.first())
    }
}
