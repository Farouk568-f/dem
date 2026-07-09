package com.cinestream.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cinestream.data.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HistoryItem(
    val id: Int,
    val title: String,
    val posterPath: String?,
    val mediaType: String,
    val lastWatched: Long,
    val position: Long,  // Seconds watched
    val duration: Long,  // Total duration in seconds
    val progress: Float = if (duration > 0) (position.toFloat() / duration) * 100 else 0f
)

class HistoryViewModel(private val movieRepository: MovieRepository) : ViewModel() {
    
    private val _history = MutableStateFlow<List<HistoryItem>>(emptyList())
    val history: StateFlow<List<HistoryItem>> = _history.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    private val _filterType = MutableStateFlow<String?>(null)
    val filterType: StateFlow<String?> = _filterType.asStateFlow()
    
    init {
        loadHistory()
    }
    
    private fun loadHistory() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                
                // TODO: Load from database when implemented
                // For now, show empty list
                _history.value = emptyList()
                _isLoading.value = false
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to load history"
                _isLoading.value = false
            }
        }
    }
    
    fun addToHistory(item: HistoryItem) {
        viewModelScope.launch {
            try {
                val currentHistory = _history.value.toMutableList()
                
                // Remove if exists (to update lastWatched)
                currentHistory.removeAll { it.id == item.id && it.mediaType == item.mediaType }
                
                // Add to front (most recent first)
                currentHistory.add(0, item)
                
                // Keep only last 100 items
                if (currentHistory.size > 100) {
                    currentHistory.removeAt(currentHistory.size - 1)
                }
                
                _history.value = currentHistory
                // TODO: Save to database
            } catch (e: Exception) {
                _error.value = "Failed to add to history"
            }
        }
    }
    
    fun removeFromHistory(itemId: Int, mediaType: String) {
        viewModelScope.launch {
            try {
                val currentHistory = _history.value.toMutableList()
                currentHistory.removeAll { it.id == itemId && it.mediaType == mediaType }
                _history.value = currentHistory
                // TODO: Remove from database
            } catch (e: Exception) {
                _error.value = "Failed to remove from history"
            }
        }
    }
    
    fun updateWatchProgress(itemId: Int, mediaType: String, position: Long, duration: Long) {
        viewModelScope.launch {
            try {
                val currentHistory = _history.value.toMutableList()
                val index = currentHistory.indexOfFirst { 
                    it.id == itemId && it.mediaType == mediaType 
                }
                
                if (index >= 0) {
                    val item = currentHistory[index]
                    val updatedItem = item.copy(
                        position = position,
                        duration = duration,
                        lastWatched = System.currentTimeMillis(),
                        progress = if (duration > 0) (position.toFloat() / duration) * 100 else 0f
                    )
                    currentHistory[index] = updatedItem
                    _history.value = currentHistory
                    // TODO: Save to database
                }
            } catch (e: Exception) {
                _error.value = "Failed to update progress"
            }
        }
    }
    
    fun setFilterType(type: String?) {
        _filterType.value = type
    }
    
    fun getContinueWatchingItems(): List<HistoryItem> {
        return _history.value.filter { it.progress < 95f }
    }
    
    fun clearAllHistory() {
        viewModelScope.launch {
            _history.value = emptyList()
            // TODO: Clear from database
        }
    }
    
    fun refresh() {
        loadHistory()
    }
    
    fun clearError() {
        _error.value = null
    }
}
