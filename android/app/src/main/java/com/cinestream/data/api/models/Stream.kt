package com.cinestream.data.api.models

import kotlinx.serialization.Serializable

@Serializable
data class StreamData(
    val sources: List<StreamSource> = emptyList(),
    val subtitles: List<SubtitleTrack> = emptyList(),
    val headers: Map<String, String> = emptyMap()
)

@Serializable
data class StreamSource(
    val url: String,
    val quality: String? = null,
    val isM3U8: Boolean = false,
    val isDash: Boolean = false
) {
    fun getQualityLabel(): String = quality ?: "Unknown"
}

@Serializable
data class SubtitleTrack(
    val url: String,
    val lang: String = "en",
    val label: String = "English",
    val kind: String = "subtitles"
)

@Serializable
data class StreamLink(
    val url: String,
    val quality: String = "auto",
    val headers: Map<String, String> = emptyMap()
)

@Serializable
data class Profile(
    val id: String,
    val name: String,
    val avatar: String? = null,
    val language: String = "en",
    val theme: String = "dark"
)
