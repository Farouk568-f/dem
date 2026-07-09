package com.cinestream

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.cinestream.data.api.RetrofitClient
import com.cinestream.data.repository.MovieRepository
import com.cinestream.domain.auth.AuthService
import com.cinestream.ui.navigation.CineStreamApp
import com.cinestream.ui.theme.CineStreamTheme

class MainActivity : ComponentActivity() {
    private lateinit var authService: AuthService
    private lateinit var movieRepository: MovieRepository
    private lateinit var encryptedPreferences: EncryptedSharedPreferences
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize encrypted shared preferences
        val masterKey = MasterKey.Builder(this)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        
        encryptedPreferences = EncryptedSharedPreferences.create(
            this,
            "cinestream_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        ) as EncryptedSharedPreferences
        
        // Initialize services
        val retrofitClient = RetrofitClient(encryptedPreferences)
        movieRepository = MovieRepository(retrofitClient.tmdbApi)
        authService = AuthService(encryptedPreferences)
        
        setContent {
            CineStreamTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CineStreamApp()
                }
            }
        }
    }
}
