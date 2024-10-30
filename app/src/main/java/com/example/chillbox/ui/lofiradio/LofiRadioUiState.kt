package com.example.chillbox.ui.lofiradio

data class LofiRadioUiState(
    val isPlaying: Boolean = false,
    val currentTrack: Int = 0,
    val currentTrackDuration: Int = 0,
    val progress: Int = 0
)
