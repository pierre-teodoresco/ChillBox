package com.example.chillbox.ui.ambiance

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.chillbox.R
import com.example.chillbox.utils.Ring

class AmbianceViewModel : ViewModel() {
    private var mediaPlayer: MediaPlayer? = null
    val rings = listOf(
        Ring(Color.Red, 0.52f, 0.2f, 0.19f, 0.27f, R.raw.temple),
        Ring(Color.Yellow, 0.22f, 0.35f, 0.16f, 0.27f, R.raw.waterfall),
        Ring(Color.Green, 0.35f, 0.18f, 0.16f, 0.27f, R.raw.forest),
        Ring(Color.Cyan, 0.20f, 0.75f, 0.3f, 0.16f, R.raw.beach),
        Ring(Color.Black, 0.515f, 0.87f, 0.1f, 0.1f, R.raw.twinkle_sound)
    )

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
