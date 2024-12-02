package com.example.chillbox.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.example.chillbox.R

val CustomTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily(
            Font(R.font.pacifico_regular)
        ),
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily(
            Font(R.font.pacifico_regular)
        ),
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    labelLarge = TextStyle(
        fontFamily = FontFamily(
            Font(R.font.pacifico_regular)
        ),
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp
    ),
    bodySmall = TextStyle(
        fontFamily = FontFamily(
            Font(R.font.pacifico_regular)
        ),
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
)
