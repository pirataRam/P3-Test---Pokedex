package com.example.p3test_pokedex.domain.usecase

import com.example.p3test_pokedex.domain.model.Pokemon
import com.example.p3test_pokedex.domain.model.PokemonDetail
import com.example.p3test_pokedex.domain.model.PokemonStat
import com.example.p3test_pokedex.domain.repository.PokemonRepository

/**
 * Reusable Fake implementation of the [PokemonRepository] for testing purposes.
 */
class FakePokemonRepository : PokemonRepository {
    private val fakeList = listOf(
        Pokemon(1, "bulbasaur", "https://example.com/1.png"),
        Pokemon(2, "ivysaur", "https://example.com/2.png"),
        Pokemon(3, "venusaur", "https://example.com/3.png")
    )

    private val favorites = mutableListOf<Pokemon>()

    override suspend fun getPokemonList(limit: Int, offset: Int): List<Pokemon> {
        return fakeList
    }

    override suspend fun getPokemonDetail(id: Int): PokemonDetail {
        return PokemonDetail(
            id = id,
            name = "pokemon-$id",
            imageUrl = "https://example.com/$id.png",
            types = listOf("grass"),
            abilities = listOf("overgrow"),
            stats = listOf(PokemonStat("hp", 45)),
            weight = 69,
            height = 7,
            baseExperience = 64
        )
    }

    override suspend fun getPokemonDetailByName(name: String): PokemonDetail {
        return PokemonDetail(
            id = 1,
            name = name,
            imageUrl = "https://example.com/1.png",
            types = listOf("grass"),
            abilities = listOf("overgrow"),
            stats = listOf(PokemonStat("hp", 45)),
            weight = 69,
            height = 7,
            baseExperience = 64
        )
    }

    override suspend fun getFavorites(): List<Pokemon> {
        return favorites
    }

    override suspend fun addFavorite(pokemon: Pokemon) {
        if (!favorites.any { it.id == pokemon.id }) {
            favorites.add(pokemon)
        }
    }

    override suspend fun removeFavorite(id: Int) {
        favorites.removeAll { it.id == id }
    }

    override suspend fun isFavorite(id: Int): Boolean {
        return favorites.any { it.id == id }
    }
}
