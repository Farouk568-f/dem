package com.cinestream.utils

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.Modifier

/**
 * Accessibility utilities ensuring WCAG 2.1 AA compliance
 */

// Minimum touch target size (48dp x 48dp per Material 3)
const val ACCESSIBLE_TOUCH_SIZE_DP = 48

// Minimum text size for legibility
const val MINIMUM_TEXT_SIZE_SP = 14

// Color contrast ratio helpers
object ContrastRatios {
    // WCAG AA requires 4.5:1 for normal text, 3:1 for large text
    // These are approximate luminance values for common colors
    
    fun hasAdequateContrast(
        foreground: Color,
        background: Color,
        isLargeText: Boolean = false
    ): Boolean {
        val minRatio = if (isLargeText) 3.0 else 4.5
        val contrast = calculateContrastRatio(foreground, background)
        return contrast >= minRatio
    }
    
    private fun calculateContrastRatio(color1: Color, color2: Color): Double {
        val l1 = relativeLuminance(color1)
        val l2 = relativeLuminance(color2)
        val lighter = maxOf(l1, l2)
        val darker = minOf(l1, l2)
        return (lighter + 0.05) / (darker + 0.05)
    }
    
    private fun relativeLuminance(color: Color): Double {
        var r = color.red.toDouble()
        var g = color.green.toDouble()
        var b = color.blue.toDouble()
        
        r = if (r <= 0.03928) r / 12.92 else kotlin.math.pow((r + 0.055) / 1.055, 2.4)
        g = if (g <= 0.03928) g / 12.92 else kotlin.math.pow((g + 0.055) / 1.055, 2.4)
        b = if (b <= 0.03928) b / 12.92 else kotlin.math.pow((b + 0.055) / 1.055, 2.4)
        
        return 0.2126 * r + 0.7152 * g + 0.0722 * b
    }
}

// Semantic content description helpers
fun Modifier.accessibleClick(
    label: String,
    onClickLabel: String = "Click"
) = this.semantics {
    contentDescription = "$label, $onClickLabel"
}

fun Modifier.accessibleButton(label: String) = this.semantics {
    contentDescription = "$label button"
}

// Focus indication for keyboard navigation
@Composable
fun getFocusIndicatorColor(): Color {
    return MaterialTheme.colorScheme.primary
}

// High contrast mode support
@Composable
fun getHighContrastColor(normalColor: Color, highContrastColor: Color? = null): Color {
    // In a real app, this would check system accessibility settings
    return normalColor
}

// Screen reader announcements
object AccessibilityAnnouncements {
    const val SEARCH_RESULTS_COUNT = "Found {0} results"
    const val FAVORITES_ADDED = "Added to favorites"
    const val FAVORITES_REMOVED = "Removed from favorites"
    const val PLAYBACK_PAUSED = "Playback paused"
    const val PLAYBACK_RESUMED = "Playback resumed"
    const val SUBTITLE_ENABLED = "Subtitles enabled, language: {0}"
    const val SUBTITLE_DISABLED = "Subtitles disabled"
    const val QUALITY_CHANGED = "Playback quality changed to {0}p"
}
