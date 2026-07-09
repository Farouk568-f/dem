package com.cinestream.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PlayArrow
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
import com.cinestream.ui.viewmodel.DetailsViewModel
import com.cinestream.utils.formatRating
import com.cinestream.utils.getBackdropImageUrl

@Composable
fun DetailsPage(
    navController: NavHostController,
    movieId: Int,
    mediaType: String,
    detailsViewModel: DetailsViewModel? = null
) {
    val details by remember { mutableStateOf<Movie?>(null) }
    val isLoading by remember { mutableStateOf(false) }
    val error by remember { mutableStateOf<String?>(null) }
    val isFavorite by remember { mutableStateOf(false) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top Bar
        TopAppBar(
            title = { Text(details?.getDisplayTitle() ?: "Details") },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            actions = {
                IconButton(onClick = { detailsViewModel?.toggleFavorite() }) {
                    Icon(
                        if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) MaterialTheme.colorScheme.primary 
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )
        
        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
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
                        text = "Error loading details",
                        color = MaterialTheme.colorScheme.error
                    )
                    Button(onClick = { detailsViewModel?.refresh() }) {
                        Text("Retry")
                    }
                }
            }
            details != null -> {
                DetailsContent(
                    movie = details!!,
                    mediaType = mediaType,
                    onPlayClick = {
                        navController.navigate(
                            Route.Player.createRoute(movieId, mediaType)
                        )
                    },
                    onActorClick = { actorId ->
                        navController.navigate(
                            Route.ActorDetails.createRoute(actorId)
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun DetailsContent(
    movie: Movie,
    mediaType: String,
    onPlayClick: () -> Unit,
    onActorClick: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        // Backdrop
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            ) {
                AsyncImage(
                    model = movie.backdrop_path.getBackdropImageUrl(),
                    contentDescription = movie.getDisplayTitle(),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                
                Button(
                    onClick = onPlayClick,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(56.dp),
                    shape = MaterialTheme.shapes.extraLarge
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = "Play")
                }
            }
        }
        
        // Title and Rating
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = movie.getDisplayTitle(),
                    style = MaterialTheme.typography.headlineMedium
                )
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = MaterialTheme.colorScheme.primary,
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            text = movie.vote_average.formatRating(),
                            modifier = Modifier.padding(6.dp),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    
                    Text(
                        text = movie.getDisplayYear(),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    
                    movie.runtime?.let {
                        Text(
                            text = "${it}m",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
        
        // Overview
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Overview",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = movie.overview,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        
        // Genres
        movie.genres?.let { genres ->
            if (genres.isNotEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Genres",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            genres.forEach { genre ->
                                AssistChip(
                                    onClick = { },
                                    label = { Text(genre.name) }
                                )
                            }
                        }
                    }
                }
            }
        }
        
        // Cast
        movie.credits?.let { credits ->
            if (credits.cast.isNotEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Cast",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp)
                        ) {
                            items(credits.cast) { actor ->
                                CastCard(
                                    name = actor.name,
                                    character = actor.character ?: "",
                                    onClick = { /* Handle click */ }
                                )
                            }
                        }
                    }
                }
            }
        }
        
        // Recommendations
        movie.recommendations?.let { recommendations ->
            if (recommendations.results.isNotEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Recommendations",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp)
                        ) {
                            items(recommendations.results) { item ->
                                Card(
                                    modifier = Modifier
                                        .width(100.dp)
                                        .height(150.dp)
                                        .clickable { /* Navigate to details */ },
                                    shape = MaterialTheme.shapes.medium
                                ) {
                                    AsyncImage(
                                        model = item.backdrop_path.getBackdropImageUrl("w200"),
                                        contentDescription = item.getDisplayTitle(),
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CastCard(
    name: String,
    character: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(120.dp)
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.labelSmall,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            Text(
                text = character,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
    }
}
