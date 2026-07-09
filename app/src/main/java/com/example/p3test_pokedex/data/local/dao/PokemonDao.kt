package com.example.p3test_pokedex.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.p3test_pokedex.data.local.entity.PokemonDetailEntity
import com.example.p3test_pokedex.data.local.entity.PokemonEntity
import com.example.p3test_pokedex.data.local.entity.FavoritePokemonEntity

/**
 * Data Access Object for local Pokemon and PokemonDetail operations using Room.
 */
@Dao
interface PokemonDao {

    /**
     * Inserts a list of Pokémon summaries into the database, replacing duplicates.
     *
     * @param pokemon The list of [PokemonEntity] to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemonList(pokemon: List<PokemonEntity>)

    /**
     * Inserts detailed Pokémon information into the database, replacing duplicates.
     *
     * @param pokemonDetail The [PokemonDetailEntity] to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemonDetail(pokemonDetail: PokemonDetailEntity)

    /**
     * Retrieves a paginated range of Pokémon.
     *
     * @param limit The maximum number of Pokémon to return.
     * @param offset The starting position.
     * @return The list of cached [PokemonEntity] ordered by ID.
     */
    @Query("SELECT * FROM pokemon ORDER BY id ASC LIMIT :limit OFFSET :offset")
    suspend fun getPokemonList(limit: Int, offset: Int): List<PokemonEntity>

    /**
     * Retrieves detailed information of a Pokémon by its unique ID.
     *
     * @param id The ID of the Pokémon.
     * @return The cached [PokemonDetailEntity] if found, or null otherwise.
     */
    @Query("SELECT * FROM pokemon_detail WHERE id = :id")
    suspend fun getPokemonDetail(id: Int): PokemonDetailEntity?

    /**
     * Retrieves detailed information of a Pokémon by its name.
     *
     * @param name The name of the Pokémon.
     * @return The cached [PokemonDetailEntity] if found, or null otherwise.
     */
    @Query("SELECT * FROM pokemon_detail WHERE name = :name")
    suspend fun getPokemonDetailByName(name: String): PokemonDetailEntity?

    /**
     * Inserts a Pokémon into the favorites list database table, replacing duplicates.
     *
     * @param favorite The [FavoritePokemonEntity] to save.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoritePokemonEntity)

    /**
     * Deletes a favorite Pokémon entry by its ID.
     *
     * @param id The unique ID of the Pokémon to remove from favorites.
     */
    @Query("DELETE FROM favorite_pokemon WHERE id = :id")
    suspend fun deleteFavorite(id: Int)

    /**
     * Checks if a Pokémon is marked as a favorite.
     *
     * @param id The unique ID of the Pokémon.
     * @return `true` if the Pokémon exists in the favorites table; `false` otherwise.
     */
    @Query("SELECT EXISTS(SELECT 1 FROM favorite_pokemon WHERE id = :id)")
    suspend fun isFavorite(id: Int): Boolean

    /**
     * Retrieves the complete list of all favorite Pokémon from the database.
     *
     * @return List of cached [FavoritePokemonEntity] ordered by ID.
     */
    @Query("SELECT * FROM favorite_pokemon ORDER BY id ASC")
    suspend fun getFavorites(): List<FavoritePokemonEntity>
}
