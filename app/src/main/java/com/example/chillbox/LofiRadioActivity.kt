package com.example.chillbox

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalConfiguration
import androidx.lifecycle.lifecycleScope
import com.example.chillbox.model.TimeManager
import com.example.chillbox.ui.components.BackButton
import com.example.chillbox.ui.components.CustomSlider
import com.example.chillbox.ui.theme.ChillBoxTheme
import kotlinx.coroutines.*

class LofiRadioActivity : ComponentActivity() {

    private var mediaPlayer: MediaPlayer? = null
    private val mp3List = listOf(
        R.raw.lofi_storms,
        R.raw.pink_gaze,
        R.raw.the_forbidden_secret,
        R.raw.an_outlandish_dream
    )
    private val songNames = listOf(
        "Lofi Storms",
        "Pink Gaze",
        "The Forbidden Secret",
        "An Outlandish Dream"
    )
    private val songImages = listOf(
        R.drawable.lofi_storms_image,
        R.drawable.pink_gaze_image,
        R.drawable.the_forbidden_secret_image,
        R.drawable.an_outlandish_dream_image
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
                    songName = songNames[currentTrack],
                    songImage = songImages[currentTrack],
                    onPlayPauseClicked = { togglePlayPause() },
                    onNextClicked = { playNextTrack() },
                    onPreviousClicked = { playPreviousTrack() },
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
            // Do not start the player automatically
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
                updateSeekBar()
            }
        }
    }

    private fun playNextTrack() {
        val wasPlaying = isPlaying
        mediaPlayer?.reset()

        currentTrack = (currentTrack + 1) % mp3List.size
        mediaPlayer?.setDataSource(this, Uri.parse("android.resource://$packageName/${mp3List[currentTrack]}"))
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

    private fun playPreviousTrack() {
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
            mediaPlayer?.setDataSource(this, Uri.parse("android.resource://$packageName/${mp3List[currentTrack]}"))
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

    private fun seekTo(position: Float) {
        mediaPlayer?.let {
            val newPosition = (position * trackDuration).toInt()
            it.seekTo(newPosition)
            currentPosition = newPosition.toFloat()
        }
    }

    private fun updateSeekBar() {
        lifecycleScope.launch {
            while (isPlaying) {
                mediaPlayer?.let {
                    currentPosition = it.currentPosition.toFloat()
                }
                delay(200L) // Use a smaller delay for smoother updates
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
    songName: String,
    songImage: Int,
    onPlayPauseClicked: () -> Unit,
    onNextClicked: () -> Unit,
    onPreviousClicked: () -> Unit,
    onSliderChanged: (Float) -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp
    // Define scaling factor based on screen width (e.g., tablets or large devices)
    val scaleFactor = if (screenWidthDp > 600) 2.0f else 1.0f

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding((16 * scaleFactor).dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Reusable BackButton from the BackButton.kt file
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopStart) {
            BackButton(scaleFactor = scaleFactor)
        }

        Spacer(modifier = Modifier.height((16 * scaleFactor).dp))

        // Song Image with top and bottom padding and rounded corners
        Image(
            painter = painterResource(id = songImage),
            contentDescription = "Song Image",
            modifier = Modifier
                .size((500 * scaleFactor).dp) // Use appropriate size
        )

        Spacer(modifier = Modifier.height((16 * scaleFactor).dp))

        // Song Name with padding
        Text(
            text = songName,
            style = MaterialTheme.typography.titleLarge.copy(fontSize = (25 * scaleFactor).sp),
            modifier = Modifier.padding(vertical = (8 * scaleFactor).dp) // Adjust the padding values as needed
        )

        Spacer(modifier = Modifier.height((16 * scaleFactor).dp))

        // Music Player Controls
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = { onPreviousClicked() }) {
                Image(
                    painter = painterResource(id = R.drawable.ic_previous),
                    contentDescription = "Previous Track",
                    modifier = Modifier.size((24 * scaleFactor).dp) // Increase the size
                )
            }

            IconButton(onClick = { onPlayPauseClicked() }) {
                Image(
                    painter = painterResource(
                        id = if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play
                    ),
                    contentDescription = "Play/Pause",
                    modifier = Modifier.size((24 * scaleFactor).dp) // Increase the size
                )
            }

            IconButton(onClick = { onNextClicked() }) {
                Image(
                    painter = painterResource(id = R.drawable.ic_next),
                    contentDescription = "Next Track",
                    modifier = Modifier.size((24 * scaleFactor).dp) // Increase the size
                )
            }
        }

        // Slider for Music Progress
        Slider(
            value = if (trackDuration > 0) currentPosition / trackDuration else 0f,
            onValueChange = onSliderChanged,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = (32 * scaleFactor).dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = (32 * scaleFactor).dp), // Adjust horizontal padding to shrink the slider
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = TimeManager.formatTime(currentPosition.toInt() / 1000))
            Text(text = TimeManager.formatTime(trackDuration.toInt() / 1000))
        }
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
            songName = "Lofi Storms",
            songImage = R.drawable.lofi_storms_image,
            onPlayPauseClicked = {},
            onNextClicked = {},
            onPreviousClicked = {},
            onSliderChanged = {}
        )
    }
}
