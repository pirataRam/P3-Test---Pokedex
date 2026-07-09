package com.example.p3test_pokedex.domain.usecase

import com.example.p3test_pokedex.domain.repository.NetworkMonitor

/**
 * Use case to verify whether the device currently has access to the internet.
 *
 * @property networkMonitor The network monitor capability provider.
 */
class CheckInternetConnectionUseCase(private val networkMonitor: NetworkMonitor) {
    /**
     * Executes the use case.
     *
     * @return True if connected, false otherwise.
     */
    operator fun invoke(): Boolean {
        return networkMonitor.isConnected()
    }
}
