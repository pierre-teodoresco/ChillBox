package com.example.chillbox

import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.chillbox.ui.components.BackButton
import com.example.chillbox.ui.theme.ChillBoxTheme
import kotlinx.coroutines.*

class LofiRadioActivity : ComponentActivity() {

    private var mediaPlayer: MediaPlayer? = null
    private val mp3List = listOf(
        R.raw.lofi_storms,
        R.raw.pink_gaze,
        R.raw.the_forbidden_secret
    )
    private var currentTrack = 0
    private var isPlaying by mutableStateOf(false) // Track playing status
    private var currentPosition by mutableStateOf(0f) // Track progress for the slider
    private var trackDuration by mutableStateOf(0f) // Total duration of the track

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChillBoxTheme {
                LofiRadioScreen(
                    isPlaying = isPlaying,
                    currentPosition = currentPosition,
                    trackDuration = trackDuration,
                    onPlayPauseClicked = { togglePlayPause() },
                    onNextClicked = { playNextTrack() },
                    onSliderChanged = { seekTo(it) }
                )
            }
        }
        setupMediaPlayer()
    }

    private fun setupMediaPlayer() {
        mediaPlayer = MediaPlayer.create(this, mp3List[currentTrack])
        mediaPlayer?.setOnPreparedListener { player ->
            trackDuration = player.duration.toFloat() // Ensure this is set before any UI interaction
            player.start()
            isPlaying = true
            updateSeekBar()
        }
        mediaPlayer?.setOnCompletionListener {
            playNextTrack()
        }
    }

    private fun togglePlayPause() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.pause()
                isPlaying = false
            } else {
                it.start()
                isPlaying = true
            }
        }
    }

    private fun playNextTrack() {
        mediaPlayer?.stop()
        mediaPlayer?.release()

        currentTrack = (currentTrack + 1) % mp3List.size
        mediaPlayer = MediaPlayer.create(this, mp3List[currentTrack])
        mediaPlayer?.setOnPreparedListener { player ->
            trackDuration = player.duration.toFloat()
            player.start()
            isPlaying = true
            updateSeekBar()
        }
        mediaPlayer?.setOnCompletionListener {
            playNextTrack()
        }
    }

    private fun seekTo(position: Float) {
        mediaPlayer?.let {
            val newPosition = (position * trackDuration).toInt()
            it.seekTo(newPosition)
        }
    }

    private fun updateSeekBar() {
        GlobalScope.launch {
            while (mediaPlayer != null) {
                mediaPlayer?.let {
                    currentPosition = it.currentPosition.toFloat()
                }
                delay(1000L)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}

@Composable
fun LofiRadioScreen(
    isPlaying: Boolean,
    currentPosition: Float,
    trackDuration: Float,
    onPlayPauseClicked: () -> Unit,
    onNextClicked: () -> Unit,
    onSliderChanged: (Float) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        BackButton(scaleFactor = 1.0f)

        // Music Player Controls
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = { onPlayPauseClicked() }) {
                Image(
                    painter = painterResource(
                        id = if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play
                    ),
                    contentDescription = "Play/Pause",
                    modifier = Modifier.size(48.dp)
                )
            }

            IconButton(onClick = { onNextClicked() }) {
                Image(
                    painter = painterResource(id = R.drawable.ic_next),
                    contentDescription = "Next Track",
                    modifier = Modifier.size(48.dp)
                )
            }
        }

        // Slider for Music Progress
        Slider(
            value = if (trackDuration > 0) currentPosition / trackDuration else 0f, // Avoid NaN by handling cases where trackDuration is 0
            onValueChange = onSliderChanged,
            modifier = Modifier.fillMaxWidth(),
            valueRange = 0f..1f,
            colors = androidx.compose.material3.SliderDefaults.colors(
                thumbColor = Color.White,
                activeTrackColor = Color.Cyan
            )
        )

        // Current track time (in seconds)
        Text(text = "Progress: ${currentPosition.toInt() / 1000}s / ${(trackDuration / 1000).toInt()}s")
    }
}

@Preview(showBackground = true)
@Composable
fun LofiRadioScreenPreview() {
    ChillBoxTheme {
        LofiRadioScreen(
            isPlaying = false,
            currentPosition = 0f,
            trackDuration = 100f,
            onPlayPauseClicked = {},
            onNextClicked = {},
            onSliderChanged = {}
        )
    }
}
