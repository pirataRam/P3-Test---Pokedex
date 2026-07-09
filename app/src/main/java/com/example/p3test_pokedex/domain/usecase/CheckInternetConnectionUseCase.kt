package com.example.p3test_pokedex.domain.usecase

import com.example.p3test_pokedex.domain.repository.NetworkMonitor

/**
 * Use case that encapsulates the business rule of verifying whether the
 * device currently has access to the internet.
 *
 * **Business rule:** Before performing any network-dependent operation
 * (e.g., fetching the Pokémon list or detail), the application should
 * verify connectivity so it can display an appropriate offline message
 * rather than allowing a silent failure.
 *
 * This class delegates the actual connectivity check to [NetworkMonitor]
 * and exposes the operation as a callable `operator fun invoke`.
 *
 * @property networkMonitor The [NetworkMonitor] implementation that provides
 *                          the device's connectivity status.
 * @see NetworkMonitor
 */
class CheckInternetConnectionUseCase(private val networkMonitor: NetworkMonitor) {
    /**
     * Checks whether the device has an active internet connection.
     *
     * This is a synchronous (non-suspending) call that returns the
     * connectivity state at the moment of invocation.
     *
     * @return `true` if the device is connected to the internet;
     *         `false` otherwise.
     */
    operator fun invoke(): Boolean {
        return networkMonitor.isConnected()
    }
}
