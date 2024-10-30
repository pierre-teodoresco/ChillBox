package com.example.chillbox.ui.colorpicker

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.random.Random

class ColorPickerViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ColorPickerUiState())
    val uiState: StateFlow<ColorPickerUiState> = _uiState

    private val successThreshold = 25 // 10% threshold, tuneable as needed

    init {
        generateNewColor()
    }

    fun generateNewColor() {
        val targetColor = Triple(Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))
        _uiState.value = _uiState.value.copy(targetColor = targetColor, isSuccess = false)
    }

    fun updateUserColor(red: Int, green: Int, blue: Int) {
        _uiState.value = _uiState.value.copy(userColor = Triple(red, green, blue))
        checkForSuccess()
    }

    private fun checkForSuccess() {
        val (targetRed, targetGreen, targetBlue) = _uiState.value.targetColor
        val (userRed, userGreen, userBlue) = _uiState.value.userColor

        val redDiff = kotlin.math.abs(targetRed - userRed)
        val greenDiff = kotlin.math.abs(targetGreen - userGreen)
        val blueDiff = kotlin.math.abs(targetBlue - userBlue)

        val isSuccess = redDiff <= successThreshold &&
                greenDiff <= successThreshold &&
                blueDiff <= successThreshold

        if (isSuccess) {
            _uiState.value = _uiState.value.copy(isSuccess = true)
        }
    }
}
