package com.example.chillbox.ui.ambiance

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.chillbox.R
import com.example.chillbox.utils.Ring
import kotlinx.coroutines.delay

class AmbianceViewModel : ViewModel() {
    // Define a state to track the current image index
    private val _currentImage = mutableStateOf(0)
    val currentImage: State<Int> get() = _currentImage

    // Define sets of rings for each image
    val ringsSets = listOf(
        listOf(
            Ring(Color.Red, 0.52f, 0.2f, 0.19f, 0.27f, R.raw.temple),
            Ring(Color.Yellow, 0.22f, 0.35f, 0.16f, 0.27f, R.raw.waterfall),
            Ring(Color.Green, 0.35f, 0.18f, 0.16f, 0.27f, R.raw.forest),
            Ring(Color.Cyan, 0.20f, 0.75f, 0.3f, 0.16f, R.raw.beach),
            Ring(Color.Black, 0.515f, 0.87f, 0.1f, 0.1f, R.raw.twinkle_sound)
        ),
        listOf(
            Ring(Color.Blue, 0.4f, 0.7f, 0.35f, 0.15f, R.raw.love_and_thunder),
            Ring(Color.White, 0.42f, 0.17f, 0.12f, 0.1f, R.raw.forest),
            Ring(Color.Gray, 0.17f, 0.52f, 0.2f, 0.1f, R.raw.waterfall),
            Ring(Color.Red, 0.08f, 0.75f, 0.17f, 0.17f, R.raw.twinkle_sound)
        )
    )

    private var mediaPlayer: MediaPlayer? = null
    val rings: List<Ring>
        get() = ringsSets[currentImage.value]

    fun switchImage() {
        // Move to the next image and loop back to the first if needed
        _currentImage.value = (_currentImage.value + 1) % ringsSets.size
        stopSound()
    }

    fun playSound(context: Context, soundResId: Int) {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(context, soundResId).apply {
            isLooping = true
            start()
        }
    }

    fun stopSound() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

    override fun onCleared() {
        super.onCleared()
        stopSound()
    }
}
