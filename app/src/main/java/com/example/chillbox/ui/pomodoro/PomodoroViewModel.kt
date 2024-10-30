package com.example.chillbox.ui.pomodoro

import androidx.lifecycle.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.chillbox.utils.TimeManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PomodoroViewModel : ViewModel() {
    private val _state = MutableStateFlow(PomodoroUiState())
    val state: StateFlow<PomodoroUiState> = _state.asStateFlow()

    private var currentTimeInSeconds = _state.value.workSessionLength * 60
    private lateinit var timerJob: Job

    // Set the work session length
    fun setWorkSessionLength(minutes: Int) {
        _state.update { it.copy(workSessionLength = minutes) }
        if (_state.value.currentSession == PomodoroSessionType.Work) {
            currentTimeInSeconds = minutes * 60
            updateTimerDisplay()
        }
    }

    // Set the short rest session length
    fun setShortRestSessionLength(minutes: Int) {
        _state.update { it.copy(shortRestSessionLength = minutes) }
        if (_state.value.currentSession == PomodoroSessionType.Rest) {
            currentTimeInSeconds = minutes * 60
            updateTimerDisplay()
        }
    }

    // Set the long rest session length
    fun setLongRestSessionLength(minutes: Int) {
        _state.update { it.copy(longRestSessionLength = minutes) }
        if (_state.value.currentSession == PomodoroSessionType.LongRest) {
            currentTimeInSeconds = minutes * 60
            updateTimerDisplay()
        }
    }

    // Start the timer
    fun startTimer() {
        updateTimerDisplay()
        _state.update { it.copy(isTimerRunning = true) }
        timerJob = viewModelScope.launch {
            while (currentTimeInSeconds > 0 && _state.value.isTimerRunning) {
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
        _state.update { it.copy(isTimerRunning = false) }
        timerJob.cancel()
    }

    // Reset the timer to the first work session
    fun resetTimer() {
        pauseTimer()
        _state.update { it.copy(
            currentSession = PomodoroSessionType.Work,
            workSessionCount = 0,
            isTimerRunning = false
        ) }
        currentTimeInSeconds = _state.value.workSessionLength * 60
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
        if (_state.value.currentSession == PomodoroSessionType.Work) {
            val updatedScore = _state.value.workSessionCount.inc()
            _state.update { it.copy(workSessionCount = updatedScore) }
        }

        // Reset work session count when switching to Long Rest
        if (_state.value.currentSession == PomodoroSessionType.LongRest) {
            _state.update { it.copy(workSessionCount = 0) }
        }

        // Determine the next session based on the current session
        _state.update {
            it.copy(currentSession =
                when (_state.value.currentSession) {
                    PomodoroSessionType.Work ->
                        if (_state.value.workSessionCount % 5 == 0) {
                            PomodoroSessionType.LongRest
                        } else {
                            PomodoroSessionType.Rest
                        }
                    PomodoroSessionType.Rest, PomodoroSessionType.LongRest -> PomodoroSessionType.Work
                }
            )
        }

        // Update the timer value based on the next session
        currentTimeInSeconds =
            when (_state.value.currentSession) {
                PomodoroSessionType.Work -> _state.value.workSessionLength
                PomodoroSessionType.Rest -> _state.value.shortRestSessionLength
                PomodoroSessionType.LongRest -> _state.value.longRestSessionLength
            } * 60

        startTimer() // Automatically start next session
    }

    // Update timer display in MM:SS format
    private fun updateTimerDisplay() {
        _state.update { it.copy(timerValue = TimeManager.formatTime(currentTimeInSeconds)) }
    }
}
