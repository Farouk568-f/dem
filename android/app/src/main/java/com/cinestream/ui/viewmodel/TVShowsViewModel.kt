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

class TVShowsViewModel(private val movieRepository: MovieRepository) : ViewModel() {
    
    private val _tvShows = MutableStateFlow<List<Movie>>(emptyList())
    val tvShows: StateFlow<List<Movie>> = _tvShows.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    private val _currentPage = MutableStateFlow(1)
    val currentPage: StateFlow<Int> = _currentPage.asStateFlow()
    
    private val _hasMorePages = MutableStateFlow(true)
    val hasMorePages: StateFlow<Boolean> = _hasMorePages.asStateFlow()
    
    init {
        loadTVShows()
    }
    
    fun loadTVShows() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                _currentPage.value = 1
                _tvShows.value = emptyList()
                
                val result = movieRepository.getPopularTV(
                    page = 1,
                    apiKey = Constants.TMDB_API_KEY
                )
                
                _tvShows.value = result.results
                _hasMorePages.value = result.page < result.total_pages
                _isLoading.value = false
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
                _isLoading.value = false
            }
        }
    }
    
    fun loadNextPage() {
        viewModelScope.launch {
            if (_isLoading.value || !_hasMorePages.value) return@launch
            
            try {
                _isLoading.value = true
                val nextPage = _currentPage.value + 1
                
                val result = movieRepository.getPopularTV(
                    page = nextPage,
                    apiKey = Constants.TMDB_API_KEY
                )
                
                _tvShows.value = _tvShows.value + result.results
                _currentPage.value = nextPage
                _hasMorePages.value = result.page < result.total_pages
                _isLoading.value = false
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
                _isLoading.value = false
            }
        }
    }
    
    fun refresh() {
        loadTVShows()
    }
    
    fun clearError() {
        _error.value = null
    }
}
