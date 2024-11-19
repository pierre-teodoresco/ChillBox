
package com.example.chillbox.ui.cutevideos

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil3.compose.rememberAsyncImagePainter

@Composable
fun CuteVideosScreen(
    viewModel: CuteVideosViewModel = viewModel(),
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.isLoading) {
        CircularProgressIndicator(modifier = Modifier.fillMaxSize())
    } else if (uiState.errorMessage != null) {
        Text(text = "Error: ${uiState.errorMessage}", modifier = Modifier.padding(16.dp))
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(uiState.videoList.size) { index ->
                val video = uiState.videoList[index]
                Column(modifier = Modifier.padding(vertical = 8.dp)) {
                    Image(
                        painter = rememberAsyncImagePainter(video.thumbnailUrl),
                        contentDescription = video.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )
                    Text(text = video.title, modifier = Modifier.padding(8.dp))
                }
            }
        }
    }
}
