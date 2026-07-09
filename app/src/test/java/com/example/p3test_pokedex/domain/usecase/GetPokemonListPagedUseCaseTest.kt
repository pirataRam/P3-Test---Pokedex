package com.example.p3test_pokedex.domain.usecase

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
