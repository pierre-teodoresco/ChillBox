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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.chillbox.R

@Composable
fun HomeScreen(navController: NavController) {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp

    // Define scaling factor based on screen width (e.g., tablets or large devices)
    val scaleFactor = if (screenWidthDp > 600) 2.0f else 1.0f

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
            scaleFactor = scaleFactor
        )

        // Cards section with scaling
        CardsGrid(scaleFactor = scaleFactor, navController = navController)

        // Footer with default size
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
            modifier = Modifier.size((80 * scaleFactor).dp) // Scaled logo size
        )
        Text(
            text = stringResource(id = R.string.username),
            style = MaterialTheme.typography.titleLarge.copy(fontSize = (22 * scaleFactor).sp), // Scaled text size
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
fun CardsGrid(scaleFactor: Float, navController: NavController) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .padding((30 * scaleFactor).dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy((70 * scaleFactor).dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CardItem(R.drawable.lofi_radio, R.string.lofi_radio, scaleFactor) {
                navController.navigate("lofi_radio")
            }
            CardItem(R.drawable.pomodoro, R.string.pomodoro, scaleFactor) {
                navController.navigate("pomodoro")
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CardItem(R.drawable.game, R.string.game, scaleFactor) {
               navController.navigate("game")
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CardItem(R.drawable.ambiance, R.string.ambiance, scaleFactor) {
                navController.navigate("ambiance")
            }
            CardItem(R.drawable.cute_videos, R.string.cute_videos, scaleFactor) {
                navController.navigate("cute_videos")
            }
        }
    }
}

@Composable
fun CardItem(imageRes: Int, textRes: Int, scaleFactor: Float, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .size((120 * scaleFactor).dp)
            .clickable { onClick() },
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
                    .size((80 * scaleFactor).dp)
                    .align(Alignment.CenterHorizontally) // Ensure the image is centered
                    .padding((8 * scaleFactor).dp)
            )
            Text(
                text = stringResource(id = textRes),
                fontSize = (14 * scaleFactor).sp,
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
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
