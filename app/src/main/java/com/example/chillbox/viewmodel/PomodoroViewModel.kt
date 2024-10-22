package com.example.chillbox.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.chillbox.model.PomodoroSessionType
import com.example.chillbox.model.TimeManager

class PomodoroViewModel : ViewModel() {

    // LiveData to track sessions length in minutes
    var workSessionLength = MutableLiveData(20)
    var shortRestSessionLength = MutableLiveData(5)
    var longRestSessionLength = MutableLiveData(30)

    // Default values
    private var defaultWorkSessionLength = 20*60

    private var currentTimeInSeconds = workSessionLength.value?.times(60) ?: defaultWorkSessionLength

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

    // Set the work session length
    fun setWorkSessionLength(minutes: Int) {
        workSessionLength.value = minutes
        if (currentSession.value == PomodoroSessionType.Work) {
            currentTimeInSeconds = minutes * 60
            updateTimerDisplay()
        }
    }

    // Set the short rest session length
    fun setShortRestSessionLength(minutes: Int) {
        shortRestSessionLength.value = minutes
        if (currentSession.value == PomodoroSessionType.Rest) {
            currentTimeInSeconds = minutes * 60
            updateTimerDisplay()
        }
    }

    // Set the long rest session length
    fun setLongRestSessionLength(minutes: Int) {
        longRestSessionLength.value = minutes
        if (currentSession.value == PomodoroSessionType.LongRest) {
            currentTimeInSeconds = minutes * 60
            updateTimerDisplay()
        }
    }

    // Start the timer
    fun startTimer() {
        updateTimerDisplay()
        isTimerRunning.value = true
        timerJob = viewModelScope.launch {
            while (currentTimeInSeconds > 0 && isTimerRunning.value == true) {
                delay(1000) // Delay for 1 second
                currentTimeInSeconds--
                updateTimerDisplay()
            }
            if (currentTimeInSeconds <= 0) {
                switchSession()
            }
        }
    }

    // Pause the timer
    fun pauseTimer() {
        isTimerRunning.value = false
        timerJob?.cancel()
    }

    // Reset the timer to the first work session
    fun resetTimer() {
        pauseTimer()
        currentSession.value = PomodoroSessionType.Work
        workSessionCount.value = 0
        isTimerRunning.value = false
        currentTimeInSeconds = workSessionLength.value?.times(60) ?: defaultWorkSessionLength
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
        // Increment the work session count only when in Work session
        if (currentSession.value == PomodoroSessionType.Work) {
            workSessionCount.value = (workSessionCount.value ?: 0) + 1
        }

        // Reset work session count when switching to Long Rest
        if (currentSession.value == PomodoroSessionType.LongRest) {
            workSessionCount.value = 0
        }

        // Determine the next session based on the current session
        currentSession.value = when (currentSession.value) {
            PomodoroSessionType.Work ->
                if ((workSessionCount.value ?: 0) % 5 == 0) {
                    PomodoroSessionType.LongRest
                } else {
                    PomodoroSessionType.Rest
                }
            PomodoroSessionType.Rest, PomodoroSessionType.LongRest -> PomodoroSessionType.Work
            else -> PomodoroSessionType.Work
        }

        // Update the timer value based on the next session
        currentTimeInSeconds =
            when (currentSession.value) {
                PomodoroSessionType.Work -> workSessionLength.value
                PomodoroSessionType.Rest -> shortRestSessionLength.value
                PomodoroSessionType.LongRest -> longRestSessionLength.value
                null -> workSessionLength.value
            }?.times(60) ?: defaultWorkSessionLength

        startTimer() // Automatically start next session
    }

    // Update timer display in MM:SS format
    private fun updateTimerDisplay() {
        timerValue.value = TimeManager.formatTime(currentTimeInSeconds)
    }
}
