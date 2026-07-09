package com.cinestream.data.api

import com.cinestream.data.api.models.StreamData
import retrofit2.http.*

interface ScraperApiService {
    
    @GET("stream")
    suspend fun getStreamUrl(
        @Query("title") title: String,
        @Query("year") year: String? = null,
        @Query("season") season: Int? = null,
        @Query("episode") episode: Int? = null,
        @Query("provider") provider: String = "auto",
        @Query("dubLang") dubLang: String? = null
    ): StreamData

    @POST("stream")
    suspend fun postStreamUrl(
        @Body request: StreamRequest
    ): StreamData
}

@kotlinx.serialization.Serializable
data class StreamRequest(
    val title: String,
    val year: String? = null,
    val season: Int? = null,
    val episode: Int? = null,
    val provider: String = "auto",
    val dubLang: String? = null
)
