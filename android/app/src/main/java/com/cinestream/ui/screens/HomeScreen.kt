package com.cinestream.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.cinestream.data.api.models.Movie
import com.cinestream.ui.navigation.Route
import com.cinestream.ui.viewmodel.HomeViewModel
import com.cinestream.utils.getBackdropImageUrl

@Composable
fun HomeScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel? = null
) {
    val popularMovies by remember { mutableStateOf(emptyList<Movie>()) }
    val popularTV by remember { mutableStateOf(emptyList<Movie>()) }
    val isLoading by remember { mutableStateOf(false) }
    val error by remember { mutableStateOf<String?>(null) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        // Top Bar
        TopAppBar(
            title = { Text("CineStream") },
            actions = {
                IconButton(onClick = { navController.navigate(Route.Settings.route) }) {
                    Icon(Icons.Default.Settings, contentDescription = "Settings")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )
        
        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            error != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Error loading content",
                        color = MaterialTheme.colorScheme.error
                    )
                    Button(onClick = { homeViewModel?.refresh() }) {
                        Text("Retry")
                    }
                }
            }
            else -> {
                ContentSection(
                    title = "Popular Movies",
                    items = popularMovies,
                    onItemClick = { movie ->
                        navController.navigate(
                            Route.Player.createRoute(movie.id, "movie")
                        )
                    }
                )
                
                ContentSection(
                    title = "Popular TV Shows",
                    items = popularTV,
                    onItemClick = { movie ->
                        navController.navigate(
                            Route.Player.createRoute(movie.id, "tv")
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun ContentSection(
    title: String,
    items: List<Movie>,
    onItemClick: (Movie) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            items(items) { movie ->
                MovieCard(
                    movie = movie,
                    onClick = { onItemClick(movie) }
                )
            }
        }
    }
}

@Composable
private fun MovieCard(
    movie: Movie,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .height(225.dp)
            .clickable { onClick() },
        shape = MaterialTheme.shapes.medium
    ) {
        AsyncImage(
            model = movie.backdrop_path.getBackdropImageUrl("w300"),
            contentDescription = movie.getDisplayTitle(),
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}

private fun clickable(onClick: () -> Unit) = Modifier
