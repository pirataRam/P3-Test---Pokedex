package com.example.p3test_pokedex

import android.app.Application
import com.example.p3test_pokedex.core.di.coreModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

/**
 * Main Application class for the Pokédex app.
 * Initializes Koin dependency injection on startup.
 */
class PokedexApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@PokedexApplication)
            modules(coreModules)
        }
    }
}
