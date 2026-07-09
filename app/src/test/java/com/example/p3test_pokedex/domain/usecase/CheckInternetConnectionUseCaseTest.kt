package com.example.p3test_pokedex.domain.usecase

import com.example.p3test_pokedex.domain.repository.NetworkMonitor
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit test suite for the [CheckInternetConnectionUseCase] class.
 */
class CheckInternetConnectionUseCaseTest {

    @Test
    fun `invoke when network is connected returns true`() {
        // Given
        val fakeMonitor = object : NetworkMonitor {
            override fun isConnected(): Boolean = true
        }
        val useCase = CheckInternetConnectionUseCase(fakeMonitor)

        // When
        val result = useCase()

        // Then
        assertTrue(result)
    }

    @Test
    fun `invoke when network is disconnected returns false`() {
        // Given
        val fakeMonitor = object : NetworkMonitor {
            override fun isConnected(): Boolean = false
        }
        val useCase = CheckInternetConnectionUseCase(fakeMonitor)

        // When
        val result = useCase()

        // Then
        assertFalse(result)
    }
}
