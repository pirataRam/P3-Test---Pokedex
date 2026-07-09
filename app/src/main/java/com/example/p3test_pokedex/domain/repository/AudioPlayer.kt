package com.example.p3test_pokedex.domain.repository

/**
 * Contract for audio playback capabilities within the application.
 *
 * This interface is defined in the domain layer to keep it free from
 * Android framework dependencies (e.g., `MediaPlayer`). Implementations
 * reside in the data/infrastructure layer and handle the actual audio
 * streaming and resource management.
 *
 * Typical usage flow:
 * 1. Call [play] with a Pokémon cry URL to start playback.
 * 2. Call [release] when the audio player is no longer needed to free
 *    system resources and prevent memory leaks.
 *
 * @see com.example.p3test_pokedex.domain.repository.PokemonRepository
 */
interface AudioPlayer {
    /**
     * Starts playing audio from the specified network URL.
     *
     * If audio is already playing, the implementation should stop the current
     * playback and start the new one. The method is non-blocking; playback
     * occurs asynchronously.
     *
     * @param url The fully-qualified URL of the audio resource to play
     *            (e.g., a Pokémon cry `.ogg` file hosted on the PokéAPI media server).
     */
    fun play(url: String)

    /**
     * Releases all resources held by the audio player.
     *
     * This method should be called when the player is no longer needed
     * (e.g., when the hosting ViewModel is cleared or the screen is destroyed)
     * to free native resources and prevent memory leaks.
     * After calling this method, the player instance should not be reused.
     */
    fun release()
}
