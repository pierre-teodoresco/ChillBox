package com.example.chillbox.ui.colorpicker

data class ColorPickerUiState(
    val targetColor: Triple<Int, Int, Int> = Triple(0, 0, 0),  // RGB values
    val userColor: Triple<Int, Int, Int> = Triple(0, 0, 0),
    val isSuccess: Boolean = false
)
