package com.cinestream.data.api

import com.cinestream.data.api.models.*
import retrofit2.http.*

interface CineStreamApiService {
    
    // TMDB Endpoints
    @GET("discover/movie")
    suspend fun discoverMovies(
        @Query("page") page: Int = 1,
        @Query("sort_by") sortBy: String = "popularity.desc",
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US"
    ): MovieResults

    @GET("discover/tv")
    suspend fun discoverTV(
        @Query("page") page: Int = 1,
        @Query("sort_by") sortBy: String = "popularity.desc",
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US"
    ): MovieResults

    @GET("movie/{id}")
    suspend fun getMovieDetails(
        @Path("id") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US",
        @Query("append_to_response") appendToResponse: String = "credits,recommendations,videos,images,content_ratings"
    ): Movie

    @GET("tv/{id}")
    suspend fun getTVShowDetails(
        @Path("id") showId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US",
        @Query("append_to_response") appendToResponse: String = "credits,recommendations,videos,images,content_ratings,seasons"
    ): Movie

    @GET("tv/{id}/season/{season_number}")
    suspend fun getSeasonDetails(
        @Path("id") showId: Int,
        @Path("season_number") seasonNumber: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US"
    ): SeasonDetails

    @GET("tv/{id}/season/{season_number}/episode/{episode_number}")
    suspend fun getEpisodeDetails(
        @Path("id") showId: Int,
        @Path("season_number") seasonNumber: Int,
        @Path("episode_number") episodeNumber: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US"
    ): Episode

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US"
    ): MovieResults

    @GET("search/tv")
    suspend fun searchTV(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US"
    ): MovieResults

    @GET("search/multi")
    suspend fun searchMulti(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US"
    ): MovieResults

    @GET("person/{id}")
    suspend fun getActorDetails(
        @Path("id") actorId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US",
        @Query("append_to_response") appendToResponse: String = "combined_credits,images"
    ): Actor

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("page") page: Int = 1,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US"
    ): MovieResults

    @GET("tv/popular")
    suspend fun getPopularTV(
        @Query("page") page: Int = 1,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US"
    ): MovieResults

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("page") page: Int = 1,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US"
    ): MovieResults

    @GET("tv/top_rated")
    suspend fun getTopRatedTV(
        @Query("page") page: Int = 1,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US"
    ): MovieResults

    @GET("genre/movie/list")
    suspend fun getMovieGenres(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US"
    ): GenreList

    @GET("genre/tv/list")
    suspend fun getTVGenres(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US"
    ): GenreList
}

@kotlinx.serialization.Serializable
data class SeasonDetails(
    val id: Int,
    val season_number: Int,
    val name: String,
    val poster_path: String? = null,
    val episodes: List<Episode> = emptyList()
)

@kotlinx.serialization.Serializable
data class GenreList(
    val genres: List<Genre> = emptyList()
)
