package com.example.p3test_pokedex.core.di

import androidx.room.Room
import com.example.p3test_pokedex.data.local.AppDatabase
import com.example.p3test_pokedex.data.remote.PokeApiService
import com.example.p3test_pokedex.data.repository.PokemonRepositoryImpl
import com.example.p3test_pokedex.domain.repository.PokemonRepository
import com.example.p3test_pokedex.domain.repository.NetworkMonitor
import com.example.p3test_pokedex.data.network.NetworkMonitorImpl
import com.example.p3test_pokedex.domain.repository.AudioPlayer
import com.example.p3test_pokedex.data.audio.AudioPlayerImpl
import com.example.p3test_pokedex.domain.usecase.CheckInternetConnectionUseCase
import com.example.p3test_pokedex.domain.usecase.AddFavoriteUseCase
import com.example.p3test_pokedex.domain.usecase.GetFavoriteListUseCase
import com.example.p3test_pokedex.domain.usecase.GetPokemonDetailUseCase
import com.example.p3test_pokedex.domain.usecase.GetPokemonListPagedUseCase
import com.example.p3test_pokedex.domain.usecase.IsFavoriteUseCase
import com.example.p3test_pokedex.domain.usecase.RemoveFavoriteUseCase
import com.example.p3test_pokedex.presentation.detail.PokemonDetailViewModel
import com.example.p3test_pokedex.presentation.list.PokemonListViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Koin module configuration defining dependencies related to networking components.
 * Configures Retrofit with base URL "https://pokeapi.co/api/v2/", an OkHttpClient
 * instance with disk cache enabled, [NetworkMonitorImpl], and [AudioPlayerImpl].
 */
val networkModule = module {
    single {
        val cacheSize = 50 * 1024 * 1024L // 50 MiB
        val cacheDir = java.io.File(androidContext().cacheDir, "http_cache")
        OkHttpClient.Builder()
            .cache(okhttp3.Cache(cacheDir, cacheSize))
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single {
        get<Retrofit>().create(PokeApiService::class.java)
    }

    single<NetworkMonitor> { NetworkMonitorImpl(androidContext()) }
    single<AudioPlayer> { AudioPlayerImpl(get()) }
}

/**
 * Koin module configuration defining local database dependencies.
 * Establishes the Room database [AppDatabase] and exposes its DAO interface [PokemonDao].
 */
val databaseModule = module {
    single {
        Room.databaseBuilder(
                androidContext(),
                AppDatabase::class.java,
                "pokedex_database"
            ).fallbackToDestructiveMigration(false)
         .build()
    }

    single {
        get<AppDatabase>().pokemonDao()
    }
}

/**
 * Koin module configuration binding interfaces in the domain layer to concrete
 * implementations in the data layer.
 */
val repositoryModule = module {
    single<PokemonRepository> {
        PokemonRepositoryImpl(
            apiService = get(),
            pokemonDao = get()
        )
    }
}

/**
 * Koin module configuration exposing individual domain use cases as factory definitions.
 */
val useCaseModule = module {
    factory { GetPokemonListPagedUseCase(get()) }
    factory { GetPokemonDetailUseCase(get()) }
    factory { GetFavoriteListUseCase(get()) }
    factory { IsFavoriteUseCase(get()) }
    factory { AddFavoriteUseCase(get()) }
    factory { RemoveFavoriteUseCase(get()) }
    factory { CheckInternetConnectionUseCase(get()) }
}

/**
 * Koin module configuration injecting all ViewModels required by Jetpack Compose screens.
 */
val viewModelModule = module {
    viewModel { PokemonListViewModel(get(), get(), get(), get()) }
    viewModel { PokemonDetailViewModel(get(), get(), get(), get(), get()) }
}

/**
 * List containing all Koin modules that need to be started inside [PokedexApplication].
 */
val coreModules = listOf(
    networkModule,
    databaseModule,
    repositoryModule,
    useCaseModule,
    viewModelModule
)
