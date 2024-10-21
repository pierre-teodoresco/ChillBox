package com.example.chillbox

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chillbox.ui.theme.ChillBoxTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChillBoxTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header with a distinct background color
        Header(modifier = Modifier.background(MaterialTheme.colorScheme.primary))

        // Cards section with its own background color
        CardsGrid(modifier = Modifier.background(MaterialTheme.colorScheme.background))

        // Footer with another distinct background color
        Footer(modifier = Modifier.background(MaterialTheme.colorScheme.primary))
    }
}

@Composable
fun Header(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 20.dp), // Reduced padding for a smaller header
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_chillbox_logo),
            contentDescription = stringResource(id = R.string.app_logo),
            modifier = Modifier.size(80.dp) // Optionally reduce logo size if needed
        )
        Text(
            text = stringResource(id = R.string.username),
            style = MaterialTheme.typography.titleMedium, // Use a slightly smaller font
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
fun CardsGrid(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .padding(30.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(50.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CardItem(R.drawable.lofi_radio, R.string.lofi_radio) { /* Navigate to Lofi Radio Activity */ }
            CardItem(R.drawable.pomodoro, R.string.pomodoro) { /* Navigate to Pomodoro Activity */ }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CardItem(R.drawable.game, R.string.game) { /* Navigate to Game Activity */ }
            CardItem(R.drawable.forum, R.string.forum) { /* Navigate to Forum Activity */ }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CardItem(R.drawable.ambiance, R.string.ambiance) { /* Navigate to Ambiance Activity */ }
            CardItem(R.drawable.cute_videos, R.string.cute_videos) { /* Navigate to Cute Videos Activity */ }
        }
    }
}

@Composable
fun CardItem(imageRes: Int, textRes: Int, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .size(120.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp), // Rounded corners for the card
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp // Elevation for shadow effect
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White // Background color of the card (change as per theme)
        )
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize(), // Ensure the column fills the card
            horizontalAlignment = Alignment.CenterHorizontally, // Center content horizontally
            verticalArrangement = Arrangement.Center // Center content vertically within the card
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = stringResource(id = textRes),
                modifier = Modifier
                    .size(80.dp)
                    .align(Alignment.CenterHorizontally) // Ensure the image is centered
                    .padding(8.dp)
            )
            Text(
                text = stringResource(id = textRes),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.CenterHorizontally) // Ensure text is centered
            )
        }
    }
}


@Composable
fun Footer(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(id = R.string.footer),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainMenuPreview() {
    ChillBoxTheme {
        MainScreen()
    }
}
