
package com.example.wearosstopwatch.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.example.wearosstopwatch.presentation.theme.WearOsStopWatchTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            WearApp()
        }
    }
}

@Composable
fun WearApp() {
    WearOsStopWatchTheme {
        val navController = rememberSwipeDismissableNavController()
        SwipeDismissableNavHost(
            navController = navController,
            startDestination = "menu"
        ) {
            composable("menu") {
                MenuScreen(navController = navController)
            }
            composable("stopwatch") {
                val viewModel: StopWatchViewModel = viewModel()
                StopwatchScreen(viewModel = viewModel)
            }
            composable("timer") {
                TimerScreen()
            }
        }
    }
}
