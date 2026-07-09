package com.example.p3test_pokedex.domain.repository

/**
 * Contract for checking the device's network connectivity status.
 *
 * This interface is defined in the domain layer to keep it free from
 * Android framework dependencies (e.g., `ConnectivityManager`). Implementations
 * reside in the data/infrastructure layer and inspect the actual system
 * connectivity state.
 *
 * Used by [com.example.p3test_pokedex.domain.usecase.CheckInternetConnectionUseCase]
 * to determine whether the app should attempt network operations.
 *
 * @see com.example.p3test_pokedex.domain.usecase.CheckInternetConnectionUseCase
 */
interface NetworkMonitor {
    /**
     * Checks whether the device currently has an active internet connection.
     *
     * The check is synchronous and returns the connectivity state at the
     * moment of invocation. It does not guarantee that a specific endpoint
     * is reachable — only that a network route exists.
     *
     * @return `true` if the device has an active internet connection;
     *         `false` otherwise.
     */
    fun isConnected(): Boolean
}
