
package com.example.wearosstopwatch.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class TimerViewModel : ViewModel() {

    private val _duration = MutableStateFlow(10000L) // 10 seconds default

    private val _timeLeft = MutableStateFlow(_duration.value)
    val timeLeft: StateFlow<Long> = _timeLeft.asStateFlow()

    private val _timerState = MutableStateFlow(TimerState.RESET)
    val timerState: StateFlow<TimerState> = _timerState.asStateFlow()

    private val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
    val timerText = MutableStateFlow("00:00:10")

    init {
        viewModelScope.launch {
            timeLeft.collect { time ->
                val localTime = LocalTime.ofNanoOfDay(time * 1_000_000)
                timerText.value = formatter.format(localTime)
            }
        }
    }

    fun toggleIsRunning() {
        when (timerState.value) {
            TimerState.RUNNING -> _timerState.update { TimerState.PAUSED }
            TimerState.PAUSED,
            TimerState.RESET -> _timerState.update { TimerState.RUNNING }
        }

        viewModelScope.launch {
            var lastTime = System.currentTimeMillis()
            while (timerState.value == TimerState.RUNNING && _timeLeft.value > 0) {
                delay(10L)
                val now = System.currentTimeMillis()
                _timeLeft.value -= (now - lastTime)
                lastTime = now
            }
            if (_timeLeft.value <= 0) {
                _timerState.update { TimerState.RESET }
                _timeLeft.value = _duration.value
            }
        }
    }

    fun resetTimer() {
        _timerState.update { TimerState.RESET }
        _timeLeft.value = _duration.value
    }
}
