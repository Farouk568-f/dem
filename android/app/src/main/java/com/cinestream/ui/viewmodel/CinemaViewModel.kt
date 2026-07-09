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

data class CinemaLocation(
    val id: String,
    val name: String,
    val address: String,
    val showtimes: List<String>
)

class CinemaViewModel(private val movieRepository: MovieRepository) : ViewModel() {
    
    private val _cinemas = MutableStateFlow<List<CinemaLocation>>(emptyList())
    val cinemas: StateFlow<List<CinemaLocation>> = _cinemas.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    private val _selectedGenre = MutableStateFlow<String?>(null)
    val selectedGenre: StateFlow<String?> = _selectedGenre.asStateFlow()
    
    init {
        loadCinemas()
    }
    
    private fun loadCinemas() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                
                // TODO: Replace with actual API call when available
                // For now, load some popular movies as cinema featured content
                val topRated = movieRepository.getTopRatedMovies(
                    apiKey = Constants.TMDB_API_KEY
                )
                
                // Simulate cinema locations
                _cinemas.value = listOf(
                    CinemaLocation(
                        id = "cinema_1",
                        name = "CineMax Downtown",
                        address = "123 Main St, Downtown",
                        showtimes = listOf("2:00 PM", "5:30 PM", "8:00 PM", "10:30 PM")
                    ),
                    CinemaLocation(
                        id = "cinema_2",
                        name = "ScreenX Cinema",
                        address = "456 Plaza Ave, Midtown",
                        showtimes = listOf("3:00 PM", "6:00 PM", "9:00 PM")
                    ),
                    CinemaLocation(
                        id = "cinema_3",
                        name = "IMAX Theater",
                        address = "789 Boulevard, Uptown",
                        showtimes = listOf("1:00 PM", "4:00 PM", "7:00 PM", "9:30 PM")
                    )
                )
                
                _isLoading.value = false
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to load cinemas"
                _isLoading.value = false
            }
        }
    }
    
    fun setGenreFilter(genre: String?) {
        _selectedGenre.value = genre
        loadCinemas()
    }
    
    fun refresh() {
        loadCinemas()
    }
    
    fun clearError() {
        _error.value = null
    }
}
