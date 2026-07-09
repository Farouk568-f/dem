package com.cinestream.data.repository

import com.cinestream.data.api.CineStreamApiService
import com.cinestream.data.api.models.*

class MovieRepository(private val apiService: CineStreamApiService) {
    
    suspend fun getPopularMovies(page: Int = 1, apiKey: String, language: String = "en-US"): MovieResults {
        return apiService.getPopularMovies(page, apiKey, language)
    }

    suspend fun getPopularTV(page: Int = 1, apiKey: String, language: String = "en-US"): MovieResults {
        return apiService.getPopularTV(page, apiKey, language)
    }

    suspend fun getTopRatedMovies(page: Int = 1, apiKey: String, language: String = "en-US"): MovieResults {
        return apiService.getTopRatedMovies(page, apiKey, language)
    }

    suspend fun getTopRatedTV(page: Int = 1, apiKey: String, language: String = "en-US"): MovieResults {
        return apiService.getTopRatedTV(page, apiKey, language)
    }

    suspend fun getMovieDetails(movieId: Int, apiKey: String, language: String = "en-US"): Movie {
        return apiService.getMovieDetails(movieId, apiKey, language)
    }

    suspend fun getTVShowDetails(showId: Int, apiKey: String, language: String = "en-US"): Movie {
        return apiService.getTVShowDetails(showId, apiKey, language)
    }

    suspend fun getSeasonDetails(showId: Int, seasonNumber: Int, apiKey: String, language: String = "en-US"): SeasonDetails {
        return apiService.getSeasonDetails(showId, seasonNumber, apiKey, language)
    }

    suspend fun getEpisodeDetails(showId: Int, seasonNumber: Int, episodeNumber: Int, apiKey: String, language: String = "en-US"): Episode {
        return apiService.getEpisodeDetails(showId, seasonNumber, episodeNumber, apiKey, language)
    }

    suspend fun searchMovies(query: String, page: Int = 1, apiKey: String, language: String = "en-US"): MovieResults {
        return apiService.searchMovies(query, page, apiKey, language)
    }

    suspend fun searchTV(query: String, page: Int = 1, apiKey: String, language: String = "en-US"): MovieResults {
        return apiService.searchTV(query, page, apiKey, language)
    }

    suspend fun searchMulti(query: String, page: Int = 1, apiKey: String, language: String = "en-US"): MovieResults {
        return apiService.searchMulti(query, page, apiKey, language)
    }

    suspend fun getActorDetails(actorId: Int, apiKey: String, language: String = "en-US"): Actor {
        return apiService.getActorDetails(actorId, apiKey, language)
    }

    suspend fun getMovieGenres(apiKey: String, language: String = "en-US"): GenreList {
        return apiService.getMovieGenres(apiKey, language)
    }

    suspend fun getTVGenres(apiKey: String, language: String = "en-US"): GenreList {
        return apiService.getTVGenres(apiKey, language)
    }
}
