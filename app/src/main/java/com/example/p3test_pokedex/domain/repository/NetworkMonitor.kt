package com.example.p3test_pokedex.domain.repository

/**
 * Interface detailing network connection checking capabilities.
 * Abstracted to remain free from Android framework classes in the domain layer.
 */
interface NetworkMonitor {
    /**
     * Returns true if there is an active internet connection, false otherwise.
     */
    fun isConnected(): Boolean
}
