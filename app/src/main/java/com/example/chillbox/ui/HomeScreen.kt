package com.example.chillbox.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.chillbox.R
import com.example.chillbox.ui.components.CubeCarousel

@Composable
fun HomeScreen(navController: NavController) {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp

    // Define scaling factor based on screen width (e.g., tablets or large devices)
    val scaleFactor = when {
        screenWidthDp < 400 -> 0.6f
        screenWidthDp < 700 -> 0.8f
        else -> 1.2f
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header with scaled content
        Header(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primary),
            scaleFactor = 1.2f*scaleFactor
        )

        CubeCarousel(
            items = listOf(
                {
                    CardItem(R.drawable.lofi_radio, R.string.lofi_radio, 2*scaleFactor) {
                        navController.navigate("lofi_radio")
                    }
                },
                {
                    CardItem(R.drawable.pomodoro, R.string.pomodoro, 2*scaleFactor) {
                        navController.navigate("pomodoro")
                    }
                },
                {
                    CardItem(R.drawable.game, R.string.game, 2*scaleFactor) {
                        navController.navigate("color_picker")
                    }
                },
                {
                    CardItem(R.drawable.ambiance, R.string.ambiance, 2*scaleFactor) {
                        navController.navigate("ambiance")
                    }
                },
                {
                    CardItem(R.drawable.cute_videos, R.string.cute_videos, 2*scaleFactor) {
                        navController.navigate("cute_videos")
                    }
                }
            ),
            scaleFactor = scaleFactor
        )

        Footer(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primary),
            scaleFactor = scaleFactor
        )
    }
}

@Composable
fun Header(modifier: Modifier = Modifier, scaleFactor: Float) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = (16 * scaleFactor).dp, vertical = (8 * scaleFactor).dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_chillbox_logo),
            contentDescription = stringResource(id = R.string.app_logo),
            modifier = Modifier.size((80 * scaleFactor).dp)
        )

        Text(
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.labelLarge.copy(
                fontSize = (42 * scaleFactor).sp // Adjust the font size using scaleFactor
            ),
            color = MaterialTheme.colorScheme.secondary
        )
    }
}

@Composable
fun CardItem(imageRes: Int, textRes: Int, scaleFactor: Float, onClick: () -> Unit) {
    // Get the haptic feedback instance
    val hapticFeedback = LocalHapticFeedback.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height((300 * scaleFactor).dp) // Adjusted height for better scaling
            .clickable {
                hapticFeedback.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                onClick()
            },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = (8 * scaleFactor).dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .padding((8 * scaleFactor).dp)
                .fillMaxSize(), // Ensure the column fills the card
            horizontalAlignment = Alignment.CenterHorizontally, // Center content horizontally
            verticalArrangement = Arrangement.Center // Center content vertically within the card
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = stringResource(id = textRes),
                modifier = Modifier
                    .fillMaxSize(0.70f)
                    .align(Alignment.CenterHorizontally) // Ensure the image is centered
                    .padding((8 * scaleFactor).dp)
            )
            Text(
                text = stringResource(id = textRes),
                fontSize = (18 * scaleFactor).sp, // Adjusted font size for better scaling
                fontWeight = FontWeight.Medium,
                modifier = Modifier.align(Alignment.CenterHorizontally) // Ensure text is centered
            )
        }
    }
}

@Composable
fun Footer(modifier: Modifier = Modifier, scaleFactor: Float) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding((16 * scaleFactor).dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(id = R.string.footer),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = (42 * scaleFactor).sp // Adjust the font size using scaleFactor
            ),
            color = MaterialTheme.colorScheme.secondary
        )
    }
}
