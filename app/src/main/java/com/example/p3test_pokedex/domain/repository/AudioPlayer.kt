package com.example.p3test_pokedex.domain.repository

/**
 * Interface detailing audio playing capabilities.
 * Abstracted to remain free from Android framework classes in the domain layer.
 */
interface AudioPlayer {
    /**
     * Plays audio from the specified network URL.
     */
    fun play(url: String)

    /**
     * Releases audio player resources.
     */
    fun release()
}
