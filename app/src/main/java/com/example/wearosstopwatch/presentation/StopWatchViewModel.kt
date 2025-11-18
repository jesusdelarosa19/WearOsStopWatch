
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

enum class TimerState {
    RUNNING, PAUSED, RESET
}

class StopWatchViewModel : ViewModel() {

    private val _elapsedTime = MutableStateFlow(0L)
    val elapsedTime: StateFlow<Long> = _elapsedTime.asStateFlow()

    private val _timerState = MutableStateFlow(TimerState.RESET)
    val timerState: StateFlow<TimerState> = _timerState.asStateFlow()

    private val formatter = DateTimeFormatter.ofPattern("HH:mm:ss:SS")
    val stopwatchText = MutableStateFlow("00:00:00:00")

    init {
        viewModelScope.launch {
            elapsedTime.collect { time ->
                val localTime = LocalTime.ofNanoOfDay(time * 1_000_000)
                stopwatchText.value = formatter.format(localTime)
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
            while (timerState.value == TimerState.RUNNING) {
                delay(10L)
                val now = System.currentTimeMillis()
                _elapsedTime.value += (now - lastTime)
                lastTime = now
            }
        }
    }

    fun resetTimer() {
        _timerState.update { TimerState.RESET }
        _elapsedTime.value = 0L
    }
}
