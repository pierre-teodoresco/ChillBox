package com.example.chillbox.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
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
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chillbox.R
import com.example.chillbox.model.PomodoroSessionType
import com.example.chillbox.ui.components.BackButton
import com.example.chillbox.ui.theme.ChillBoxTheme
import com.example.chillbox.viewmodel.PomodoroViewModel

class PomodoroActivity : ComponentActivity() {

    private val pomodoroViewModel: PomodoroViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChillBoxTheme {
                PomodoroScreen(pomodoroViewModel)
            }
        }
    }
}

@Composable
fun PomodoroScreen(viewModel: PomodoroViewModel = PomodoroViewModel()) {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp

    // Define scaling factor based on screen width (e.g., tablets or large devices)
    val scaleFactor = if (screenWidthDp > 600) 2.0f else 1.0f

    // Get the current session type and timer value
    val currentSession by viewModel.currentSession.observeAsState(PomodoroSessionType.Work)
    val timerValue by viewModel.timerValue.observeAsState("00:00")
    val isTimerRunning by viewModel.isTimerRunning.observeAsState(false)
    val workSessionCount by viewModel.workSessionCount.observeAsState(0)

    // Background color based on session type
    val backgroundColor = when (currentSession) {
        PomodoroSessionType.Work -> MaterialTheme.colorScheme.tertiary
        else -> MaterialTheme.colorScheme.surfaceVariant
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
            BackButton(scaleFactor = scaleFactor)
        }

        Spacer(modifier = Modifier.height((275 * scaleFactor).dp))

        // Session Type Display (Work Session, Rest Session, Long Rest Session)
        Text(
            text = when (currentSession) {
                PomodoroSessionType.Work -> "Work Session"
                PomodoroSessionType.Rest -> "Rest Session"
                PomodoroSessionType.LongRest -> "Long Rest Session"
                else -> "Work Session"
            },
            style = MaterialTheme.typography.titleLarge.copy(fontSize = (25 * scaleFactor).sp)
        )

        Spacer(modifier = Modifier.height((16 * scaleFactor).dp))

        // Timer Display (Minutes:Seconds)
        Text(
            text = timerValue, // Display timer value as MM:SS
            style = MaterialTheme.typography.titleLarge.copy(fontSize = (25 * scaleFactor).sp),
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height((16 * scaleFactor).dp))

        // Progress Indicator
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            for (i in 0 until 5) {
                val color = if (i < workSessionCount.coerceAtMost(5)) {
                    Color.Black
                } else {
                    Color.White
                }
                Spacer(
                    modifier = Modifier
                        .height((8 * scaleFactor).dp)
                        .width((20 * scaleFactor).dp)
                        .background(color)
                        .padding((4 * scaleFactor).dp)
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
            if (isTimerRunning) {
                IconButton (
                    onClick = { viewModel.pauseTimer() }, // Pause the timer
                    modifier = Modifier.padding((8 * scaleFactor).dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_pause),
                        contentDescription = "Pause",
                        modifier = Modifier.size((24 * scaleFactor).dp)
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
                        modifier = Modifier.size((24 * scaleFactor).dp)
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
                    modifier = Modifier.size((24 * scaleFactor).dp)
                )
            }

            // Skip button
            IconButton(
                onClick = { viewModel.skipSession() }, // Skip the current session
                modifier = Modifier.padding((8 * scaleFactor).dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_next),
                    contentDescription = "Skip",
                    modifier = Modifier.size((24 * scaleFactor).dp)
                )
            }
        }

        Spacer(modifier = Modifier.height((16 * scaleFactor).dp))

        // Sliders for Work Session Length and Rest Session Length
        Column {
            // Work Session Length
            Text(
                text = "Work Session Length: ${viewModel.workSessionLength} minutes",
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = (16 * scaleFactor).sp)
            )
            Slider(
                value = viewModel.workSessionLength,
                onValueChange = { viewModel.workSessionLength = it.toInt().toFloat() },
                valueRange = 1f..60f,
                steps = 59,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height((16 * scaleFactor).dp))

            // Short Rest Session Length
            Text(
                text = "Rest Session Length: ${viewModel.shortRestSessionLength} minutes",
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = (16 * scaleFactor).sp)
            )
            Slider(
                value = viewModel.shortRestSessionLength,
                onValueChange = { viewModel.shortRestSessionLength = it.toInt().toFloat() },
                valueRange = 1f..60f,
                steps = 59,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height((16 * scaleFactor).dp))

            // Long Rest Session Length
            Text(
                text = "Long Rest Session Length: ${viewModel.longRestSessionLength} minutes",
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = (16 * scaleFactor).sp)
            )
            Slider(
                value = viewModel.longRestSessionLength,
                onValueChange = { viewModel.longRestSessionLength = it.toInt().toFloat() },
                valueRange = 1f..60f,
                steps = 59,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun SessionLengthSlider(
    label: String,
    length: Float,
    onValueChange: (Float) -> Unit,
    scaleFactor: Float
) {
    Text(
        text = "$label: ${length.toInt()} minutes",
        style = MaterialTheme.typography.bodyMedium.copy(fontSize = (16 * scaleFactor).sp)
    )
    Slider(
        value = length,
        onValueChange = onValueChange,
        valueRange = 1f..60f,
        modifier = Modifier.padding(horizontal = (16 * scaleFactor).dp)
    )

}

@Preview(showBackground = true)
@Composable
fun PomodoroScreenPreview() {
    ChillBoxTheme {
        PomodoroScreen()
    }
}
