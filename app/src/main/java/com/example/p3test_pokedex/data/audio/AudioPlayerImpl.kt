package com.example.p3test_pokedex.data.audio

import android.media.AudioAttributes
import android.media.MediaPlayer
import com.example.p3test_pokedex.domain.repository.AudioPlayer
import com.example.p3test_pokedex.domain.repository.NetworkMonitor

/**
 * Concrete implementation of [AudioPlayer] utilizing Android's MediaPlayer.
 * Integrates [NetworkMonitor] to check connectivity before trying to play remote network audio,
 * preventing native state crashes when the device is offline.
 */
class AudioPlayerImpl(private val networkMonitor: NetworkMonitor) : AudioPlayer {
    
    private var mediaPlayer: MediaPlayer? = null

    override fun play(url: String) {
        // Prevent trying to play remote URLs when offline, avoiding native MediaPlayer crashes
        if (!networkMonitor.isConnected()) {
            return
        }

        try {
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer()
            } else {
                mediaPlayer?.reset()
            }
            
            mediaPlayer?.apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                setDataSource(url)
                setOnPreparedListener { mp ->
                    mp.start()
                }
                setOnErrorListener { mp, _, _ ->
                    // Avoid calling mp.reset() directly in the error callback on some Android platforms
                    true
                }
                prepareAsync()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun release() {
        try {
            mediaPlayer?.release()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            mediaPlayer = null
        }
    }
}
