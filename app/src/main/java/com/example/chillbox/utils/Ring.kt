package com.example.chillbox.utils

import androidx.compose.ui.graphics.Color

data class Ring(
    val color: Color,
    val mainRingXPercentage: Float,
    val mainRingYPercentage: Float,
    val mainRingWidthPercentage: Float,
    val mainRingHeightPercentage: Float,
    val soundResId: Int
)
