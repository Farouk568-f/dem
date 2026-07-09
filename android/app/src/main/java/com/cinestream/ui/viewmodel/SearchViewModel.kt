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
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay

class SearchViewModel(private val movieRepository: MovieRepository) : ViewModel() {
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    private val _searchResults = MutableStateFlow<List<Movie>>(emptyList())
    val searchResults: StateFlow<List<Movie>> = _searchResults.asStateFlow()
    
    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()
    
    private val _searchError = MutableStateFlow<String?>(null)
    val searchError: StateFlow<String?> = _searchError.asStateFlow()
    
    private val _searchHistory = MutableStateFlow<List<String>>(emptyList())
    val searchHistory: StateFlow<List<String>> = _searchHistory.asStateFlow()
    
    private val _currentPage = MutableStateFlow(1)
    val currentPage: StateFlow<Int> = _currentPage.asStateFlow()
    
    private val _hasMoreResults = MutableStateFlow(false)
    val hasMoreResults: StateFlow<Boolean> = _hasMoreResults.asStateFlow()
    
    private var searchJob: Job? = null
    
    init {
        loadSearchHistory()
    }
    
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        
        // Cancel previous search
        searchJob?.cancel()
        
        if (query.isEmpty()) {
            _searchResults.value = emptyList()
            _searchError.value = null
            _currentPage.value = 1
            return
        }
        
        // Debounce search (wait 500ms after user stops typing)
        searchJob = viewModelScope.launch {
            delay(500)
            performSearch(query)
        }
    }
    
    private fun performSearch(query: String) {
        viewModelScope.launch {
            try {
                _isSearching.value = true
                _searchError.value = null
                _currentPage.value = 1
                _searchResults.value = emptyList()
                
                val result = movieRepository.searchMulti(
                    query = query,
                    page = 1,
                    apiKey = Constants.TMDB_API_KEY
                )
                
                _searchResults.value = result.results
                _currentPage.value = 1
                _hasMoreResults.value = result.page < result.total_pages
                
                // Add to search history
                addToSearchHistory(query)
                _isSearching.value = false
            } catch (e: Exception) {
                _searchError.value = e.message ?: "Search failed"
                _isSearching.value = false
            }
        }
    }
    
    fun loadNextPage() {
        viewModelScope.launch {
            if (_isSearching.value || !_hasMoreResults.value || _searchQuery.value.isEmpty()) {
                return@launch
            }
            
            try {
                _isSearching.value = true
                val nextPage = _currentPage.value + 1
                
                val result = movieRepository.searchMulti(
                    query = _searchQuery.value,
                    page = nextPage,
                    apiKey = Constants.TMDB_API_KEY
                )
                
                _searchResults.value = _searchResults.value + result.results
                _currentPage.value = nextPage
                _hasMoreResults.value = result.page < result.total_pages
                _isSearching.value = false
            } catch (e: Exception) {
                _searchError.value = e.message ?: "Load more failed"
                _isSearching.value = false
            }
        }
    }
    
    private fun addToSearchHistory(query: String) {
        val history = _searchHistory.value.toMutableList()
        history.remove(query) // Remove if exists
        history.add(0, query) // Add to front
        if (history.size > 10) {
            history.removeAt(history.size - 1) // Keep only 10 most recent
        }
        _searchHistory.value = history
    }
    
    fun loadSearchHistory() {
        // TODO: Load from database when favorites/history DB is set up
        _searchHistory.value = listOf(
            "Inception",
            "The Matrix",
            "Avatar",
            "Interstellar"
        )
    }
    
    fun clearSearchHistory() {
        _searchHistory.value = emptyList()
        // TODO: Clear from database
    }
    
    fun removeFromHistory(query: String) {
        val history = _searchHistory.value.toMutableList()
        history.remove(query)
        _searchHistory.value = history
    }
    
    fun clearError() {
        _searchError.value = null
    }
}
