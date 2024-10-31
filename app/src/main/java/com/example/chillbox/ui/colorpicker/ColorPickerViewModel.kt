package com.example.chillbox.ui.colorpicker

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random

class ColorPickerViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ColorPickerUiState())
    val uiState: StateFlow<ColorPickerUiState> = _uiState

    private val successThreshold = 255*3 / 15 // 15% threshold, tuneable as needed

    init {
        generateNewColor()
    }

    fun generateNewColor() {
        val targetColor = Triple(Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))
        _uiState.update {
            it.copy(
                targetColor = targetColor,
                isSuccess = false,
                userColor = Triple(0, 0, 0)
            )
        }
    }

    fun updateUserColor(red: Int, green: Int, blue: Int) {
        _uiState.update { it.copy(userColor = Triple(red, green, blue)) }
        checkForSuccess()
    }

    private fun checkForSuccess() {
        val (targetRed, targetGreen, targetBlue) = _uiState.value.targetColor
        val (userRed, userGreen, userBlue) = _uiState.value.userColor

        val redDiff = kotlin.math.abs(targetRed - userRed)
        val greenDiff = kotlin.math.abs(targetGreen - userGreen)
        val blueDiff = kotlin.math.abs(targetBlue - userBlue)

        val isSuccess = redDiff + greenDiff + blueDiff <= successThreshold

        if (isSuccess) {
            _uiState.update { it.copy(isSuccess = true) }
        }
    }
}
