package com.example.p3test_pokedex.data.local.converter

import androidx.room.TypeConverter
import com.example.p3test_pokedex.domain.model.PokemonStat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Type converters for Room Database to serialize/deserialize complex objects using Gson.
 */
class Converters {

    /**
     * Gson instance used for JSON serialization and deserialization.
     */
    private val gson = Gson()

    /**
     * Converts a JSON string representing a list of strings into a list of strings.
     *
     * @param value The JSON string.
     * @return Deserialized list of strings, or an empty list if value is null.
     */
    @TypeConverter
    fun fromStringListJson(value: String?): List<String> {
        if (value == null) return emptyList()
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, listType)
    }

    /**
     * Converts a list of strings into a JSON string representation.
     *
     * @param list The list of strings.
     * @return Serialized JSON string.
     */
    @TypeConverter
    fun toStringListJson(list: List<String>?): String {
        return gson.toJson(list ?: emptyList<String>())
    }

    /**
     * Converts a JSON string representing a list of [PokemonStat] into a list.
     *
     * @param value The JSON string.
     * @return Deserialized list of [PokemonStat], or an empty list if value is null.
     */
    @TypeConverter
    fun fromPokemonStatListJson(value: String?): List<PokemonStat> {
        if (value == null) return emptyList()
        val listType = object : TypeToken<List<PokemonStat>>() {}.type
        return gson.fromJson(value, listType)
    }

    /**
     * Converts a list of [PokemonStat] into a JSON string representation.
     *
     * @param list The list of [PokemonStat].
     * @return Serialized JSON string.
     */
    @TypeConverter
    fun toPokemonStatListJson(list: List<PokemonStat>?): String {
        return gson.toJson(list ?: emptyList<PokemonStat>())
    }
}
