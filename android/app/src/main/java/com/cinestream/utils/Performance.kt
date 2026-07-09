package com.cinestream.utils

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

/**
 * Performance monitoring and optimization utilities for CineStream
 */

// Debounce utility for search and input operations
fun <T> debounce(
    delayMs: Long = 300,
    onDebouncedValue: (T) -> Unit
): (T) -> Unit {
    var debounceJob: kotlinx.coroutines.Job? = null
    return { value: T ->
        debounceJob?.cancel()
        debounceJob = kotlinx.coroutines.GlobalScope.launch {
            kotlinx.coroutines.delay(delayMs)
            onDebouncedValue(value)
        }
    }
}

// Performance metric tracking
object PerformanceMetrics {
    private const val TAG = "CineStream-Perf"
    private val metrics = mutableMapOf<String, Long>()
    
    fun startMeasure(name: String) {
        metrics[name] = System.currentTimeMillis()
    }
    
    fun endMeasure(name: String) {
        val startTime = metrics[name] ?: return
        val duration = System.currentTimeMillis() - startTime
        Log.d(TAG, "$name completed in ${duration}ms")
        metrics.remove(name)
    }
    
    fun logMeasure(name: String, block: () -> Unit) {
        startMeasure(name)
        block()
        endMeasure(name)
    }
}

// Memory-efficient image loading configuration
object ImageLoadingConfig {
    const val THUMBNAIL_SIZE = 100
    const val POSTER_SIZE = 200
    const val BACKDROP_SIZE = 300
    const val CACHE_SIZE_MB = 100
}

// LazyList optimization helper
@Composable
fun rememberLazyListState() = androidx.compose.foundation.lazy.rememberLazyListState()

// Composable that only recompiles when necessary
@Composable
fun <T> rememberMemo(key1: T?, calculation: () -> T): T {
    return remember(key1) { calculation() }
}
