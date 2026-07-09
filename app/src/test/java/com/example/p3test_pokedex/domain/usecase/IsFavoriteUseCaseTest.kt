package com.example.p3test_pokedex.domain.usecase

import com.example.p3test_pokedex.domain.model.Pokemon
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit test suite for the [IsFavoriteUseCase] class.
 */
class IsFavoriteUseCaseTest {

    private val fakeRepository = FakePokemonRepository()
    private val isFavoriteUseCase = IsFavoriteUseCase(fakeRepository)

    @Test
    fun `invoke with non-favorite id returns false`() = runTest {
        // When
        val result = isFavoriteUseCase(25)

        // Then
        assertFalse(result)
    }

    @Test
    fun `invoke with favorite id returns true`() = runTest {
        // Given
        val id = 25
        fakeRepository.addFavorite(Pokemon(id, "pikachu", "https://example.com/25.png"))

        // When
        val result = isFavoriteUseCase(id)

        // Then
        assertTrue(result)
    }
}
