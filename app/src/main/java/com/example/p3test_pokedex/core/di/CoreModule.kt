package com.example.p3test_pokedex.core.di

import androidx.room.Room
import com.example.p3test_pokedex.data.local.AppDatabase
import com.example.p3test_pokedex.data.remote.PokeApiService
import com.example.p3test_pokedex.data.repository.PokemonRepositoryImpl
import com.example.p3test_pokedex.domain.repository.PokemonRepository
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
 * Core Koin module that defines all dependencies across the layers of the application.
 */
val networkModule = module {
    single {
        OkHttpClient.Builder()
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
}

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "pokedex_database"
        ).fallbackToDestructiveMigration()
         .build()
    }

    single {
        get<AppDatabase>().pokemonDao()
    }
}

val repositoryModule = module {
    single<PokemonRepository> {
        PokemonRepositoryImpl(
            apiService = get(),
            pokemonDao = get()
        )
    }
}

val useCaseModule = module {
    factory { GetPokemonListPagedUseCase(get()) }
    factory { GetPokemonDetailUseCase(get()) }
    factory { GetFavoriteListUseCase(get()) }
    factory { IsFavoriteUseCase(get()) }
    factory { AddFavoriteUseCase(get()) }
    factory { RemoveFavoriteUseCase(get()) }
}

val viewModelModule = module {
    viewModel { PokemonListViewModel(get(), get(), get()) }
    viewModel { PokemonDetailViewModel(get(), get(), get(), get()) }
}

val coreModules = listOf(
    networkModule,
    databaseModule,
    repositoryModule,
    useCaseModule,
    viewModelModule
)
