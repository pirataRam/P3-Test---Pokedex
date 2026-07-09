package com.example.p3test_pokedex.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.p3test_pokedex.domain.repository.NetworkMonitor

/**
 * Concrete implementation of [NetworkMonitor] that checks Android system connectivity.
 */
class NetworkMonitorImpl(private val context: Context) : NetworkMonitor {
    
    override fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}
