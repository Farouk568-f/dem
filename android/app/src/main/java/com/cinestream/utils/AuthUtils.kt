package com.cinestream.utils

import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

object AuthUtils {
    
    fun getAuthToken(preferences: SharedPreferences): String? {
        return preferences.getString(Constants.PREF_AUTH_TOKEN, null)
    }
    
    fun setAuthToken(preferences: SharedPreferences, token: String) {
        preferences.edit().putString(Constants.PREF_AUTH_TOKEN, token).apply()
    }
    
    fun clearAuthToken(preferences: SharedPreferences) {
        preferences.edit().remove(Constants.PREF_AUTH_TOKEN).apply()
    }
    
    fun isTokenValid(preferences: SharedPreferences): Boolean {
        return getAuthToken(preferences) != null
    }
    
    fun getProfileId(preferences: SharedPreferences): String? {
        return preferences.getString(Constants.PREF_PROFILE_ID, null)
    }
    
    fun setProfileId(preferences: SharedPreferences, profileId: String) {
        preferences.edit().putString(Constants.PREF_PROFILE_ID, profileId).apply()
    }
    
    fun getLanguage(preferences: SharedPreferences): String {
        return preferences.getString(Constants.PREF_LANGUAGE, "en") ?: "en"
    }
    
    fun setLanguage(preferences: SharedPreferences, language: String) {
        preferences.edit().putString(Constants.PREF_LANGUAGE, language).apply()
    }
    
    fun getTheme(preferences: SharedPreferences): String {
        return preferences.getString(Constants.PREF_THEME, "dark") ?: "dark"
    }
    
    fun setTheme(preferences: SharedPreferences, theme: String) {
        preferences.edit().putString(Constants.PREF_THEME, theme).apply()
    }
    
    fun clearAllPreferences(preferences: SharedPreferences) {
        preferences.edit().clear().apply()
    }
}
