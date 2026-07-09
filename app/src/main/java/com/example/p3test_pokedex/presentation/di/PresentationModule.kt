package com.example.p3test_pokedex.presentation.di

import com.example.p3test_pokedex.domain.usecase.GetPokemonDetailUseCase
import com.example.p3test_pokedex.domain.usecase.GetPokemonListUseCase
import com.example.p3test_pokedex.presentation.detail.PokemonDetailViewModel
import com.example.p3test_pokedex.presentation.list.PokemonListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Koin module definition for the Presentation Layer.
 * Registers use cases and view models.
 */
val presentationModule = module {
    // Use Cases (Factories)
    factory { GetPokemonListUseCase(get()) }
    factory { GetPokemonDetailUseCase(get()) }

    // ViewModels
    viewModel { PokemonListViewModel(get()) }
    viewModel { PokemonDetailViewModel(get()) }
}
