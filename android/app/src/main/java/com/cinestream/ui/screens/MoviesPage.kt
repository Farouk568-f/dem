package com.cinestream.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
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
import com.cinestream.ui.viewmodel.MoviesViewModel
import com.cinestream.utils.getBackdropImageUrl

@Composable
fun MoviesPage(
    navController: NavHostController,
    moviesViewModel: MoviesViewModel? = null
) {
    val movies by remember { mutableStateOf(emptyList<Movie>()) }
    val isLoading by remember { mutableStateOf(false) }
    val error by remember { mutableStateOf<String?>(null) }
    var showFilterMenu by remember { mutableStateOf(false) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top Bar
        TopAppBar(
            title = { Text("Movies") },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            actions = {
                IconButton(onClick = { showFilterMenu = !showFilterMenu }) {
                    Icon(Icons.Default.FilterList, contentDescription = "Filter")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )
        
        if (showFilterMenu) {
            FilterBar(
                onSortChange = { moviesViewModel?.setSortBy(it) },
                onDismiss = { showFilterMenu = false }
            )
        }
        
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
                        text = "Error loading movies",
                        color = MaterialTheme.colorScheme.error
                    )
                    Button(onClick = { moviesViewModel?.refresh() }) {
                        Text("Retry")
                    }
                }
            }
            movies.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No movies found")
                }
            }
            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(movies) { movie ->
                        MovieGridCard(
                            movie = movie,
                            onClick = {
                                navController.navigate(
                                    Route.Details.createRoute(movie.id, "movie")
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FilterBar(
    onSortChange: (String) -> Unit,
    onDismiss: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val sorts = listOf("popularity.desc", "rating.desc", "release_date.desc")
        
        sorts.forEach { sort ->
            FilterChip(
                selected = false,
                onClick = { 
                    onSortChange(sort)
                    onDismiss()
                },
                label = { Text(sort.replace(".desc", "").replaceFirstChar { it.uppercase() }) }
            )
        }
    }
}

@Composable
private fun MovieGridCard(
    movie: Movie,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .aspectRatio(0.67f)
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
            
            // Rating overlay
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(8.dp),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = "⭐ ${movie.vote_average}",
                    modifier = Modifier.padding(4.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}
