package com.example.p3test_pokedex.domain.usecase

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

/**
 * Unit test suite for the [GetPokemonDetailUseCase] class.
 */
class GetPokemonDetailUseCaseTest {

    private val fakeRepository = FakePokemonRepository()
    private val getPokemonDetailUseCase = GetPokemonDetailUseCase(fakeRepository)

    @Test
    fun `invoke with id returns correct Pokémon details`() = runTest {
        // Given
        val targetId = 25

        // When
        val result = getPokemonDetailUseCase(targetId)

        // Then
        assertNotNull(result)
        assertEquals(targetId, result.id)
        assertEquals("pokemon-25", result.name)
    }

    @Test
    fun `invoke with name returns correct Pokémon details`() = runTest {
        // Given
        val targetName = "pikachu"

        // When
        val result = getPokemonDetailUseCase(targetName)

        // Then
        assertNotNull(result)
        assertEquals(targetName, result.name)
    }
}
