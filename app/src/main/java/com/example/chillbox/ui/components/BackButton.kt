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

@Composable
fun BackButton(scaleFactor: Float) {
    // Get the current activity context to call finish()
    val context = LocalContext.current as ComponentActivity

    // IconButton with an arrow back icon
    IconButton(
        onClick = { context.finish() },
        modifier = Modifier.size((48 * scaleFactor).dp)
    ) {
        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
    }
}