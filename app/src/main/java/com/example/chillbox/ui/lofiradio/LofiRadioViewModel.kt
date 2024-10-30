package com.example.chillbox.ui.lofiradio

import android.content.Context
import android.media.MediaPlayer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chillbox.R
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LofiRadioViewModel : ViewModel() {
    private val _state = MutableStateFlow(LofiRadioUiState())
    val state: StateFlow<LofiRadioUiState> = _state.asStateFlow()

    private var mediaPlayer: MediaPlayer? = null
    private val playlist = listOf(
        R.raw.lofi_storms,
        R.raw.pink_gaze,
        R.raw.the_forbidden_secret,
        R.raw.an_outlandish_dream
    )
    private var titles: List<String> = listOf(
        "Lofi Storms",
        "Pink Gaze",
        "The Forbidden Secret",
        "An Outlandish Dream"
    )
    private var images: List<Int> = listOf(
        R.drawable.lofi_storms_image,
        R.drawable.pink_gaze_image,
        R.drawable.the_forbidden_secret_image,
        R.drawable.an_outlandish_dream_image
    )

    private var currentTrack = 0
    private var progressTracker: Job? = null

    fun initMediaPlayer(context: Context) {
        mediaPlayer = MediaPlayer.create(context, playlist[currentTrack]).apply{
            setOnCompletionListener {
                next(context)
            }
        }
        updateUiState()
    }

    fun play() {
        if (!_state.value.isPlaying) {
            mediaPlayer?.start()
            _state.update { it.copy(isPlaying = true) }
            startProgressTracker()
        }
    }

    fun pause() {
        if (_state.value.isPlaying) {
            mediaPlayer?.pause()
            _state.update { it.copy(isPlaying = false) }
            stopProgressTracker()
        }
    }

    fun stop() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null;
        stopProgressTracker()
        _state.update { it.copy(isPlaying = false, progress = 0) }
    }

    fun next(context: Context) {
        currentTrack = (currentTrack + 1) % playlist.size
        playNewTrack(context)
    }

    fun previous(context: Context) {
        val threshold = 2000 // 2 seconds

        if ((mediaPlayer?.currentPosition ?: 0) > threshold) {
            mediaPlayer?.seekTo(0)
            _state.update { it.copy(progress = 0) }
            return
        }

        currentTrack = if (currentTrack > 0) currentTrack - 1 else playlist.size - 1
        playNewTrack(context)
    }

    private fun playNewTrack(context: Context) {
        stop() // Ensure the previous track is stopped and resources are released
        initMediaPlayer(context) // Initialize MediaPlayer with the new track
        play() // Start playing the new track
        updateUiState() // Update the UI state with the new track details
    }

    fun onSeek(newPosition: Int) {
        mediaPlayer?.seekTo(newPosition)
        _state.update { it.copy(progress = newPosition) }
    }

    fun getTrackTitle(): String {
        return titles[_state.value.currentTrack]
    }

    fun getTrackName(): Int {
        return images[_state.value.currentTrack]
    }

    private fun updateUiState() {
        _state.update {
            it.copy(
                isPlaying = mediaPlayer?.isPlaying ?: false,
                currentTrack = currentTrack,
                currentTrackDuration = mediaPlayer?.duration ?: 0
            )
        }
    }

    private fun startProgressTracker() {
        // Cancel any existing progress job
        progressTracker?.cancel()
        // Launch a coroutine to update progress
        progressTracker = viewModelScope.launch {
            mediaPlayer?.let { player ->
                while (player.isPlaying) {
                    _state.update { it.copy(progress = player.currentPosition) }
                    delay(100) // Update progress every 100ms
                }
            }
        }
    }

    private fun stopProgressTracker() {
        progressTracker?.cancel()
        progressTracker = null
    }

    override fun onCleared() {
        super.onCleared()
        stop() // Ensure MediaPlayer is released when ViewModel is cleared
    }
}