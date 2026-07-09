package com.cinestream.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cinestream.domain.auth.AuthService
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AuthViewModel(private val authService: AuthService) : ViewModel() {
    
    val isAuthenticated: StateFlow<Boolean> = authService.isAuthenticated
        .stateIn(viewModelScope, SharingStarted.Lazily, false)
    
    val currentProfileId: StateFlow<String?> = authService.currentProfileId
        .stateIn(viewModelScope, SharingStarted.Lazily, null)
    
    val authError: StateFlow<String?> = authService.authError
        .stateIn(viewModelScope, SharingStarted.Lazily, null)
    
    fun login(email: String, password: String) {
        viewModelScope.launch {
            authService.login(email, password)
        }
    }
    
    fun loginWithQR(qrCode: String) {
        viewModelScope.launch {
            authService.loginWithQR(qrCode)
        }
    }
    
    fun selectProfile(profileId: String) {
        authService.selectProfile(profileId)
    }
    
    fun logout() {
        authService.logout()
    }
    
    fun clearError() {
        authService.clearError()
    }
}
