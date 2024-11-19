
package com.example.chillbox.ui.cutevideos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class CuteVideosViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CuteVideosUiState())
    val uiState: StateFlow<CuteVideosUiState> = _uiState

    private val apiKey = "AIzaSyBaZGMKtXo9xTRI-U4KaRUYAfcV7mTbzwc"
    private val searchQuery = "cute animals"
    private val maxResults = 10

    init {
        fetchCuteVideos()
    }

    private fun fetchCuteVideos() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val videos = getYouTubeVideos()
                _uiState.update { it.copy(videoList = videos, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }

    private fun getYouTubeVideos(): List<VideoItem> {
        val url = "https://www.googleapis.com/youtube/v3/search?part=snippet&type=video&videoDuration=short" +
                  "&q=$searchQuery&maxResults=$maxResults&key=$apiKey"
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.requestMethod = "GET"

        val response = connection.inputStream.bufferedReader().use { it.readText() }
        val jsonResponse = JSONObject(response)
        val items = jsonResponse.getJSONArray("items")

        val videoList = mutableListOf<VideoItem>()
        for (i in 0 until items.length()) {
            val item = items.getJSONObject(i)
            val videoId = item.getJSONObject("id").getString("videoId")
            val snippet = item.getJSONObject("snippet")
            val title = snippet.getString("title")
            val thumbnailUrl = snippet.getJSONObject("thumbnails").getJSONObject("medium").getString("url")
            videoList.add(VideoItem(videoId, title, thumbnailUrl))
        }

        println("videoList: $videoList")

        return videoList
    }
}
