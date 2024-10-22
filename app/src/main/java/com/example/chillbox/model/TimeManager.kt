package com.example.chillbox.model

import android.annotation.SuppressLint

class TimeManager {
    companion object {
        @SuppressLint("DefaultLocale")
        fun formatTime(seconds: Long): String {
            val minutes = seconds / 60
            val remainingSeconds = seconds % 60
            return String.format("%02d:%02d", minutes, remainingSeconds)
        }
    }
}