package com.cinestream.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cinestream.data.api.models.Actor
import com.cinestream.data.repository.MovieRepository
import com.cinestream.utils.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ActorViewModel(
    private val movieRepository: MovieRepository,
    private val actorId: Int
) : ViewModel() {
    
    private val _actor = MutableStateFlow<Actor?>(null)
    val actor: StateFlow<Actor?> = _actor.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    init {
        loadActorDetails()
    }
    
    private fun loadActorDetails() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                
                val actor = movieRepository.getActorDetails(
                    actorId,
                    Constants.TMDB_API_KEY
                )
                
                _actor.value = actor
                _isLoading.value = false
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
                _isLoading.value = false
            }
        }
    }
    
    fun refresh() {
        loadActorDetails()
    }
    
    fun clearError() {
        _error.value = null
    }
}
