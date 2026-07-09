package com.cinestream

import android.app.Application
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

/**
 * CineStream Application class for global initialization
 */
class CineStreamApplication : Application(), ImageLoaderFactory {
    
    companion object {
        private var instance: CineStreamApplication? = null
        
        fun getInstance(): CineStreamApplication {
            return instance ?: throw IllegalStateException("CineStreamApplication not initialized")
        }
    }
    
    override fun onCreate() {
        super.onCreate()
        instance = this
        
        // Initialize analytics (placeholder for future integration)
        initializeAnalytics()
        
        // Set default theme preference
        when (getThemePreference()) {
            "light" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            "dark" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
        
        // Configure crash reporting (placeholder for future integration)
        initializeCrashReporting()
    }
    
    /**
     * Factory method for creating Coil ImageLoader with optimized caching
     */
    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(0.25)  // Use 25% of app memory
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(cacheDir.resolve("image_cache"))
                    .maxSizeBytes(100L * 1024 * 1024)  // 100MB disk cache
                    .build()
            }
            .okHttpClient {
                OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(15, TimeUnit.SECONDS)
                    .build()
            }
            .build()
    }
    
    private fun initializeAnalytics() {
        // TODO: Integrate Firebase Analytics or similar
        // FirebaseAnalytics.getInstance(this).setAnalyticsCollectionEnabled(true)
    }
    
    private fun initializeCrashReporting() {
        // TODO: Integrate Crashlytics or similar
        // FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
    }
    
    private fun getThemePreference(): String {
        val prefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        return prefs.getString("theme_preference", "auto") ?: "auto"
    }
}
