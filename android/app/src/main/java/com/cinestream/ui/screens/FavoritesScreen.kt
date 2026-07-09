package com.cinestream.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.cinestream.ui.navigation.Route
import com.cinestream.ui.viewmodel.FavoritesViewModel
import com.cinestream.ui.viewmodel.FavoriteItem
import com.cinestream.utils.getBackdropImageUrl

@Composable
fun FavoritesScreen(
    navController: NavHostController,
    favoritesViewModel: FavoritesViewModel? = null
) {
    val favorites by remember { mutableStateOf(emptyList<FavoriteItem>()) }
    val isLoading by remember { mutableStateOf(false) }
    val error by remember { mutableStateOf<String?>(null) }
    val sortOption by remember { mutableStateOf("recent") }
    val filterType by remember { mutableStateOf<String?>(null) }
    var showSortMenu by remember { mutableStateOf(false) }
    var showFilterMenu by remember { mutableStateOf(false) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top Bar
        TopAppBar(
            title = { 
                Column {
                    Text("Favorites")
                    if (favorites.isNotEmpty()) {
                        Text(
                            text = "${favorites.size} item${if (favorites.size != 1) "s" else ""}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            },
            actions = {
                IconButton(onClick = { showSortMenu = !showSortMenu }) {
                    Icon(Icons.Default.Sort, contentDescription = "Sort")
                }
                IconButton(onClick = { showFilterMenu = !showFilterMenu }) {
                    Icon(Icons.Default.FilterList, contentDescription = "Filter")
                }
                if (favorites.isNotEmpty()) {
                    IconButton(onClick = { favoritesViewModel?.clearAllFavorites() }) {
                        Icon(Icons.Default.Delete, contentDescription = "Clear All")
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )
        
        // Sort Menu
        if (showSortMenu) {
            SortFilterBar(
                title = "Sort by",
                options = listOf("recent", "oldest", "rating", "alphabetical"),
                selectedOption = sortOption,
                onOptionSelected = { 
                    favoritesViewModel?.setSortOption(it)
                    showSortMenu = false
                },
                onDismiss = { showSortMenu = false }
            )
        }
        
        // Filter Menu
        if (showFilterMenu) {
            SortFilterBar(
                title = "Filter by",
                options = listOf("All", "Movie", "TV"),
                selectedOption = filterType ?: "All",
                onOptionSelected = { 
                    favoritesViewModel?.setFilterType(if (it == "All") null else it)
                    showFilterMenu = false
                },
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
                        text = "Error loading favorites",
                        color = MaterialTheme.colorScheme.error
                    )
                    Button(onClick = { favoritesViewModel?.refresh() }) {
                        Text("Retry")
                    }
                }
            }
            favorites.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            Icons.Default.FavoriteBorder,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            "No favorites yet",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            "Add movies or shows to your favorites",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
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
                    items(favorites) { favorite ->
                        FavoriteCard(
                            favorite = favorite,
                            onCardClick = {
                                navController.navigate(
                                    Route.Details.createRoute(favorite.id, favorite.mediaType)
                                )
                            },
                            onRemoveClick = {
                                favoritesViewModel?.removeFavorite(favorite.id, favorite.mediaType)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FavoriteCard(
    favorite: FavoriteItem,
    onCardClick: () -> Unit,
    onRemoveClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .aspectRatio(0.67f)
            .clickable(onClick = onCardClick),
        shape = MaterialTheme.shapes.medium
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = favorite.posterPath.getBackdropImageUrl("w200"),
                contentDescription = favorite.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            
            // Remove button
            IconButton(
                onClick = onRemoveClick,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(4.dp)
                    .size(32.dp)
            ) {
                Icon(
                    Icons.Default.Favorite,
                    contentDescription = "Remove from favorites",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(20.dp)
                )
            }
            
            // Title and rating overlay
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(),
                color = MaterialTheme.colorScheme.scrim.copy(alpha = 0.7f)
            ) {
                Column(
                    modifier = Modifier.padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = favorite.title,
                        style = MaterialTheme.typography.labelSmall,
                        maxLines = 1,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "⭐ ${favorite.rating}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun SortFilterBar(
    title: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.weight(1f)
        )
        options.forEach { option ->
            FilterChip(
                selected = option == selectedOption,
                onClick = { onOptionSelected(option) },
                label = { Text(option.replaceFirstChar { it.uppercase() }) }
            )
        }
    }
}
