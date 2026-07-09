package com.cinestream.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.New
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.cinestream.data.api.models.Movie
import com.cinestream.ui.navigation.Route
import com.cinestream.utils.getBackdropImageUrl

@Composable
fun DiscoveryPage(
    navController: NavHostController
) {
    val trendingMovies by remember { mutableStateOf(emptyList<Movie>()) }
    val topRatedMovies by remember { mutableStateOf(emptyList<Movie>()) }
    val upcomingMovies by remember { mutableStateOf(emptyList<Movie>()) }
    val isLoading by remember { mutableStateOf(false) }
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        // Header
        item {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Discover",
                    style = MaterialTheme.typography.headlineLarge
                )
                Text(
                    text = "Find your next favorite movie or show",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        // Quick Filters
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(androidx.compose.foundation.rememberScrollState())
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val filters = listOf("Action", "Comedy", "Drama", "Horror", "Sci-Fi")
                filters.forEach { filter ->
                    FilterChip(
                        selected = false,
                        onClick = { /* Navigate to filtered list */ },
                        label = { Text(filter) }
                    )
                }
            }
        }
        
        // Trending Section
        if (trendingMovies.isNotEmpty()) {
            item {
                DiscoverySection(
                    title = "Trending Now",
                    icon = Icons.Default.TrendingUp,
                    movies = trendingMovies,
                    onMovieClick = { movie ->
                        navController.navigate(Route.Details.createRoute(movie.id, "movie"))
                    }
                )
            }
        }
        
        // Top Rated Section
        if (topRatedMovies.isNotEmpty()) {
            item {
                DiscoverySection(
                    title = "Top Rated",
                    icon = Icons.Default.Star,
                    movies = topRatedMovies,
                    onMovieClick = { movie ->
                        navController.navigate(Route.Details.createRoute(movie.id, "movie"))
                    }
                )
            }
        }
        
        // Upcoming Section
        if (upcomingMovies.isNotEmpty()) {
            item {
                DiscoverySection(
                    title = "Coming Soon",
                    icon = Icons.Default.New,
                    movies = upcomingMovies,
                    onMovieClick = { movie ->
                        navController.navigate(Route.Details.createRoute(movie.id, "movie"))
                    }
                )
            }
        }
        
        // Loading State
        if (isLoading && trendingMovies.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
private fun DiscoverySection(
    title: String,
    icon: androidx.compose.material.icons.rounded.Auto,
    movies: List<Movie>,
    onMovieClick: (Movie) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icon as androidx.compose.material.icons.materialIcon,
                contentDescription = title,
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
        }
        
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(movies.take(6)) { movie ->
                DiscoveryMovieCard(
                    movie = movie,
                    onClick = { onMovieClick(movie) }
                )
            }
        }
    }
}

@Composable
private fun DiscoveryMovieCard(
    movie: Movie,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(140.dp)
            .height(210.dp)
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = movie.backdrop_path.getBackdropImageUrl("w200"),
                contentDescription = movie.getDisplayTitle(),
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            
            // Rating badge
            Surface(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = "⭐ ${movie.vote_average}",
                    modifier = Modifier.padding(4.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
            
            // Title overlay
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(),
                color = MaterialTheme.colorScheme.scrim.copy(alpha = 0.7f)
            ) {
                Text(
                    text = movie.getDisplayTitle(),
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

// Note: This is a simplified version. In production, we would:
// 1. Create proper icon references
// 2. Load actual data from ViewModels
// 3. Implement real genre filtering
// 4. Add infinite scroll pagination
