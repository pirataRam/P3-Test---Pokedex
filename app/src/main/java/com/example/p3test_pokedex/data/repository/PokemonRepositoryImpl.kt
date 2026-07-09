package com.example.p3test_pokedex.data.repository

import com.example.p3test_pokedex.data.local.dao.PokemonDao
import com.example.p3test_pokedex.data.local.entity.PokemonDetailEntity
import com.example.p3test_pokedex.data.local.entity.PokemonEntity
import com.example.p3test_pokedex.data.remote.PokeApiService
import com.example.p3test_pokedex.domain.model.Pokemon
import com.example.p3test_pokedex.domain.model.PokemonDetail
import com.example.p3test_pokedex.domain.model.PokemonStat
import com.example.p3test_pokedex.domain.repository.PokemonRepository

import com.example.p3test_pokedex.data.local.entity.FavoritePokemonEntity

/**
 * Implementation of [PokemonRepository] that provides caching functionality
 * using a local Room Database and network fetching using a Retrofit API service.
 */
class PokemonRepositoryImpl(
    private val apiService: PokeApiService,
    private val pokemonDao: PokemonDao
) : PokemonRepository {

    override suspend fun getFavorites(): List<Pokemon> {
        return pokemonDao.getFavorites().map {
            Pokemon(it.id, it.name, it.imageUrl)
        }
    }

    override suspend fun addFavorite(pokemon: Pokemon) {
        pokemonDao.insertFavorite(
            FavoritePokemonEntity(
                id = pokemon.id,
                name = pokemon.name,
                imageUrl = pokemon.imageUrl
            )
        )
    }

    override suspend fun removeFavorite(id: Int) {
        pokemonDao.deleteFavorite(id)
    }

    override suspend fun isFavorite(id: Int): Boolean {
        return pokemonDao.isFavorite(id)
    }

    override suspend fun getPokemonList(limit: Int, offset: Int): List<Pokemon> {
        val cached = pokemonDao.getPokemonList(limit, offset)
        return if (cached.isEmpty() || cached.size < limit) {
            val apiResponse = apiService.getPokemonList(limit, offset)
            val entities = apiResponse.results.map { dto ->
                val id = extractIdFromUrl(dto.url)
                val imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"
                PokemonEntity(
                    id = id,
                    name = dto.name,
                    imageUrl = imageUrl
                )
            }
            pokemonDao.insertPokemonList(entities)
            entities.map { Pokemon(it.id, it.name, it.imageUrl) }
        } else {
            cached.map { Pokemon(it.id, it.name, it.imageUrl) }
        }
    }

    override suspend fun getPokemonDetail(id: Int): PokemonDetail {
        val cached = pokemonDao.getPokemonDetail(id)
        return if (cached != null) {
            mapToDomain(cached)
        } else {
            val apiDetail = apiService.getPokemonDetail(id)
            val imageUrl = apiDetail.sprites.other?.officialArtwork?.frontDefault
                ?: apiDetail.sprites.frontDefault
                ?: "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"

            val entity = PokemonDetailEntity(
                id = apiDetail.id,
                name = apiDetail.name,
                imageUrl = imageUrl,
                types = apiDetail.types.map { it.type.name },
                abilities = apiDetail.abilities.map { it.ability.name },
                stats = apiDetail.stats.map { PokemonStat(it.stat.name, it.baseStat) },
                weight = apiDetail.weight,
                height = apiDetail.height,
                baseExperience = apiDetail.baseExperience
            )
            pokemonDao.insertPokemonDetail(entity)
            mapToDomain(entity)
        }
    }

    override suspend fun getPokemonDetailByName(name: String): PokemonDetail {
        val cached = pokemonDao.getPokemonDetailByName(name)
        return if (cached != null) {
            mapToDomain(cached)
        } else {
            val apiDetail = apiService.getPokemonDetailByName(name)
            val imageUrl = apiDetail.sprites.other?.officialArtwork?.frontDefault
                ?: apiDetail.sprites.frontDefault
                ?: "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${apiDetail.id}.png"

            val entity = PokemonDetailEntity(
                id = apiDetail.id,
                name = apiDetail.name,
                imageUrl = imageUrl,
                types = apiDetail.types.map { it.type.name },
                abilities = apiDetail.abilities.map { it.ability.name },
                stats = apiDetail.stats.map { PokemonStat(it.stat.name, it.baseStat) },
                weight = apiDetail.weight,
                height = apiDetail.height,
                baseExperience = apiDetail.baseExperience
            )
            pokemonDao.insertPokemonDetail(entity)
            mapToDomain(entity)
        }
    }

    /**
     * Extracts the Pokémon ID from its url.
     * Expected url format: https://pokeapi.co/api/v2/pokemon/{id}/
     */
    private fun extractIdFromUrl(url: String): Int {
        return url.split("/")
            .filter { it.isNotEmpty() }
            .last()
            .toInt()
    }

    /**
     * Maps a [PokemonDetailEntity] to a domain [PokemonDetail].
     */
    private fun mapToDomain(entity: PokemonDetailEntity): PokemonDetail {
        return PokemonDetail(
            id = entity.id,
            name = entity.name,
            imageUrl = entity.imageUrl,
            types = entity.types,
            abilities = entity.abilities,
            stats = entity.stats,
            weight = entity.weight,
            height = entity.height,
            baseExperience = entity.baseExperience
        )
    }
}
