package com.example.chillbox.ui

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalConfiguration
import com.example.chillbox.R
import com.example.chillbox.model.TimeManager
import com.example.chillbox.ui.components.BackButton
import com.example.chillbox.ui.theme.ChillBoxTheme
import com.example.chillbox.viewmodel.LofiRadioViewModel

class LofiRadioActivity : ComponentActivity() {

    private val lofiRadioViewModel: LofiRadioViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChillBoxTheme {
                LofiRadioScreen(viewModel = lofiRadioViewModel, context = this)
            }
        }
        lofiRadioViewModel.setupMediaPlayer(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        lofiRadioViewModel.destroyMediaPlayer()
    }
}

@Composable
fun LofiRadioScreen(viewModel: LofiRadioViewModel = LofiRadioViewModel(), context: Context) {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp
    // Define scaling factor based on screen width (e.g., tablets or large devices)
    val scaleFactor = if (screenWidthDp > 600) 2.0f else 1.0f

    val currentTrack = viewModel.currentTrack
    val songImage = viewModel.songImages[currentTrack]
    val songName = viewModel.songNames[currentTrack]

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
            IconButton(onClick = { viewModel.playPreviousTrack(context) }) {
                Image(
                    painter = painterResource(id = R.drawable.ic_previous),
                    contentDescription = "Previous Track",
                    modifier = Modifier.size((24 * scaleFactor).dp) // Increase the size
                )
            }

            IconButton(onClick = { viewModel.togglePlayPause() }) {
                Image(
                    painter = painterResource(
                        id = if (viewModel.isPlaying) R.drawable.ic_pause else R.drawable.ic_play
                    ),
                    contentDescription = "Play/Pause",
                    modifier = Modifier.size((24 * scaleFactor).dp) // Increase the size
                )
            }

            IconButton(onClick = { viewModel.playNextTrack(context) }) {
                Image(
                    painter = painterResource(id = R.drawable.ic_next),
                    contentDescription = "Next Track",
                    modifier = Modifier.size((24 * scaleFactor).dp) // Increase the size
                )
            }
        }

        // Slider for Music Progress
        Slider(
            value = if (viewModel.trackDuration > 0) viewModel.currentPosition / viewModel.trackDuration else 0f,
            onValueChange = { viewModel.seekTo(it) },
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
            Text(text = TimeManager.formatTime(viewModel.currentPosition.toInt() / 1000))
            Text(text = TimeManager.formatTime(viewModel.trackDuration.toInt() / 1000))
        }
    }
}