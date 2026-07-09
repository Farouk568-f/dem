package com.cinestream.di

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cinestream.data.api.RetrofitClient
import com.cinestream.data.repository.MovieRepository
import com.cinestream.ui.viewmodel.*

/**
 * Simple app-wide dependency container (singleton).
 * Mirrors how the Vite app shares a single apiService module.
 */
object AppContainer {

    @Volatile
    private var retrofitClient: RetrofitClient? = null

    @Volatile
    private var movieRepository: MovieRepository? = null

    fun init(context: Context) {
        if (retrofitClient == null) {
            synchronized(this) {
                if (retrofitClient == null) {
                    val prefs: SharedPreferences =
                        context.applicationContext.getSharedPreferences("cinestream_app", Context.MODE_PRIVATE)
                    val client = RetrofitClient(prefs)
                    retrofitClient = client
                    movieRepository = MovieRepository(client.tmdbApi)
                }
            }
        }
    }

    fun repository(context: Context): MovieRepository {
        init(context)
        return movieRepository!!
    }

    fun retrofit(context: Context): RetrofitClient {
        init(context)
        return retrofitClient!!
    }
}

/**
 * Generic ViewModel factory wired to the shared MovieRepository.
 */
class CineStreamViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repo = AppContainer.repository(context)
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(repo) as T
            modelClass.isAssignableFrom(MoviesViewModel::class.java) -> MoviesViewModel(repo) as T
            modelClass.isAssignableFrom(TVShowsViewModel::class.java) -> TVShowsViewModel(repo) as T
            modelClass.isAssignableFrom(SearchViewModel::class.java) -> SearchViewModel(repo) as T
            modelClass.isAssignableFrom(CinemaViewModel::class.java) -> CinemaViewModel(repo) as T
            modelClass.isAssignableFrom(FavoritesViewModel::class.java) -> FavoritesViewModel(repo) as T
            modelClass.isAssignableFrom(HistoryViewModel::class.java) -> HistoryViewModel(repo) as T
            modelClass.isAssignableFrom(PlayerViewModel::class.java) -> PlayerViewModel(repo) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
