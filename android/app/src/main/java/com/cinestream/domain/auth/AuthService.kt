package com.cinestream.domain.auth

import android.content.SharedPreferences
import com.cinestream.utils.AuthUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthService(private val preferences: SharedPreferences) {
    
    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated.asStateFlow()
    
    private val _currentProfileId = MutableStateFlow<String?>(null)
    val currentProfileId: StateFlow<String?> = _currentProfileId.asStateFlow()
    
    private val _authError = MutableStateFlow<String?>(null)
    val authError: StateFlow<String?> = _authError.asStateFlow()
    
    init {
        // Check if token exists on initialization
        _isAuthenticated.value = AuthUtils.isTokenValid(preferences)
        _currentProfileId.value = AuthUtils.getProfileId(preferences)
    }
    
    suspend fun login(email: String, password: String): Result<String> {
        return try {
            // In a real app, this would make an API call to authenticate
            // For now, we'll simulate a login
            if (email.isNotEmpty() && password.isNotEmpty()) {
                val token = generateToken(email)
                AuthUtils.setAuthToken(preferences, token)
                _isAuthenticated.value = true
                _authError.value = null
                Result.success(token)
            } else {
                _authError.value = "Email and password required"
                Result.failure(IllegalArgumentException("Invalid credentials"))
            }
        } catch (e: Exception) {
            _authError.value = e.message
            Result.failure(e)
        }
    }
    
    suspend fun loginWithQR(qrCode: String): Result<String> {
        return try {
            // Decode and validate QR code
            val token = qrCode.split("|").getOrNull(0) ?: ""
            if (token.isNotEmpty()) {
                AuthUtils.setAuthToken(preferences, token)
                _isAuthenticated.value = true
                _authError.value = null
                Result.success(token)
            } else {
                _authError.value = "Invalid QR code"
                Result.failure(IllegalArgumentException("Invalid QR code"))
            }
        } catch (e: Exception) {
            _authError.value = e.message
            Result.failure(e)
        }
    }
    
    fun selectProfile(profileId: String) {
        AuthUtils.setProfileId(preferences, profileId)
        _currentProfileId.value = profileId
    }
    
    fun logout() {
        AuthUtils.clearAuthToken(preferences)
        AuthUtils.setProfileId(preferences, "")
        _isAuthenticated.value = false
        _currentProfileId.value = null
        _authError.value = null
    }
    
    fun getAuthToken(): String? {
        return AuthUtils.getAuthToken(preferences)
    }
    
    fun getCurrentProfileId(): String? {
        return AuthUtils.getProfileId(preferences)
    }
    
    fun clearError() {
        _authError.value = null
    }
    
    private fun generateToken(email: String): String {
        // Simple token generation - in production, use API response
        return "token_${System.currentTimeMillis()}_${email.hashCode()}"
    }
}
