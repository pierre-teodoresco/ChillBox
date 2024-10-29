package com.example.chillbox

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.chillbox.ui.components.BackButton
import com.example.chillbox.ui.theme.ChillBoxTheme

class AmbianceActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChillBoxTheme {
                AmbianceScreen()
            }
        }
    }
}

@Composable
fun AmbianceScreen() {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp

    // Define scaling factor based on screen width (e.g., tablets or large devices)
    val scaleFactor = if (screenWidthDp > 600) 2.0f else 1.0f

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding((16 * scaleFactor).dp)
    ) {
        // Reusable BackButton from the BackButton.kt file
        BackButton(scaleFactor = scaleFactor)

        // Display the interactive image
        InteractiveImage(
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun InteractiveImage(modifier: Modifier = Modifier) {
    // This will hold the current image shown
    var imageRes by remember { mutableStateOf(R.drawable.island_true) }

    // Load sounds (ensure they are in res/raw folder)
    val mediaPlayer = remember { mutableStateOf<MediaPlayer?>(null) }
    val context = LocalContext.current

    // Debug flag to show interactive areas
    var showDebug by remember { mutableStateOf(true) }

    // Define interactive regions for each image
    val interactiveRegions = remember {
        mutableStateMapOf(
            R.drawable.island_true to listOf(
                Region("temple", 600f..775f, 500f..825f),
                Region("waterfall", 275f..400f, 725f..925f),
                Region("forest", 425f..550f, 500f..750f),
                Region("beach", 200f..500f, 1150f..1275f),
                Region("boat", 580f..675f, 1250f..1325f)
            ),
            R.drawable.mountain to listOf(
                Region("mountain_peak", 300f..400f, 100f..200f),
                Region("mountain_base", 200f..300f, 300f..400f)
            )
        )
    }

    Box(modifier = modifier) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = "Island with interactive regions",
            modifier = Modifier
                .fillMaxSize() // Make the image fill the available space
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        val clickedRegion = detectClickedRegion(offset, interactiveRegions[imageRes] ?: emptyList())

                        // Play the corresponding sound or change the image
                        when (clickedRegion) {
                            "temple" -> {
                                playSound(context, mediaPlayer, R.raw.temple)
                            }
                            "waterfall" -> {
                                playSound(context, mediaPlayer, R.raw.waterfall)
                            }
                            "forest" -> {
                                playSound(context, mediaPlayer, R.raw.forest)
                            }
                            "beach" -> {
                                playSound(context, mediaPlayer, R.raw.beach)
                            }
                            "boat" -> {
                                imageRes = R.drawable.mountain // Replace with the ID of the new image
                            }
                            "mountain_peak" -> {
                                playSound(context, mediaPlayer, R.raw.forest)
                            }
                            "mountain_base" -> {
                                imageRes = R.drawable.island_true
                            }
                        }
                    }
                }
        )

        if (showDebug) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawInteractiveAreas(interactiveRegions[imageRes] ?: emptyList())
            }
        }
    }
}

// Helper function to detect the clicked region
fun detectClickedRegion(offset: Offset, regions: List<Region>): String {
    // Define specific areas for each interactive region based on the image dimensions
    return regions.find { region ->
        offset.x in region.xRange && offset.y in region.yRange
    }?.name ?: ""
}

// Helper function to play a sound
fun playSound(context: Context, mediaPlayer: MutableState<MediaPlayer?>, soundResId: Int) {
    mediaPlayer.value?.release()  // Release any existing MediaPlayer instance
    mediaPlayer.value = MediaPlayer.create(context, soundResId)
    mediaPlayer.value?.isLooping = true  // Set looping to true
    mediaPlayer.value?.start()
}

// Draw interactive areas for debugging purposes
fun DrawScope.drawInteractiveAreas(regions: List<Region>) {
    regions.forEach { region ->
        drawRect(
            color = Color.Red,
            topLeft = Offset(region.xRange.start, region.yRange.start),
            size = androidx.compose.ui.geometry.Size(region.xRange.endInclusive - region.xRange.start, region.yRange.endInclusive - region.yRange.start),
            style = Stroke(width = 5f)
        )
    }
}

data class Region(val name: String, val xRange: ClosedFloatingPointRange<Float>, val yRange: ClosedFloatingPointRange<Float>)

@Preview(showBackground = true)
@Composable
fun AmbianceScreenPreview() {
    ChillBoxTheme {
        AmbianceScreen()
    }
}
