package com.example.p3test_pokedex.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.p3test_pokedex.domain.repository.NetworkMonitor

/**
 * Concrete implementation of [NetworkMonitor] that checks Android system connectivity.
 *
 * @property context The application context used to retrieve system connectivity services.
 */
class NetworkMonitorImpl(private val context: Context) : NetworkMonitor {
    
    /**
     * Checks whether the device is currently connected to the internet.
     * Checks for active network capabilities using [ConnectivityManager].
     *
     * @return `true` if connected; `false` otherwise.
     */
    override fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}
