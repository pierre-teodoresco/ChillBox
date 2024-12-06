package com.example.chillbox.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun CubeCarousel(
    items: List<@Composable () -> Unit>,
    scaleFactor: Float = 1f
) {
    val pagerState = rememberPagerState(pageCount = { items.size })
    val coroutineScope = rememberCoroutineScope()

    val hapticFeedback = LocalHapticFeedback.current

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        // Add left arrow
        IconButton(
            modifier = Modifier
                .padding((16 * scaleFactor).dp),
            onClick = { // Trigger callback
                hapticFeedback.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)

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

        Text(
            text = "Swipe to see more",
            // Use custom typography style
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding((17 * scaleFactor).dp),
        )

        // Add right arrow
        IconButton(
            modifier = Modifier
                .padding((16 * scaleFactor).dp),
            onClick = { // Trigger callback
                hapticFeedback.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)

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
    }

    // Page Indicator Slider
    PageIndicator(
        pageCount = items.size,
        currentPage = pagerState.currentPage,
        scaleFactor = scaleFactor
    ) { selectedPage ->
        coroutineScope.launch {
            pagerState.animateScrollToPage(selectedPage)
        }
    }
}

@Composable
fun PageIndicator(
    pageCount: Int,
    currentPage: Int,
    scaleFactor: Float,
    onPageSelected: (Int) -> Unit
) {
    val hapticFeedback = LocalHapticFeedback.current

    Row(
        modifier = Modifier
            .padding(top = (8 * scaleFactor).dp)
            .clipToBounds(),
        horizontalArrangement = Arrangement.Center
    ) {
        for (i in 0 until pageCount) {
            val isSelected = i == currentPage
            val size: Dp = (30 * scaleFactor).dp
            val color: Color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray

            Box(
                modifier = Modifier
                    .size(size)
                    .padding(horizontal = (4 * scaleFactor).dp)
                    .background(color = color, shape = MaterialTheme.shapes.small)
                    .clickable {
                        hapticFeedback.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                        onPageSelected(i)
                    }
            )
        }
    }
}
