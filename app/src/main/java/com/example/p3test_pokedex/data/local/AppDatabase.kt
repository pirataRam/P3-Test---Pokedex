package com.example.p3test_pokedex.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.p3test_pokedex.data.local.converter.Converters
import com.example.p3test_pokedex.data.local.dao.PokemonDao
import com.example.p3test_pokedex.data.local.entity.PokemonDetailEntity
import com.example.p3test_pokedex.data.local.entity.PokemonEntity

/**
 * Main application database using Room.
 *
 * Provides access to the Pokémon data storage.
 */
@Database(
    entities = [
        PokemonEntity::class,
        PokemonDetailEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    /**
     * Obtains the Pokémon Data Access Object.
     */
    abstract fun pokemonDao(): PokemonDao
}
