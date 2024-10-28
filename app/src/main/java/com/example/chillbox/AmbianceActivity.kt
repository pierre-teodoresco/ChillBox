package com.example.chillbox

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
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

        // Placeholder text for Ambiance Screen
        Text(text = "Ambiance Screen", modifier = Modifier.padding(top = (16 * scaleFactor).dp))

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

    Image(
        painter = painterResource(id = imageRes),
        contentDescription = "Island with interactive regions",
        modifier = modifier.pointerInput(Unit) {
            detectTapGestures { offset ->
                val clickedRegion = detectClickedRegion(offset)

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
                }
            }
        }
    )
}

// Helper function to detect the clicked region
fun detectClickedRegion(offset: Offset): String {
    // Define specific areas for each interactive region based on the image dimensions
    return when {
        offset.x in 100f..200f && offset.y in 150f..250f -> "temple"      // Adjust coordinates
        offset.x in 50f..150f && offset.y in 300f..400f -> "waterfall"   // Adjust coordinates
        offset.x in 200f..300f && offset.y in 350f..450f -> "forest"      // Adjust coordinates
        offset.x in 300f..400f && offset.y in 450f..550f -> "beach"       // Adjust coordinates
        offset.x in 350f..450f && offset.y in 500f..600f -> "boat"        // Adjust coordinates
        // Else debug message
        else -> ""
    }
}

// Helper function to play a sound
fun playSound(context: Context, mediaPlayer: MutableState<MediaPlayer?>, soundResId: Int) {
    mediaPlayer.value?.release()  // Release any existing MediaPlayer instance
    mediaPlayer.value = MediaPlayer.create(context, soundResId)
    mediaPlayer.value?.start()
}

@Preview(showBackground = true)
@Composable
fun AmbianceScreenPreview() {
    ChillBoxTheme {
        AmbianceScreen()
    }
}
