package com.example.chillbox.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CustomSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    lineColor: Color = MaterialTheme.colorScheme.primary,
    thumbColor: Color = MaterialTheme.colorScheme.primary,
    lineHeight: Dp = 8.dp,
    thumbRadius: Dp = 12.dp,
    modifier: Modifier
) {
    val sliderPosition = remember { mutableFloatStateOf(value) }
    val widthInPx = remember { mutableIntStateOf(0) }
    val isDragInProgress = remember { mutableStateOf(false) }

    // Update the slider position when the external value changes smoothly
    LaunchedEffect(value) {
        if (!isDragInProgress.value) {
            sliderPosition.floatValue = value
        }
    }

    Box(
        modifier = modifier
            .onSizeChanged {
                widthInPx.intValue = it.width
            }
            .background(Color.Transparent)
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragStart = {
                        isDragInProgress.value = true
                    },
                    onDragEnd = {
                        isDragInProgress.value = false
                    }
                ) { _, dragAmount ->
                    val newValue = (sliderPosition.floatValue + dragAmount / widthInPx.intValue * (valueRange.endInclusive - valueRange.start))
                        .coerceIn(valueRange.start, valueRange.endInclusive)
                    sliderPosition.floatValue = newValue
                    onValueChange(newValue)
                }
            }
            .pointerInteropFilter { event ->
                if (event.action == android.view.MotionEvent.ACTION_DOWN || event.action == android.view.MotionEvent.ACTION_MOVE) {
                    val touchX = event.x.coerceIn(0f, widthInPx.intValue.toFloat())
                    val newValue = (touchX / widthInPx.intValue) * (valueRange.endInclusive - valueRange.start) + valueRange.start
                    sliderPosition.floatValue = newValue
                    onValueChange(newValue)
                    true
                } else {
                    false
                }
            }
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val thumbCenterOffset = ((sliderPosition.floatValue - valueRange.start) / (valueRange.endInclusive - valueRange.start)) * size.width

            // Draw filled part of the slider line
            drawRoundRect(
                color = lineColor,
                topLeft = Offset(0f, (size.height - lineHeight.toPx()) / 2),
                size = androidx.compose.ui.geometry.Size(
                    width = thumbCenterOffset,
                    height = lineHeight.toPx()
                ),
                cornerRadius = CornerRadius(lineHeight.toPx() / 2, lineHeight.toPx() / 2)
            )

            // Draw unfilled part of the slider line
            drawRoundRect(
                color = Color.LightGray,
                topLeft = Offset(thumbCenterOffset, (size.height - lineHeight.toPx()) / 2),
                size = androidx.compose.ui.geometry.Size(
                    width = size.width - thumbCenterOffset,
                    height = lineHeight.toPx()
                ),
                cornerRadius = CornerRadius(lineHeight.toPx() / 2, lineHeight.toPx() / 2)
            )

            // Draw thumb circle
            drawCircle(
                color = thumbColor,
                radius = thumbRadius.toPx(),
                center = Offset(thumbCenterOffset, size.height / 2)
            )
        }
    }
}
