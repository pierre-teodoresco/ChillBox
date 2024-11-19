package com.example.chillbox

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chillbox.ui.ambiance.AmbianceScreen
import com.example.chillbox.ui.cutevideos.CuteVideosScreen
import com.example.chillbox.ui.HomeScreen
import com.example.chillbox.ui.colorpicker.ColorPickerScreen
import com.example.chillbox.ui.lofiradio.LofiRadioScreen
import com.example.chillbox.ui.pomodoro.PomodoroScreen
import com.example.chillbox.ui.theme.ChillBoxTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChillBoxTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController = navController) }
        composable("lofi_radio") { LofiRadioScreen(navController = navController) }
        composable("pomodoro") { PomodoroScreen(navController = navController) }
        composable("color_picker") { ColorPickerScreen(navController = navController) }
        composable("ambiance") { AmbianceScreen(navController = navController) }
        composable("cute_videos") { CuteVideosScreen(navController = navController) }
    }
}