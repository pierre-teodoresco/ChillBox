package com.example.chillbox.viewmodel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chillbox.model.PomodoroSessionType

class PomodoroViewModel : ViewModel() {

    // LiveData for current session type (Work, Rest, Long Rest)
    val currentSession = MutableLiveData<PomodoroSessionType>()

    // Timer value as LiveData
    val timerValue = MutableLiveData("20:00")

    // Timer running state
    val isTimerRunning = MutableLiveData(false)

    // User preferences
    val isSoundEnabled = MutableLiveData(true)
    val isNotificationEnabled = MutableLiveData(true)

    var workDuration = 20
    var restDuration = 5
    var longRestDuration = 30

    // Start the timer
    fun startTimer() {
        // Implement timer logic here
        isTimerRunning.value = true
    }

    // Pause the timer
    fun pauseTimer() {
        isTimerRunning.value = false
    }

    // Reset the timer
    fun resetTimer() {
        // Logic to reset timer to initial value
    }

    // Toggle sound
    fun toggleSound() {
        isSoundEnabled.value = isSoundEnabled.value?.not()
    }

    // Toggle notifications
    fun toggleNotification() {
        isNotificationEnabled.value = isNotificationEnabled.value?.not()
    }

    // Update work duration via slider
    fun updateWorkDuration(duration: Int) {
        workDuration = duration
    }

    // Update rest duration via slider
    fun updateRestDuration(duration: Int) {
        restDuration = duration
    }

    // Update long rest duration via slider
    fun updateLongRestDuration(duration: Int) {
        longRestDuration = duration
    }
}
