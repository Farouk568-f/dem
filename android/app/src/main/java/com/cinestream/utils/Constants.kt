package com.cinestream.utils

object Constants {
    // TMDB API
    const val TMDB_API_KEY = "0d2c94c6cd0a1a95c2e27c0a2c9b9e8f"
    const val TMDB_BASE_URL = "https://api.themoviedb.org/3/"
    const val TMDB_IMAGE_URL = "https://image.tmdb.org/t/p/"
    
    // Scraper API
    const val SCRAPER_API_URL = "https://api.consumet.org/movies/"
    
    // Local Storage Keys
    const val PREF_AUTH_TOKEN = "auth_token"
    const val PREF_PROFILE_ID = "profile_id"
    const val PREF_LANGUAGE = "language"
    const val PREF_THEME = "theme"
    const val PREF_USER_PREFERENCES = "user_preferences"
    
    // Available Providers
    val AVAILABLE_PROVIDERS = listOf(
        "vidsrc",
        "vidsrc_to",
        "vidsrc_me",
        "vidsrc_xyz",
        "vidsrc_vip",
        "embedflix",
        "multiembed",
        "vidnap",
        "moviesdrive",
        "watch",
        "autoembed",
        "warezcdn",
        "dood",
        "filemoon",
        "streamwish",
        "linkvertise",
        "mediafire",
        "gdrive",
        "uptobox",
        "cloudflare",
        "ok",
        "streamable"
    )
    
    // Languages
    val AVAILABLE_LANGUAGES = mapOf(
        "en" to "English",
        "ar" to "العربية",
        "fr" to "Français",
        "es" to "Español",
        "pt" to "Português",
        "it" to "Italiano",
        "de" to "Deutsch",
        "ja" to "日本語",
        "zh" to "中文",
        "ko" to "한국어"
    )
    
    // Timeouts
    const val API_TIMEOUT_MS = 15000L
    const val CACHE_DURATION_MS = 3600000L // 1 hour
    
    // UI
    const val ITEMS_PER_PAGE = 20
    const val GRID_COLUMN_COUNT = 3
    const val PLACEHOLDER_IMAGE = "https://via.placeholder.com/500"
}
