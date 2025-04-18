package com.example.chillbox.ui.colorpicker

import com.example.chillbox.ui.components.CustomSlider
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.chillbox.ui.components.BackButton

@Composable
fun ColorPickerScreen(
    viewModel: ColorPickerViewModel = viewModel(),
    navController: NavController
) {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp

    // Define scaling factor based on screen width (e.g., tablets or large devices)
    val scaleFactor = when {
        screenWidthDp < 400 -> 0.7f
        screenWidthDp < 700 -> 0.9f
        else -> 1.2f
    }

    // State
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding((16 * scaleFactor).dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Reusable BackButton from the BackButton.kt file
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopStart) {
            BackButton(navController = navController, scaleFactor = scaleFactor)
        }

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // Target Color Display
                Text(
                    "Match this color",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontSize = (21 * scaleFactor).sp
                    )
                )
                Spacer(modifier = Modifier.height((16 * scaleFactor).dp))
                Box(
                    modifier = Modifier
                        .size((100 * scaleFactor).dp)
                        .background(
                            color = Color(uiState.targetColor.first, uiState.targetColor.second, uiState.targetColor.third),
                            shape = CircleShape
                        )
                )
            }

            Spacer(modifier = Modifier.width((64 * scaleFactor).dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // User Color Display
                Text(
                    "Your color",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontSize = (21 * scaleFactor).sp
                    )
                )
                Spacer(modifier = Modifier.height((16 * scaleFactor).dp))
                Box(
                    modifier = Modifier
                        .size((100 * scaleFactor).dp)
                        .background(
                            color = Color(uiState.userColor.first, uiState.userColor.second, uiState.userColor.third),
                            shape = CircleShape
                        )
                )
            }
        }

        if (!uiState.isSuccess) {

            // RGB Sliders with Colored Circles
            SliderWithColorCircle(
                color = Color.Red,
                value = uiState.userColor.first,
                onValueChange = { viewModel.updateUserColor(it, uiState.userColor.second, uiState.userColor.third) },
                scaleFactor = scaleFactor
            )
            SliderWithColorCircle(
                color = Color.Green,
                value = uiState.userColor.second,
                onValueChange = { viewModel.updateUserColor(uiState.userColor.first, it, uiState.userColor.third) },
                scaleFactor = scaleFactor
            )
            SliderWithColorCircle(
                color = Color.Blue,
                value = uiState.userColor.third,
                onValueChange = { viewModel.updateUserColor(uiState.userColor.first, uiState.userColor.second, it) },
                scaleFactor = scaleFactor
            )

            Spacer(modifier = Modifier.height((42 * scaleFactor).dp))

            // Reset Button
            Button(
                onClick = { viewModel.generateNewColor() },
                modifier = Modifier
                    .width((150 * scaleFactor).dp)
                    .height((64 * scaleFactor).dp)
            ) {
                Text(
                    "Reset",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = (21 * scaleFactor).sp
                    ),
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        } else {
            Spacer(modifier = Modifier.height((128 * scaleFactor).dp))

            Text(
                "Success! You've matched the color!",
                color = Color.Black,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = (29 * scaleFactor).sp
                )
            )
            Spacer(modifier = Modifier.height((128 * scaleFactor).dp))
            Button(
                onClick = { viewModel.generateNewColor() },
                modifier = Modifier
                    .width((200 * scaleFactor).dp)
                    .height((64 * scaleFactor).dp)
            ) {
                Text(
                    "Play Again",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = (21 * scaleFactor).sp
                    ),
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}

@Composable
fun SliderWithColorCircle(
    color: Color, value: Int,
    onValueChange: (Int) -> Unit,
    scaleFactor: Float = 1.0f
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(vertical = (16 * scaleFactor).dp)
    ) {
        // Color circle label
        Box(
            modifier = Modifier
                .size((32 * scaleFactor).dp)
                .background(color = color, shape = CircleShape)
        )

        Spacer(modifier = Modifier.height((8 * scaleFactor).dp))

        // Slider with value label
        Text(
            "$value",
            style = MaterialTheme.typography.labelLarge.copy(
                fontSize = (21 * scaleFactor).sp
            )
        )

        CustomSlider(
            value = value.toFloat(),
            onValueChange = { onValueChange(it.toInt()) },
            valueRange = 0f..255f,
            lineColor = color,
            thumbColor = color,
            thumbRadius = (16 * scaleFactor).dp,
            lineHeight = (13 * scaleFactor).dp,
            modifier = Modifier
                .width((300 * scaleFactor).dp)
                .padding((16 * scaleFactor).dp)
        )
    }
}

