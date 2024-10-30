package com.example.chillbox.model

data class PomodoroState(
    val currentSession: PomodoroSessionType = PomodoroSessionType.Work,
    val timerValue: String = "20:00",
    val isTimerRunning: Boolean = false,
    val workSessionCount: Int = 0,
    val workSessionLength: Int = 20,
    val shortRestSessionLength: Int = 5,
    val longRestSessionLength: Int = 30,
)
