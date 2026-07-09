package com.cinestream.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cinestream.data.api.models.Movie
import com.cinestream.data.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class FavoriteItem(
    val id: Int,
    val title: String,
    val posterPath: String?,
    val mediaType: String,
    val rating: Double,
    val addedDate: Long = System.currentTimeMillis()
)

class FavoritesViewModel(private val movieRepository: MovieRepository) : ViewModel() {
    
    private val _favorites = MutableStateFlow<List<FavoriteItem>>(emptyList())
    val favorites: StateFlow<List<FavoriteItem>> = _favorites.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    private val _sortOption = MutableStateFlow("recent")
    val sortOption: StateFlow<String> = _sortOption.asStateFlow()
    
    private val _filterType = MutableStateFlow<String?>(null)
    val filterType: StateFlow<String?> = _filterType.asStateFlow()
    
    init {
        loadFavorites()
    }
    
    private fun loadFavorites() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                
                // TODO: Load from database when implemented
                // For now, show empty list
                _favorites.value = emptyList()
                _isLoading.value = false
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to load favorites"
                _isLoading.value = false
            }
        }
    }
    
    fun addFavorite(item: FavoriteItem) {
        viewModelScope.launch {
            try {
                val currentFavorites = _favorites.value.toMutableList()
                
                // Check if already exists
                if (currentFavorites.none { it.id == item.id && it.mediaType == item.mediaType }) {
                    currentFavorites.add(item)
                    _favorites.value = currentFavorites
                    // TODO: Save to database
                }
            } catch (e: Exception) {
                _error.value = "Failed to add favorite"
            }
        }
    }
    
    fun removeFavorite(itemId: Int, mediaType: String) {
        viewModelScope.launch {
            try {
                val currentFavorites = _favorites.value.toMutableList()
                currentFavorites.removeAll { it.id == itemId && it.mediaType == mediaType }
                _favorites.value = currentFavorites
                // TODO: Remove from database
            } catch (e: Exception) {
                _error.value = "Failed to remove favorite"
            }
        }
    }
    
    fun toggleFavorite(item: FavoriteItem) {
        val isFavorited = _favorites.value.any { 
            it.id == item.id && it.mediaType == item.mediaType 
        }
        
        if (isFavorited) {
            removeFavorite(item.id, item.mediaType)
        } else {
            addFavorite(item)
        }
    }
    
    fun isFavorited(itemId: Int, mediaType: String): Boolean {
        return _favorites.value.any { it.id == itemId && it.mediaType == mediaType }
    }
    
    fun setSortOption(sort: String) {
        _sortOption.value = sort
        applySorting()
    }
    
    fun setFilterType(type: String?) {
        _filterType.value = type
        applyFiltering()
    }
    
    private fun applySorting() {
        val sorted = when (_sortOption.value) {
            "recent" -> _favorites.value.sortedByDescending { it.addedDate }
            "oldest" -> _favorites.value.sortedBy { it.addedDate }
            "rating" -> _favorites.value.sortedByDescending { it.rating }
            "alphabetical" -> _favorites.value.sortedBy { it.title }
            else -> _favorites.value
        }
        _favorites.value = sorted
    }
    
    private fun applyFiltering() {
        val filtered = if (_filterType.value != null) {
            _favorites.value.filter { it.mediaType == _filterType.value }
        } else {
            _favorites.value
        }
        _favorites.value = filtered
    }
    
    fun clearAllFavorites() {
        viewModelScope.launch {
            _favorites.value = emptyList()
            // TODO: Clear from database
        }
    }
    
    fun refresh() {
        loadFavorites()
    }
    
    fun clearError() {
        _error.value = null
    }
}
