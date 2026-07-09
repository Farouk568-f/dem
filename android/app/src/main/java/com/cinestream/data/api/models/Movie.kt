package com.cinestream.data.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Movie(
    val id: Int,
    val title: String? = null,
    val name: String? = null,
    val poster_path: String? = null,
    val backdrop_path: String? = null,
    val overview: String = "",
    val release_date: String? = null,
    val first_air_date: String? = null,
    val vote_average: Double = 0.0,
    val vote_count: Int = 0,
    val popularity: Double? = null,
    val genres: List<Genre>? = null,
    val runtime: Int? = null,
    val media_type: String? = null,
    val itemType: String? = null,
    val content_ratings: ContentRatings? = null,
    val credits: Credits? = null,
    val production_companies: List<ProductionCompany>? = null,
    val status: String? = null,
    val original_title: String? = null,
    val original_name: String? = null,
    val original_language: String? = null,
    val recommendations: MovieResults? = null,
    val seasons: List<Season>? = null,
    val number_of_seasons: Int? = null,
    val number_of_episodes: Int? = null,
    val videos: Videos? = null,
    val images: Images? = null,
    
    // Local properties
    var currentTime: Long = 0,
    var duration: Long = 0
) {
    fun getDisplayTitle(): String = title ?: name ?: ""
    
    fun getMediaType(): String = media_type ?: itemType ?: "movie"
}

@Serializable
data class Genre(
    val id: Int,
    val name: String
)

@Serializable
data class ContentRatings(
    val results: List<ContentRating> = emptyList()
)

@Serializable
data class ContentRating(
    val iso_3166_1: String,
    val rating: String
)

@Serializable
data class Credits(
    val cast: List<Cast> = emptyList()
)

@Serializable
data class Cast(
    val name: String,
    val character: String? = null,
    val profile_path: String? = null
)

@Serializable
data class ProductionCompany(
    val id: Int? = null,
    val name: String,
    val logo_path: String? = null
)

@Serializable
data class Season(
    val id: Int,
    val season_number: Int,
    val name: String,
    val poster_path: String? = null,
    val episode_count: Int
)

@Serializable
data class Episode(
    val id: Int,
    val episode_number: Int,
    val name: String,
    val overview: String,
    val still_path: String? = null,
    val air_date: String? = null
)

@Serializable
data class Videos(
    val results: List<Video> = emptyList()
)

@Serializable
data class Video(
    val key: String,
    val site: String,
    val type: String
)

@Serializable
data class Images(
    val logos: List<Logo> = emptyList(),
    val posters: List<Poster>? = null,
    val backdrops: List<Backdrop>? = null
)

@Serializable
data class Logo(
    val file_path: String,
    val iso_639_1: String? = null
)

@Serializable
data class Poster(
    val file_path: String
)

@Serializable
data class Backdrop(
    val file_path: String
)

@Serializable
data class MovieResults(
    val results: List<Movie> = emptyList(),
    val page: Int = 1,
    val total_pages: Int = 1,
    val total_results: Int = 0
)

@Serializable
data class HistoryItem(
    val id: Int,
    val type: String, // 'movie' or 'tv'
    val title: String,
    val itemImage: String,
    val currentTime: Long,
    val duration: Long,
    val timestamp: Long,
    val episodeId: Int? = null,
    val seasonNumber: Int? = null,
    val episodeNumber: Int? = null
)
