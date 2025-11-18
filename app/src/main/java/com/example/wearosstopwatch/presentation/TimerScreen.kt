
package com.example.wearosstopwatch.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText

@Composable
fun TimerScreen(viewModel: TimerViewModel = viewModel()) {
    val timerState by viewModel.timerState.collectAsState()
    val timerText by viewModel.timerText.collectAsState()

    Scaffold(
        timeText = { TimeText() }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = timerText,
                fontSize = 30.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.onBackground
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(onClick = { viewModel.toggleIsRunning() }) {
                    Icon(
                        imageVector = if (timerState == TimerState.RUNNING) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (timerState == TimerState.RUNNING) "Pause" else "Start"
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = { viewModel.resetTimer() },
                    enabled = timerState != TimerState.RESET && timerState != TimerState.RUNNING
                ) {
                    Icon(
                        imageVector = Icons.Default.Stop,
                        contentDescription = "Reset"
                    )
                }
            }
        }
    }
}
