package com.example.chillbox.ui.lofiradio

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
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.chillbox.R
import com.example.chillbox.utils.TimeManager
import com.example.chillbox.ui.components.BackButton

@Composable
fun LofiRadioScreen(
    navController: NavController,
    viewModel: LofiRadioViewModel = viewModel(),
) {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp
    // Define scaling factor based on screen width (e.g., tablets or large devices)
    val scaleFactor = if (screenWidthDp > 600) 2.0f else 1.0f

    // Context
    val context = LocalContext.current

    // State
    val uiState by viewModel.state.collectAsState()

    // Initialize the MediaPlayer on entering the screen
    LaunchedEffect(Unit) {
        viewModel.initMediaPlayer(context)
    }

    // Dispose of MediaPlayer when exiting the screen
    DisposableEffect(Unit) {
        onDispose {
            viewModel.stop()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding((16 * scaleFactor).dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Back button to home screen
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopStart) {
            BackButton(
                navController = navController,
                scaleFactor = scaleFactor
            )
        }

        Spacer(modifier = Modifier.height((16 * scaleFactor).dp))

        // Song Image with top and bottom padding and rounded corners
        Image(
            painter = painterResource(id = viewModel.getTrackName()),
            contentDescription = "Song Image",
            modifier = Modifier
                .size((500 * scaleFactor).dp) // Use appropriate size
        )

        Spacer(modifier = Modifier.height((16 * scaleFactor).dp))

        // Song Name with padding
        Text(
            text = viewModel.getTrackTitle(),
            style = MaterialTheme.typography.titleLarge.copy(fontSize = (25 * scaleFactor).sp),
            modifier = Modifier.padding(vertical = (8 * scaleFactor).dp) // Adjust the padding values as needed
        )

        Spacer(modifier = Modifier.height((16 * scaleFactor).dp))

        // Music Player Controls
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = { viewModel.previous(context) }) {
                Image(
                    painter = painterResource(id = R.drawable.ic_previous),
                    contentDescription = "Previous Track",
                    modifier = Modifier.size((24 * scaleFactor).dp) // Increase the size
                )
            }

            IconButton(onClick = { if (uiState.isPlaying) viewModel.pause() else viewModel.play() }) {
                Image(
                    painter = painterResource(
                        id = if (uiState.isPlaying) R.drawable.ic_pause else R.drawable.ic_play
                    ),
                    contentDescription = "Play/Pause",
                    modifier = Modifier.size((24 * scaleFactor).dp) // Increase the size
                )
            }

            IconButton(onClick = { viewModel.next(context) }) {
                Image(
                    painter = painterResource(id = R.drawable.ic_next),
                    contentDescription = "Next Track",
                    modifier = Modifier.size((24 * scaleFactor).dp) // Increase the size
                )
            }
        }

        // Slider for Music Progress
        Slider(
            value = uiState.progress.toFloat(),
            onValueChange = { viewModel.onSeek(it.toInt()) },
            valueRange = 0f..(uiState.currentTrackDuration.toFloat()),
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
            Text(text = TimeManager.formatTime(uiState.progress / 1000))
            Text(text = TimeManager.formatTime(uiState.currentTrackDuration / 1000))
        }
    }
}