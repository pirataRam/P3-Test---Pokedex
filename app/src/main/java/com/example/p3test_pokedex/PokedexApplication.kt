package com.example.p3test_pokedex

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import com.example.p3test_pokedex.core.di.coreModules
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.context.startKoin

/**
 * Main Application class for the Pokédex app.
 * Initializes Koin dependency injection on startup.
 * Implements [ImageLoaderFactory] to configure Glide-like disk and memory caching for Coil globally.
 */
class PokedexApplication : Application(), ImageLoaderFactory, KoinComponent {
    
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@PokedexApplication)
            modules(coreModules)
        }
    }

    /**
     * Configures the global Coil image loader with persistent disk caching.
     * By disabling respectCacheHeaders (similar to Glide), images are guaranteed to be cached
     * on disk and accessible offline regardless of remote server HTTP cache control headers.
     */
    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .okHttpClient { get<OkHttpClient>() }
            .respectCacheHeaders(false) // Force caching on disk like Glide
            .diskCache {
                DiskCache.Builder()
                    .directory(this.cacheDir.resolve("image_cache"))
                    .maxSizeBytes(300 * 1024 * 1024L) // 300 MiB Disk Cache
                    .build()
            }
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(0.25) // 25% of available memory
                    .build()
            }
            .build()
    }
}
