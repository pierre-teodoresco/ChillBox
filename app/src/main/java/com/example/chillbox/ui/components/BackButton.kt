package com.example.chillbox.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun BackButton(
    navController: NavController,
    exitTask: () -> Unit = {},
    scaleFactor: Float,
    modifier: Modifier = Modifier
) {
    var isButtonEnabled by remember { mutableStateOf(true) }
    val hapticFeedback = LocalHapticFeedback.current

    // IconButton with an arrow back icon
    IconButton(
        onClick = {
            if (isButtonEnabled) {
                hapticFeedback.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                isButtonEnabled = false
                exitTask()
                navController.popBackStack()
            }
        },
        modifier = modifier.size((48 * scaleFactor).dp)
    ) {
        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
    }

    // Re-enable the button after a delay using LaunchedEffect
    LaunchedEffect(isButtonEnabled) {
        if (!isButtonEnabled) {
            kotlinx.coroutines.delay(1000) // 1s debounce
            isButtonEnabled = true
        }
    }
}