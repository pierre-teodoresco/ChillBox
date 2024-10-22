package com.example.chillbox.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.chillbox.model.PomodoroSessionType
import com.example.chillbox.model.TimeManager

class PomodoroViewModel : ViewModel() {

    // Customization attributes (now using Float for flexibility)
    var workSessionLength = 0.1f // 0.1 minutes = 6 seconds (for testing)
    var shortRestSessionLength = 0.1f // 0.1 minutes = 6 seconds
    var longRestSessionLength = 0.2f // 0.5 minutes = 12 seconds

    private var currentTimeInMillis = workSessionLength * 60 * 1000 // Default work session in milliseconds

    private var timerJob: Job? = null

    // LiveData to track session type and timer value
    val currentSession = MutableLiveData<PomodoroSessionType>().apply { value = PomodoroSessionType.Work }
    val timerValue = MutableLiveData("00:00") // Default work session value
    val isTimerRunning = MutableLiveData(false)
    val workSessionCount = MutableLiveData(0) // Track work session count0

    // Initialize timer display
    init {
        updateTimerDisplay()
    }

    // Start the timer
    fun startTimer() {
        isTimerRunning.value = true
        timerJob = viewModelScope.launch {
            while (currentTimeInMillis > 0 && isTimerRunning.value == true) {
                delay(1000L) // Delay for 1 second
                currentTimeInMillis -= 1000L
                updateTimerDisplay()
            }
            if (currentTimeInMillis <= 0L) {
                switchSession()
            }
        }
    }

    // Pause the timer
    fun pauseTimer() {
        isTimerRunning.value = false
        timerJob?.cancel()
    }

    // Reset the timer to initial value based on current session
    fun resetTimer() {
        pauseTimer()
        currentSession.value = PomodoroSessionType.Work
        workSessionCount.value = 0
        isTimerRunning.value = false
        currentTimeInMillis = workSessionLength * 60 * 1000
        updateTimerDisplay()
    }

    // Skip the current session
    fun skipSession() {
        pauseTimer()
        switchSession()
        updateTimerDisplay()
    }

    // Switch between sessions
    private fun switchSession() {
        // Safely increment the work session count only when in Work session
        workSessionCount.value = if (currentSession.value == PomodoroSessionType.Work) {
            (workSessionCount.value ?: 0) + 1
        } else {
            workSessionCount.value ?: 0
        }

        // Determine the next session based on the current session
        currentSession.value = when (currentSession.value) {
            PomodoroSessionType.Work -> if ((workSessionCount.value ?: 0) % 5 == 0) {
                PomodoroSessionType.LongRest
            } else {
                PomodoroSessionType.Rest
            }
            PomodoroSessionType.Rest, PomodoroSessionType.LongRest -> PomodoroSessionType.Work
            else -> PomodoroSessionType.Work
        }

        // Update the timer value based on the next session
        currentTimeInMillis = when (currentSession.value) {
            PomodoroSessionType.Work -> (workSessionLength * 60 * 1000)
            PomodoroSessionType.Rest -> (shortRestSessionLength * 60 * 1000)
            PomodoroSessionType.LongRest -> (longRestSessionLength * 60 * 1000)
            null -> (workSessionLength * 60 * 1000)
        }
        startTimer() // Automatically start next session
    }

    // Update timer display in MM:SS format
    private fun updateTimerDisplay() {
        timerValue.value = TimeManager.formatTime(currentTimeInMillis / 1000)
    }
}
