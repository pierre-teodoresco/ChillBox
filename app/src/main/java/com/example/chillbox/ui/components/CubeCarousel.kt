package com.example.chillbox.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun CubeCarousel(
    items: List<@Composable () -> Unit>,
    scaleFactor: Float = 1f
) {
    val pagerState = rememberPagerState(pageCount = { items.size })
    val coroutineScope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxWidth()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height((500 * scaleFactor).dp)
        ) { page ->
            val rotation by animateFloatAsState(
                targetValue = if (pagerState.currentPage == page) 0f else -75f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                ),
                label = "rotation"
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding((16 * scaleFactor).dp)
                    .graphicsLayer {
                        rotationY = rotation // Apply Y-axis rotation
                        cameraDistance = 12f * density
                    },
                contentAlignment = Alignment.Center
            ) {
                // Display the item content
                items[page]()
            }
        }

        // Add left arrow
        IconButton(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding((16 * scaleFactor).dp),
            onClick = { // Trigger callback
                val previousPage = (pagerState.currentPage - 1).coerceAtLeast(0)
                // Scroll to the previous page
                coroutineScope.launch {
                    pagerState.animateScrollToPage(previousPage)
                }
            }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Previous"
            )
        }

        // Add right arrow
        IconButton(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding((16 * scaleFactor).dp),
            onClick = { // Trigger callback
                val nextPage = (pagerState.currentPage + 1).coerceAtMost(items.size - 1)
                // Scroll to the next page
                coroutineScope.launch {
                    pagerState.animateScrollToPage(nextPage)
                }
            }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Next"
            )
        }
    }
}
