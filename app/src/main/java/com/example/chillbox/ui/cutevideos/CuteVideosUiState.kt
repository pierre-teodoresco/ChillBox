
package com.example.chillbox.ui.cutevideos

data class CuteVideosUiState(
    val videoList: List<VideoItem> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

data class VideoItem(
    val videoId: String,
    val title: String,
    val thumbnailUrl: String
)
