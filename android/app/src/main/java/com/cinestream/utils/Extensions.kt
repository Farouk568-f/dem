package com.cinestream.utils

import com.cinestream.data.api.models.Genre
import com.cinestream.data.api.models.Movie

fun String?.getPosterImageUrl(width: String = "w500"): String {
    if (this == null) return Constants.PLACEHOLDER_IMAGE
    return "${Constants.TMDB_IMAGE_URL}$width$this"
}

fun String?.getBackdropImageUrl(width: String = "w1280"): String {
    if (this == null) return Constants.PLACEHOLDER_IMAGE
    return "${Constants.TMDB_IMAGE_URL}$width$this"
}

fun Double.formatRating(): String {
    return String.format("%.1f", this)
}

fun Long.formatDuration(): String {
    val minutes = this / 60
    val seconds = this % 60
    return String.format("%02d:%02d", minutes, seconds)
}

fun Movie.getDisplayYear(): String {
    return (release_date ?: first_air_date)?.take(4) ?: "N/A"
}

fun Movie.isMovie(): Boolean {
    return media_type == "movie" || itemType == "movie"
}

fun Movie.isTV(): Boolean {
    return media_type == "tv" || itemType == "tv"
}

fun List<Genre>.getGenreString(): String {
    return this.joinToString(", ") { it.name }
}

fun String.truncate(maxLength: Int): String {
    return if (this.length > maxLength) this.take(maxLength) + "..." else this
}
