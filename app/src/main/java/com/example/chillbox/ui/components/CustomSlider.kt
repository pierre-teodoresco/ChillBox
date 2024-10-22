package com.example.chillbox.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp

@Composable
fun CustomSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Canvas(modifier = Modifier.fillMaxWidth().height(48.dp)) {
            val trackHeight = 16.dp.toPx() // Increase the track height
            val thumbRadius = 20.dp.toPx() // Increase the thumb radius
            val trackWidth = size.width
            val activeTrackWidth = trackWidth * value

            // Draw inactive track
            drawRoundRect(
                color = Color.LightGray,
                topLeft = Offset(0f, (size.height - trackHeight) / 2),
                size = Size(trackWidth, trackHeight),
                cornerRadius = CornerRadius(trackHeight / 2, trackHeight / 2)
            )

            // Draw active track
            drawRoundRect(
                color = Color(0xFF66BB6A),
                topLeft = Offset(0f, (size.height - trackHeight) / 2),
                size = Size(activeTrackWidth, trackHeight),
                cornerRadius = CornerRadius(trackHeight / 2, trackHeight / 2)
            )

            // Draw thumb
            drawCircle(
                color = Color.Gray,
                radius = thumbRadius,
                center = Offset(activeTrackWidth, size.height / 2)
            )
        }

        // Handle touch events to update the slider value
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        val newValue = offset.x / size.width
                        onValueChange(newValue)
                    }
                }
        )
    }
}