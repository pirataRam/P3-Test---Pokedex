package com.example.p3test_pokedex.data.di

import androidx.room.Room
import com.example.p3test_pokedex.data.local.AppDatabase
import com.example.p3test_pokedex.data.remote.PokeApiService
import com.example.p3test_pokedex.data.repository.PokemonRepositoryImpl
import com.example.p3test_pokedex.domain.repository.PokemonRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Koin module definition for the Data Layer of the Pokédex application.
 * Declares all remote (Retrofit) and local (Room) database singletons.
 */
val dataModule = module {

    // Local Storage (Room Database)
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "pokedex_database"
        ).fallbackToDestructiveMigration()
         .build()
    }

    // Pokémon Dao
    single {
        get<AppDatabase>().pokemonDao()
    }

    // OkHttpClient with logging
    single {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    // Retrofit Instance
    single {
        Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // API Service
    single {
        get<Retrofit>().create(PokeApiService::class.java)
    }

    // Repository Implementation
    single<PokemonRepository> {
        PokemonRepositoryImpl(
            apiService = get(),
            pokemonDao = get()
        )
    }
}
