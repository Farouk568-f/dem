package com.cinestream.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cinestream.data.api.models.Movie
import com.cinestream.data.repository.MovieRepository
import com.cinestream.utils.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val movieRepository: MovieRepository,
    private val movieId: Int,
    private val mediaType: String
) : ViewModel() {
    
    private val _details = MutableStateFlow<Movie?>(null)
    val details: StateFlow<Movie?> = _details.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()
    
    init {
        loadDetails()
    }
    
    private fun loadDetails() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                
                val details = if (mediaType == "movie") {
                    movieRepository.getMovieDetails(movieId, Constants.TMDB_API_KEY)
                } else {
                    movieRepository.getTVShowDetails(movieId, Constants.TMDB_API_KEY)
                }
                
                _details.value = details
                _isLoading.value = false
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
                _isLoading.value = false
            }
        }
    }
    
    fun toggleFavorite() {
        _isFavorite.value = !_isFavorite.value
    }
    
    fun refresh() {
        loadDetails()
    }
    
    fun clearError() {
        _error.value = null
    }
}
