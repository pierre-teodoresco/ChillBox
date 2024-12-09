
package com.example.chillbox.ui.cutevideos

import android.content.Context
import android.net.Uri
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.chillbox.ui.components.BackButton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun CuteVideosScreen(
    viewModel: CuteVideosViewModel = viewModel(),
    navController: NavController
) {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp

    // Define scaling factor based on screen width (e.g., tablets or large devices)
    val scaleFactor = if (screenWidthDp > 600) 2.0f else 1.0f

    val context = LocalContext.current

    // Fetch the video list
    val videos by viewModel.videoList.collectAsState()

    // Keep a reference to the ViewPager2 Adapter
    val videoPagerAdapter = remember { VideoPagerAdapter(videos, context) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding((16 * scaleFactor).dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Reusable BackButton from the BackButton.kt file
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopStart) {
            BackButton(navController = navController, scaleFactor = scaleFactor)
        }

        AndroidView(
            factory = { ctx ->
                ViewPager2(ctx).apply {
                    orientation = ViewPager2.ORIENTATION_VERTICAL
                    adapter = videoPagerAdapter
                    offscreenPageLimit = 1 // Preload only the next video

                    // Handle page change events to control playback
                    registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                        override fun onPageSelected(position: Int) {
                            super.onPageSelected(position)
                            (adapter as? VideoPagerAdapter)?.onPageSelected(position)
                        }
                    })
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    }

    // Clean up resources when the screen is disposed
    DisposableEffect(Unit) {
        onDispose {
            videoPagerAdapter.releaseAllPlayers()
        }
    }
}


class CuteVideosViewModel : ViewModel() {
    private val _videoList = MutableStateFlow(
        listOf(
            "android.resource://com.example.chillbox/raw/cat1",
            "android.resource://com.example.chillbox/raw/cat2",
            "android.resource://com.example.chillbox/raw/cat3",
            "android.resource://com.example.chillbox/raw/cat4",
        )
    )
    val videoList: StateFlow<List<String>> = _videoList
}

class VideoPagerAdapter(private val videoUrls: List<String>, private val context: Context) :
    RecyclerView.Adapter<VideoPagerAdapter.VideoViewHolder>() {

    private var currentPlayingPosition: Int = RecyclerView.NO_POSITION
    private val activePlayers = mutableListOf<ExoPlayer>()

    class VideoViewHolder(val playerView: PlayerView) : RecyclerView.ViewHolder(playerView) {
        private var player: ExoPlayer? = null

        fun bind(videoUrl: String, play: Boolean) {
            player = ExoPlayer.Builder(playerView.context).build().apply {
                setMediaItem(MediaItem.fromUri(Uri.parse(videoUrl)))
                prepare()
                playWhenReady = play
            }
            playerView.player = player
        }

        fun pause() {
            player?.playWhenReady = false
        }

        fun release() {
            player?.release()
            player = null
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val playerView = PlayerView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
        return VideoViewHolder(playerView)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val shouldPlay = position == currentPlayingPosition
        holder.bind(videoUrls[position], shouldPlay)
        activePlayers.add(holder.playerView.player as ExoPlayer)
    }

    override fun onViewRecycled(holder: VideoViewHolder) {
        super.onViewRecycled(holder)
        holder.release()
        activePlayers.remove(holder.playerView.player as ExoPlayer)
    }

    override fun getItemCount(): Int = videoUrls.size

    fun onPageSelected(position: Int) {
        // Pause the previously playing video
        if (currentPlayingPosition != RecyclerView.NO_POSITION) {
            notifyItemChanged(currentPlayingPosition)
        }

        // Start the newly selected video
        currentPlayingPosition = position
        notifyItemChanged(position)
    }

    // Release all players
    fun releaseAllPlayers() {
        activePlayers.forEach { it.release() }
        activePlayers.clear()
    }
}
