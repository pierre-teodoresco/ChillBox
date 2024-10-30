package com.example.chillbox.ui.components

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun BackButton(
    navController: NavController,
    exitTask: () -> Unit = {},
    scaleFactor: Float,
    modifier: Modifier = Modifier
) {
    // IconButton with an arrow back icon
    IconButton(
        onClick = {
            exitTask()
            navController.popBackStack()
        },
        modifier = modifier.size((48 * scaleFactor).dp)
    ) {
        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
    }
}