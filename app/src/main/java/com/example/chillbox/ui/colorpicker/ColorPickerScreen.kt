package com.example.chillbox.ui.colorpicker

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
    val scaleFactor = if (screenWidthDp > 600) 2.0f else 1.0f

    // State
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Reusable BackButton from the BackButton.kt file
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopStart) {
            BackButton(navController = navController, scaleFactor = scaleFactor)
        }

        // Target Color Display
        Text("Match this color", style = MaterialTheme.typography.titleMedium)
        Box(
            modifier = Modifier
                .size((100 * scaleFactor).dp)
                .background(
                    color = Color(uiState.targetColor.first, uiState.targetColor.second, uiState.targetColor.third),
                    shape = CircleShape
                )
        )

        Spacer(modifier = Modifier.height((32 * scaleFactor).dp))

        // User Color Display
        Text("Your color", style = MaterialTheme.typography.titleMedium)
        Box(
            modifier = Modifier
                .size((100 * scaleFactor).dp)
                .background(
                    color = Color(uiState.userColor.first, uiState.userColor.second, uiState.userColor.third),
                    shape = CircleShape
                )
        )

        Spacer(modifier = Modifier.height((32 * scaleFactor).dp))

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

        Spacer(modifier = Modifier.height((32 * scaleFactor).dp))

        // Result message
        if (uiState.isSuccess) {
            Text(
                "Success! You've matched the color!",
                color = Color.Green,
                style = MaterialTheme.typography.titleMedium
            )
            Button(onClick = { viewModel.generateNewColor() }) {
                Text("Play Again")
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
                .size((24 * scaleFactor).dp)
                .background(color = color, shape = CircleShape)
        )

        Spacer(modifier = Modifier.height((8 * scaleFactor).dp))

        // Slider with value label
        Text("$value", style = MaterialTheme.typography.labelLarge)
        Slider(
            value = value.toFloat(),
            onValueChange = { onValueChange(it.toInt()) },
            valueRange = 0f..255f,
            modifier = Modifier.width(250.dp)
        )
    }
}

