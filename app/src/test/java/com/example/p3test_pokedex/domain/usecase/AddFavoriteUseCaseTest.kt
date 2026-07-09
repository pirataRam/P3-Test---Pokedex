package com.example.p3test_pokedex.domain.usecase

import com.example.p3test_pokedex.domain.model.Pokemon
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit test suite for the [AddFavoriteUseCase] class.
 */
class AddFavoriteUseCaseTest {

    private val fakeRepository = FakePokemonRepository()
    private val addFavoriteUseCase = AddFavoriteUseCase(fakeRepository)

    @Test
    fun `invoke successfully adds Pokémon to favorites`() = runTest {
        // Given
        val pokemon = Pokemon(25, "pikachu", "https://example.com/25.png")

        // When
        addFavoriteUseCase(pokemon)

        // Then
        assertTrue(fakeRepository.isFavorite(25))
    }
}
