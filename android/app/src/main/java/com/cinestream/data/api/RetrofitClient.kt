package com.cinestream.data.api

import android.content.SharedPreferences
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import okhttp3.MediaType.Companion.toMediaType
import java.util.concurrent.TimeUnit

class RetrofitClient(private val preferences: SharedPreferences) {
    
    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    private val authInterceptor = Interceptor { chain ->
        val request = chain.request()
        val newRequest = request.newBuilder()
            .addHeader("User-Agent", "CineStream/1.0")
            .addHeader("Accept", "application/json")
            .build()
        chain.proceed(newRequest)
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
    }

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()

    val tmdbApi: CineStreamApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .client(httpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(CineStreamApiService::class.java)
    }

    val scraperApi: ScraperApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.consumet.org/movies/")
            .client(httpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(ScraperApiService::class.java)
    }
}
