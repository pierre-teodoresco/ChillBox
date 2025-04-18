package com.example.chillbox.ui.pomodoro

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.chillbox.R
import com.example.chillbox.ui.components.BackButton
import com.example.chillbox.ui.components.CustomSlider

@Composable
fun PomodoroScreen(
    viewModel: PomodoroViewModel = viewModel(),
    navController: NavController
) {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp

    // Define scaling factor based on screen width (e.g., tablets or large devices)
    val scaleFactor = when {
        screenWidthDp < 400 -> 0.65f
        screenWidthDp < 700 -> 1.0f
        else -> 1.2f
    }

    // State
    val state by viewModel.state.collectAsState()

    // Background color based on session type
    val backgroundColor = when (state.currentSession) {
        PomodoroSessionType.Work -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.tertiary
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding((16 * scaleFactor).dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Reusable BackButton from the BackButton.kt file
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopStart) {
            BackButton(navController = navController, scaleFactor = scaleFactor)
        }

        Spacer(modifier = Modifier.height((32 * scaleFactor).dp))

        // Session Type Display (Work Session, Rest Session, Long Rest Session)
        Text(
            text = when (state.currentSession) {
                PomodoroSessionType.Work -> "Work Session"
                PomodoroSessionType.Rest -> "Rest Session"
                PomodoroSessionType.LongRest -> "Long Rest Session"
            },
            style = MaterialTheme.typography.bodyLarge
        )

        // Timer Display (Minutes:Seconds)
        Text(
            text = state.timerValue, // Display timer value as MM:SS
            style = MaterialTheme.typography.titleLarge.copy(fontSize = (64 * scaleFactor).sp),
            fontFamily = FontFamily(Font(R.font.pacifico_regular)),

            )

        Spacer(modifier = Modifier.height((24 * scaleFactor).dp))

        // Progress Indicator
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            for (i in 0 until 5) {
                val color = if (i < state.workSessionCount.coerceAtMost(5)) {
                    Color.Black
                } else {
                    Color.White
                }
                Spacer(
                    modifier = Modifier
                        .height((8 * scaleFactor).dp)
                        .width((32 * scaleFactor).dp)
                        .background(color)
                )
            }
        }

        Spacer(modifier = Modifier.height((24 * scaleFactor).dp))

        // Control Buttons: Play/Pause and Reset
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Play or Pause button based on timer state
            if (state.isTimerRunning) {
                IconButton (
                    onClick = { viewModel.pauseTimer() }, // Pause the timer
                    modifier = Modifier.padding((8 * scaleFactor).dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_pause),
                        contentDescription = "Pause",
                        modifier = Modifier.size((32 * scaleFactor).dp)
                    )
                }
            } else {
                IconButton(
                    onClick = { viewModel.startTimer() }, // Start the timer
                    modifier = Modifier.padding((8 * scaleFactor).dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_play),
                        contentDescription = "Play",
                        modifier = Modifier.size((32 * scaleFactor).dp)
                    )
                }
            }

            // Reset button
            IconButton(
                onClick = { viewModel.resetTimer() }, // Reset the timer to initial session value
                modifier = Modifier.padding((8 * scaleFactor).dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_reset),
                    contentDescription = "Reset",
                    modifier = Modifier.size((32 * scaleFactor).dp)
                )
            }

            // Skip button
            IconButton(
                onClick = { viewModel.skipSession(false) }, // Skip the current session
                modifier = Modifier.padding((8 * scaleFactor).dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_next),
                    contentDescription = "Skip",
                    modifier = Modifier.size((32 * scaleFactor).dp)
                )
            }
        }

        Spacer(modifier = Modifier.height((32 * scaleFactor).dp))

        // Sliders for Work Session Length and Rest Session Length
        Column {
            // Work Session Length
            SessionLengthSlider(
                label = "Work",
                length = state.workSessionLength,
                onValueChange = { viewModel.setWorkSessionLength(it.toInt()) },
                scaleFactor = scaleFactor,
                currentSessionType = state.currentSession // Pass the current session type
            )

            Spacer(modifier = Modifier.height((48 * scaleFactor).dp))

            // Short Rest Session Length
            SessionLengthSlider(
                label = "Short Rest",
                length = state.shortRestSessionLength,
                onValueChange = { viewModel.setShortRestSessionLength(it.toInt()) },
                scaleFactor = scaleFactor,
                currentSessionType = state.currentSession // Pass the current session type
            )

            Spacer(modifier = Modifier.height((48 * scaleFactor).dp))

            // Long Rest Session Length
            SessionLengthSlider(
                label = "Long Rest",
                length = state.longRestSessionLength,
                onValueChange = { viewModel.setLongRestSessionLength(it.toInt()) },
                scaleFactor = scaleFactor,
                currentSessionType = state.currentSession // Pass the current session type
            )
        }
    }
}

@Composable
fun SessionLengthSlider(
    label: String,
    length: Int,
    onValueChange: (Float) -> Unit,
    scaleFactor: Float,
    currentSessionType: PomodoroSessionType // Add current session type parameter
) {
    // Determine the color scheme based on the current session type
    val lineColor = when (currentSessionType) {
        PomodoroSessionType.Work -> MaterialTheme.colorScheme.tertiary
        else -> MaterialTheme.colorScheme.primary
    }
    val thumbColor = when (currentSessionType) {
        PomodoroSessionType.Work -> MaterialTheme.colorScheme.tertiary
        else -> MaterialTheme.colorScheme.primary
    }

    Text(
        text = "$label: $length minutes",
        style = MaterialTheme.typography.bodyMedium.copy(fontSize = (24 * scaleFactor).sp),
        modifier = Modifier.padding(horizontal = (16 * scaleFactor).dp)
    )
    CustomSlider(
        value = length.toFloat(),
        onValueChange = { it: Float -> onValueChange(it) },
        valueRange = 5f..60f,
        lineColor = lineColor,
        thumbColor = thumbColor,
        thumbRadius = (16 * scaleFactor).dp,
        lineHeight = (13 * scaleFactor).dp,
        modifier = Modifier
            .width((300 * scaleFactor).dp)
            .padding((16 * scaleFactor).dp)
    )
}
