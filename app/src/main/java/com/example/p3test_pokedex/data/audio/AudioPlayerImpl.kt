package com.example.p3test_pokedex.data.audio

import android.media.AudioAttributes
import android.media.MediaPlayer
import com.example.p3test_pokedex.domain.repository.AudioPlayer

/**
 * Concrete implementation of [AudioPlayer] utilizing Android's MediaPlayer.
 */
class AudioPlayerImpl : AudioPlayer {
    
    private var mediaPlayer: MediaPlayer? = null

    override fun play(url: String) {
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
                    mp.reset()
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
