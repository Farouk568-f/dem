package com.cinestream.utils

import android.view.KeyEvent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalView

/**
 * TV remote and d-pad navigation support for Android TV
 */

// D-Pad key events
data class DPadNavigation(
    val onUp: (() -> Unit)? = null,
    val onDown: (() -> Unit)? = null,
    val onLeft: (() -> Unit)? = null,
    val onRight: (() -> Unit)? = null,
    val onCenter: (() -> Unit)? = null,
    val onBack: (() -> Unit)? = null
)

// Key code helper
object RemoteKeyCode {
    const val UP = KeyEvent.KEYCODE_DPAD_UP
    const val DOWN = KeyEvent.KEYCODE_DPAD_DOWN
    const val LEFT = KeyEvent.KEYCODE_DPAD_LEFT
    const val RIGHT = KeyEvent.KEYCODE_DPAD_RIGHT
    const val CENTER = KeyEvent.KEYCODE_DPAD_CENTER
    const val BACK = KeyEvent.KEYCODE_BACK
    const val PLAY_PAUSE = KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE
    const val REWIND = KeyEvent.KEYCODE_MEDIA_REWIND
    const val FAST_FORWARD = KeyEvent.KEYCODE_MEDIA_FAST_FORWARD
}

// TV navigation composable hook
@Composable
fun rememberTVRemoteNavigation(navigation: DPadNavigation) {
    val view = LocalView.current
    
    DisposableEffect(navigation) {
        val listener = android.view.View.OnKeyListener { _, keyCode, _ ->
            when (keyCode) {
                RemoteKeyCode.UP -> {
                    navigation.onUp?.invoke()
                    true
                }
                RemoteKeyCode.DOWN -> {
                    navigation.onDown?.invoke()
                    true
                }
                RemoteKeyCode.LEFT -> {
                    navigation.onLeft?.invoke()
                    true
                }
                RemoteKeyCode.RIGHT -> {
                    navigation.onRight?.invoke()
                    true
                }
                RemoteKeyCode.CENTER -> {
                    navigation.onCenter?.invoke()
                    true
                }
                RemoteKeyCode.BACK -> {
                    navigation.onBack?.invoke()
                    true
                }
                else -> false
            }
        }
        
        view.setOnKeyListener(listener)
        
        onDispose {
            view.setOnKeyListener(null)
        }
    }
}

// Player-specific remote control handling
object PlayerRemoteControls {
    fun handlePlaybackKey(
        keyCode: Int,
        onPlayPause: () -> Unit,
        onRewind: () -> Unit,
        onFastForward: () -> Unit
    ): Boolean {
        return when (keyCode) {
            RemoteKeyCode.PLAY_PAUSE -> {
                onPlayPause()
                true
            }
            RemoteKeyCode.REWIND -> {
                onRewind()
                true
            }
            RemoteKeyCode.FAST_FORWARD -> {
                onFastForward()
                true
            }
            else -> false
        }
    }
}

// Focus management for TV
object TVFocusHelpers {
    const val FOCUS_SCALE_NORMAL = 1.0f
    const val FOCUS_SCALE_FOCUSED = 1.05f
    
    // Safe area insets for safe zone rendering on TV
    data class SafeAreaInsets(
        val left: Float = 0.1f,    // 10% from left
        val right: Float = 0.1f,   // 10% from right
        val top: Float = 0.05f,    // 5% from top
        val bottom: Float = 0.05f  // 5% from bottom
    )
}
