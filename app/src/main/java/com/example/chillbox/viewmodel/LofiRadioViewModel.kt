package com.example.chillbox.viewmodel

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chillbox.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LofiRadioViewModel : ViewModel() {
    var mediaPlayer: MediaPlayer? = null
    val mp3List = listOf(
        R.raw.lofi_storms,
        R.raw.pink_gaze,
        R.raw.the_forbidden_secret,
        R.raw.an_outlandish_dream
    )
    val songNames = listOf(
        "Lofi Storms",
        "Pink Gaze",
        "The Forbidden Secret",
        "An Outlandish Dream"
    )
    val songImages = listOf(
        R.drawable.lofi_storms_image,
        R.drawable.pink_gaze_image,
        R.drawable.the_forbidden_secret_image,
        R.drawable.an_outlandish_dream_image
    )
    var currentTrack = 0
    var isPlaying by mutableStateOf(false) // Track playing status
    var currentPosition by mutableFloatStateOf(0f) // Track progress for the slider
    var trackDuration by mutableFloatStateOf(0f) // Total duration of the track

    fun setupMediaPlayer(context: Context) {
        mediaPlayer = MediaPlayer.create(context, mp3List[currentTrack])
        mediaPlayer?.setOnPreparedListener { player ->
            trackDuration = player.duration.toFloat() // Ensure this is set before any UI interaction
            // Do not start the player automatically
        }
        mediaPlayer?.setOnCompletionListener {
            playNextTrack(context)
        }
    }

    fun destroyMediaPlayer() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

    fun togglePlayPause() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.pause()
                isPlaying = false
            } else {
                it.start()
                isPlaying = true
                updateSeekBar()
            }
        }
    }

    fun playNextTrack(context: Context) {
        val wasPlaying = isPlaying
        mediaPlayer?.reset()

        currentTrack = (currentTrack + 1) % mp3List.size
        mediaPlayer?.setDataSource(context, Uri.parse("android.resource://${context.packageName}/${mp3List[currentTrack]}"))
        mediaPlayer?.prepareAsync()
        mediaPlayer?.setOnPreparedListener {
            trackDuration = it.duration.toFloat()
            if (wasPlaying) {
                it.start()
            }
            updateSeekBar()
        }
        mediaPlayer?.setOnErrorListener { _, _, _ ->
            // Handle error
            false
        }
    }

    fun playPreviousTrack(context: Context) {
        val wasPlaying = isPlaying
        val currentPosition = mediaPlayer?.currentPosition ?: 0
        val threshold = 2000 // 2 seconds in milliseconds

        if (currentPosition > threshold) {
            // Restart the current song
            mediaPlayer?.seekTo(0)
            mediaPlayer?.start()
        } else {
            // Switch to the previous song
            mediaPlayer?.reset()

            currentTrack = if (currentTrack == 0) mp3List.size - 1 else currentTrack - 1
            mediaPlayer?.setDataSource(context, Uri.parse("android.resource://${context.packageName}/${mp3List[currentTrack]}"))
            mediaPlayer?.prepareAsync()
            mediaPlayer?.setOnPreparedListener {
                trackDuration = it.duration.toFloat()
                if (wasPlaying) {
                    it.start()
                }
                updateSeekBar()
            }
            mediaPlayer?.setOnErrorListener { _, what, extra ->
                // Handle error
                false
            }
        }
    }

    fun seekTo(position: Float) {
        mediaPlayer?.let {
            val newPosition = (position * trackDuration).toInt()
            it.seekTo(newPosition)
            currentPosition = newPosition.toFloat()
        }
    }

    private fun updateSeekBar() {
        viewModelScope.launch {
            while (isPlaying) {
                mediaPlayer?.let {
                    currentPosition = it.currentPosition.toFloat()
                }
                delay(200L) // Use a smaller delay for smoother updates
            }
        }
    }
}