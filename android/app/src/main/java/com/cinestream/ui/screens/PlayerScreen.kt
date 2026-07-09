package com.cinestream.ui.screens

import android.content.pm.ActivityInfo
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.NavHostController
import com.cinestream.ui.viewmodel.PlayerViewModel
import com.cinestream.ui.viewmodel.PlayerState
import kotlinx.coroutines.delay
import androidx.compose.material.icons.rounded.VolumeOff

@Composable
fun PlayerScreen(
    navController: NavHostController,
    movieId: Int,
    mediaType: String,
    playerViewModel: PlayerViewModel? = null
) {
    val context = LocalContext.current
    val playerState by remember { mutableStateOf(PlayerState()) }
    val isLoading by remember { mutableStateOf(false) }
    val error by remember { mutableStateOf<String?>(null) }
    val videoSource by remember { mutableStateOf("") }
    
    var exoPlayer by remember { mutableStateOf<ExoPlayer?>(null) }
    var controlsVisible by remember { mutableStateOf(true) }
    var controlsVisibilityJob by remember { mutableStateOf<kotlinx.coroutines.Job?>(null) }
    
    LaunchedEffect(Unit) {
        exoPlayer = ExoPlayer.Builder(context).build()
    }
    
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer?.release()
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            }
            error != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.ErrorOutline,
                        contentDescription = "Error",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = error ?: "Playback error occurred",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { navController.popBackStack() }) {
                        Text("Go Back")
                    }
                }
            }
            videoSource.isNotEmpty() -> {
                // ExoPlayer View
                AndroidView(
                    factory = { context ->
                        FrameLayout(context).apply {
                            exoPlayer?.let { player ->
                                val playerView = androidx.media3.ui.PlayerView(context).apply {
                                    this.player = player
                                    useController = true
                                    controllerShowTimeoutMs = 5000
                                    controllerHideTimeoutMs = 5000
                                    shownControls = androidx.media3.ui.PlayerView.SHOW_ALL
                                    controllerLayoutParams = FrameLayout.LayoutParams(
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.MATCH_PARENT
                                    )
                                }
                                addView(playerView)
                                
                                // Load video
                                val mediaItem = MediaItem.Builder()
                                    .setUri(videoSource)
                                    .build()
                                player.setMediaItem(mediaItem)
                                player.prepare()
                            }
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
        
        // Custom UI Overlay (appears over player)
        if (controlsVisible) {
            PlayerControls(
                state = playerState,
                onPlayPauseClick = { playerViewModel?.togglePlayPause(playerState.isPlaying) },
                onFullscreenClick = { playerViewModel?.toggleFullscreen() },
                onBackClick = { navController.popBackStack() },
                onSeek = { position -> playerViewModel?.seekTo(position) },
                onQualityChange = { quality -> playerViewModel?.setQuality(quality) },
                modifier = Modifier.fillMaxSize()
            )
        }
        
        // Single tap to toggle controls
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    controlsVisible = !controlsVisible
                    
                    // Auto-hide controls after 5 seconds
                    controlsVisibilityJob?.cancel()
                    if (controlsVisible && playerState.isPlaying) {
                        controlsVisibilityJob = LaunchedEffect(Unit) {
                            delay(5000)
                            controlsVisible = false
                        }
                    }
                }
        )
    }
}

@Composable
private fun PlayerControls(
    state: PlayerState,
    onPlayPauseClick: () -> Unit,
    onFullscreenClick: () -> Unit,
    onBackClick: () -> Unit,
    onSeek: (Long) -> Unit,
    onQualityChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                        colors = listOf(Color.Black.copy(alpha = 0.6f), Color.Transparent)
                    )
                )
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
            
            Text(
                text = "Now Playing",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )
            
            var showQualityMenu by remember { mutableStateOf(false) }
            Box {
                IconButton(onClick = { showQualityMenu = !showQualityMenu }) {
                    Icon(
                        Icons.Default.Settings,
                        contentDescription = "Quality",
                        tint = Color.White
                    )
                }
                
                DropdownMenu(
                    expanded = showQualityMenu,
                    onDismissRequest = { showQualityMenu = false }
                ) {
                    state.availableQualities.forEach { quality ->
                        DropdownMenuItem(
                            text = { Text(quality) },
                            onClick = {
                                onQualityChange(quality)
                                showQualityMenu = false
                            }
                        )
                    }
                }
            }
        }
        
        // Center Play/Pause
        IconButton(
            onClick = onPlayPauseClick,
            modifier = Modifier
                .align(Alignment.Center)
                .size(64.dp)
        ) {
            Icon(
                if (state.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = if (state.isPlaying) "Pause" else "Play",
                tint = Color.White,
                modifier = Modifier.size(48.dp)
            )
        }
        
        // Bottom Bar
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(
                    brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f))
                    )
                )
                .padding(12.dp)
        ) {
            // Progress Bar
            Slider(
                value = state.currentPosition.toFloat(),
                onValueChange = { onSeek(it.toLong()) },
                valueRange = 0f..state.duration.coerceAtLeast(1L).toFloat(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp),
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.primary,
                    activeTrackColor = MaterialTheme.colorScheme.primary
                )
            )
            
            // Time Display
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = formatTime(state.currentPosition),
                    color = Color.White,
                    style = MaterialTheme.typography.labelSmall
                )
                Text(
                    text = formatTime(state.duration),
                    color = Color.White,
                    style = MaterialTheme.typography.labelSmall
                )
            }
            
            // Control Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                var showSpeedMenu by remember { mutableStateOf(false) }
                Box {
                    IconButton(
                        onClick = { showSpeedMenu = !showSpeedMenu },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Text(
                            text = "${state.playbackSpeed}x",
                            color = Color.White,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                    
                    DropdownMenu(
                        expanded = showSpeedMenu,
                        onDismissRequest = { showSpeedMenu = false }
                    ) {
                        listOf(0.5f, 0.75f, 1f, 1.25f, 1.5f, 2f).forEach { speed ->
                            DropdownMenuItem(
                                text = { Text("${speed}x") },
                                onClick = { /* onSpeedChange(speed) */ }
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.weight(1f))
                
                IconButton(
                    onClick = onFullscreenClick,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        if (state.isFullscreen) Icons.Default.Fullscreen else Icons.Default.Fullscreen,
                        contentDescription = "Fullscreen",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

private fun formatTime(millis: Long): String {
    val seconds = (millis / 1000) % 60
    val minutes = (millis / (1000 * 60)) % 60
    val hours = (millis / (1000 * 60 * 60))
    
    return when {
        hours > 0 -> String.format("%d:%02d:%02d", hours, minutes, seconds)
        else -> String.format("%d:%02d", minutes, seconds)
    }
}
