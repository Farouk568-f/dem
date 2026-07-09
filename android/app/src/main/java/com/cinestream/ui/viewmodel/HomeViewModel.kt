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

class HomeViewModel(private val movieRepository: MovieRepository) : ViewModel() {
    
    private val _popularMovies = MutableStateFlow<List<Movie>>(emptyList())
    val popularMovies: StateFlow<List<Movie>> = _popularMovies.asStateFlow()
    
    private val _popularTV = MutableStateFlow<List<Movie>>(emptyList())
    val popularTV: StateFlow<List<Movie>> = _popularTV.asStateFlow()
    
    private val _topRatedMovies = MutableStateFlow<List<Movie>>(emptyList())
    val topRatedMovies: StateFlow<List<Movie>> = _topRatedMovies.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    init {
        loadHomeData()
    }
    
    private fun loadHomeData() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                
                // Load popular movies
                val moviesResult = movieRepository.getPopularMovies(
                    apiKey = Constants.TMDB_API_KEY
                )
                _popularMovies.value = moviesResult.results
                
                // Load popular TV shows
                val tvResult = movieRepository.getPopularTV(
                    apiKey = Constants.TMDB_API_KEY
                )
                _popularTV.value = tvResult.results
                
                // Load top rated movies
                val topRatedResult = movieRepository.getTopRatedMovies(
                    apiKey = Constants.TMDB_API_KEY
                )
                _topRatedMovies.value = topRatedResult.results
                
                _isLoading.value = false
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
                _isLoading.value = false
            }
        }
    }
    
    fun refresh() {
        loadHomeData()
    }
    
    fun clearError() {
        _error.value = null
    }
}
