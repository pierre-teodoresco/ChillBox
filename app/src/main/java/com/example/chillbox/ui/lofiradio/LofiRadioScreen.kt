package com.example.chillbox.ui.lofiradio

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.example.chillbox.ui.components.CustomSlider

@Composable
fun LofiRadioScreen(
    navController: NavController,
    viewModel: LofiRadioViewModel = viewModel(),
) {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp

    // Define scaling factor based on screen width (e.g., tablets or large devices)
    val scaleFactor = when {
        screenWidthDp < 400 -> 0.6f
        screenWidthDp < 700 -> 0.8f
        else -> 1.2f
    }

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
            .background(MaterialTheme.colorScheme.tertiary)
            .padding(
                start = (10 * scaleFactor).dp,
                top = (64 * scaleFactor).dp,
                end = (10 * scaleFactor).dp,
                bottom = (64 * scaleFactor).dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Back button to home screen
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding((16 * scaleFactor).dp),
            contentAlignment = Alignment.TopStart
        ) {
            BackButton(
                navController = navController,
                scaleFactor = 0.7f*scaleFactor
            )
        }

        // Song Image with top and bottom padding and rounded corners
        Image(
            painter = painterResource(id = viewModel.getTrackName()),
            contentDescription = "Song Image",
            modifier = Modifier
                .size((500 * scaleFactor).dp) // Use appropriate size
        )

        // Song Name with padding
        Text(
            text = viewModel.getTrackTitle(),
            style = MaterialTheme.typography.labelLarge.copy(
                fontSize = (42 * scaleFactor).sp // Adjust the font size using scaleFactor
            ),
            modifier = Modifier.padding(vertical = (2 * scaleFactor).dp) // Adjust the padding values as needed
        )

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

        CustomSlider(
            value = uiState.progress.toFloat(),
            onValueChange = { viewModel.onSeek(it.toInt()) },
            valueRange = 0f..(uiState.currentTrackDuration.toFloat()),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = (64 * scaleFactor).dp, vertical = (16 * scaleFactor).dp),
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = (32 * scaleFactor).dp), // Adjust horizontal padding to shrink the slider
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = TimeManager.formatTime(uiState.progress / 1000),
                fontSize = (32 * scaleFactor).sp
            )
            Text(
                text = TimeManager.formatTime(uiState.currentTrackDuration / 1000),
                fontSize = (32 * scaleFactor).sp
            )
        }
    }
}