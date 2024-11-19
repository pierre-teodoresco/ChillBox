
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
        // TODO
    }

    private fun getYouTubeVideos(): List<VideoItem> {
        // TODO
        return List(0) { VideoItem("", "", "") }
    }
}
