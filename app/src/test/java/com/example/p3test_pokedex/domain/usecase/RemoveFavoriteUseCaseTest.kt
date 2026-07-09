package com.example.p3test_pokedex.domain.usecase

import com.example.p3test_pokedex.domain.model.Pokemon
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Test

/**
 * Unit test suite for the [RemoveFavoriteUseCase] class.
 */
class RemoveFavoriteUseCaseTest {

    private val fakeRepository = FakePokemonRepository()
    private val removeFavoriteUseCase = RemoveFavoriteUseCase(fakeRepository)

    @Test
    fun `invoke successfully removes Pokémon from favorites`() = runTest {
        // Given
        val id = 25
        fakeRepository.addFavorite(Pokemon(id, "pikachu", "https://example.com/25.png"))

        // When
        removeFavoriteUseCase(id)

        // Then
        assertFalse(fakeRepository.isFavorite(id))
    }
}
