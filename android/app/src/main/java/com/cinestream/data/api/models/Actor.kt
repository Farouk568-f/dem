package com.cinestream.data.api.models

import kotlinx.serialization.Serializable

@Serializable
data class Actor(
    val id: Int,
    val name: String,
    val profile_path: String? = null,
    val biography: String = "",
    val birthday: String? = null,
    val place_of_birth: String? = null,
    val known_for_department: String = "",
    val popularity: Double = 0.0,
    val likes: Int? = null,
    val combined_credits: CombinedCredits? = null,
    val images: ActorImages? = null
)

@Serializable
data class CombinedCredits(
    val cast: List<Movie> = emptyList()
)

@Serializable
data class ActorImages(
    val profiles: List<ProfileImage> = emptyList()
)

@Serializable
data class ProfileImage(
    val file_path: String
)

@Serializable
data class ActorResults(
    val results: List<Actor> = emptyList(),
    val page: Int = 1,
    val total_pages: Int = 1,
    val total_results: Int = 0
)
