package com.cinestream.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.Player
import com.cinestream.data.repository.MovieRepository
import com.cinestream.utils.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.min

data class PlayerState(
    val isPlaying: Boolean = false,
    val currentPosition: Long = 0L,
    val duration: Long = 0L,
    val isFullscreen: Boolean = false,
    val volume: Float = 1f,
    val playbackSpeed: Float = 1f,
    val showControls: Boolean = true,
    val isSeeking: Boolean = false,
    val quality: String = "auto",
    val availableQualities: List<String> = listOf("auto", "1080p", "720p", "480p", "360p")
)

data class VideoSource(
    val url: String,
    val quality: String = "auto",
    val mimeType: String = "video/mp4"
)

class PlayerViewModel(private val movieRepository: MovieRepository) : ViewModel() {
    
    private val _playerState = MutableStateFlow(PlayerState())
    val playerState: StateFlow<PlayerState> = _playerState.asStateFlow()
    
    private val _videoSource = MutableStateFlow<VideoSource?>(null)
    val videoSource: StateFlow<VideoSource?> = _videoSource.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    private val _subtitles = MutableStateFlow<List<SubtitleTrack>>(emptyList())
    val subtitles: StateFlow<List<SubtitleTrack>> = _subtitles.asStateFlow()
    
    private val _selectedSubtitle = MutableStateFlow<SubtitleTrack?>(null)
    val selectedSubtitle: StateFlow<SubtitleTrack?> = _selectedSubtitle.asStateFlow()
    
    private val _watchHistory = MutableStateFlow<Long>(0L)
    val watchHistory: StateFlow<Long> = _watchHistory.asStateFlow()
    
    private val _bookmarks = MutableStateFlow<List<Long>>(emptyList())
    val bookmarks: StateFlow<List<Long>> = _bookmarks.asStateFlow()
    
    fun loadVideoSource(movieId: Int, mediaType: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                
                // TODO: Replace with actual API call to get streaming links
                // For now, use a placeholder video URL
                val videoSource = VideoSource(
                    url = "https://commondatastorage.googleapis.com/gtv-videos-library/sample/BigBuckBunny.mp4",
                    quality = "auto",
                    mimeType = "video/mp4"
                )
                
                _videoSource.value = videoSource
                
                // Load subtitle tracks
                loadSubtitles(movieId, mediaType)
                
                _isLoading.value = false
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to load video"
                _isLoading.value = false
            }
        }
    }
    
    private fun loadSubtitles(movieId: Int, mediaType: String) {
        viewModelScope.launch {
            try {
                // TODO: Load actual subtitles from API
                val mockSubtitles = listOf(
                    SubtitleTrack("English", "en", "url/en.vtt"),
                    SubtitleTrack("Spanish", "es", "url/es.vtt"),
                    SubtitleTrack("French", "fr", "url/fr.vtt")
                )
                _subtitles.value = mockSubtitles
            } catch (e: Exception) {
                // Silently fail - continue without subtitles
            }
        }
    }
    
    fun onPlayerStateChanged(playbackState: Int, playWhenReady: Boolean) {
        when (playbackState) {
            Player.STATE_IDLE -> {
                updatePlayerState { copy(isPlaying = false) }
            }
            Player.STATE_BUFFERING -> {
                updatePlayerState { copy(isPlaying = playWhenReady) }
            }
            Player.STATE_READY -> {
                updatePlayerState { copy(isPlaying = playWhenReady) }
            }
            Player.STATE_ENDED -> {
                updatePlayerState { copy(isPlaying = false) }
            }
        }
    }
    
    fun onPositionChanged(position: Long, duration: Long) {
        updatePlayerState { 
            copy(
                currentPosition = position,
                duration = duration
            )
        }
        _watchHistory.value = position
    }
    
    fun togglePlayPause(isPlaying: Boolean) {
        updatePlayerState { copy(isPlaying = !isPlaying) }
    }
    
    fun toggleFullscreen() {
        updatePlayerState { copy(isFullscreen = !isFullscreen) }
    }
    
    fun seekTo(position: Long) {
        updatePlayerState { 
            copy(
                currentPosition = min(position, _playerState.value.duration),
                isSeeking = false
            )
        }
    }
    
    fun setVolume(volume: Float) {
        updatePlayerState { copy(volume = volume.coerceIn(0f, 1f)) }
    }
    
    fun setPlaybackSpeed(speed: Float) {
        updatePlayerState { copy(playbackSpeed = speed) }
    }
    
    fun setQuality(quality: String) {
        if (quality in _playerState.value.availableQualities) {
            updatePlayerState { copy(quality = quality) }
            // TODO: Switch video source based on quality
        }
    }
    
    fun toggleControlsVisibility() {
        updatePlayerState { copy(showControls = !showControls) }
    }
    
    fun selectSubtitle(subtitle: SubtitleTrack?) {
        _selectedSubtitle.value = subtitle
    }
    
    fun addBookmark(position: Long) {
        val currentBookmarks = _bookmarks.value.toMutableList()
        if (!currentBookmarks.contains(position)) {
            currentBookmarks.add(position)
            currentBookmarks.sort()
            _bookmarks.value = currentBookmarks
        }
    }
    
    fun removeBookmark(position: Long) {
        val currentBookmarks = _bookmarks.value.toMutableList()
        currentBookmarks.remove(position)
        _bookmarks.value = currentBookmarks
    }
    
    fun clearError() {
        _error.value = null
    }
    
    fun savePlaybackPosition(movieId: Int, mediaType: String, position: Long) {
        viewModelScope.launch {
            try {
                // TODO: Save to database when history/watchlist DB is set up
                _watchHistory.value = position
            } catch (e: Exception) {
                // Silently fail
            }
        }
    }
    
    private fun updatePlayerState(update: PlayerState.() -> PlayerState) {
        _playerState.value = _playerState.value.update()
    }
}

data class SubtitleTrack(
    val name: String,
    val languageCode: String,
    val url: String
)
