package com.cinestream.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.cinestream.data.api.models.Actor
import com.cinestream.ui.viewmodel.ActorViewModel
import com.cinestream.utils.getBackdropImageUrl

@Composable
fun ActorDetailsPage(
    navController: NavHostController,
    actorId: Int,
    actorViewModel: ActorViewModel? = null
) {
    val actor by remember { mutableStateOf<Actor?>(null) }
    val isLoading by remember { mutableStateOf(false) }
    val error by remember { mutableStateOf<String?>(null) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top Bar
        TopAppBar(
            title = { Text(actor?.name ?: "Actor") },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
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
                        text = "Error loading actor",
                        color = MaterialTheme.colorScheme.error
                    )
                    Button(onClick = { actorViewModel?.refresh() }) {
                        Text("Retry")
                    }
                }
            }
            actor != null -> {
                ActorContent(actor!!)
            }
        }
    }
}

@Composable
private fun ActorContent(actor: Actor) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        // Profile Image
        item {
            actor.images?.let { images ->
                if (images.profiles.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                    ) {
                        AsyncImage(
                            model = "https://image.tmdb.org/t/p/w500${images.profiles[0].file_path}",
                            contentDescription = actor.name,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }
        
        // Actor Info
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = actor.name,
                    style = MaterialTheme.typography.headlineMedium
                )
                
                actor.birthday?.let {
                    Text(
                        text = "Born: $it",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                
                actor.place_of_birth?.let {
                    Text(
                        text = "Birthplace: $it",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                
                if (actor.known_for_department.isNotEmpty()) {
                    Text(
                        text = "Known for: ${actor.known_for_department}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                
                if (actor.popularity > 0) {
                    LinearProgressIndicator(
                        progress = (actor.popularity / 100).coerceAtMost(1f),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
        
        // Biography
        if (actor.biography.isNotEmpty()) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Biography",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = actor.biography,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
        
        // Filmography
        actor.combined_credits?.let { credits ->
            if (credits.cast.isNotEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Filmography (${credits.cast.size})",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp)
                        ) {
                            items(credits.cast.take(10)) { movie ->
                                Card(
                                    modifier = Modifier
                                        .width(120.dp)
                                        .height(180.dp),
                                    shape = MaterialTheme.shapes.medium
                                ) {
                                    AsyncImage(
                                        model = movie.backdrop_path.getBackdropImageUrl("w200"),
                                        contentDescription = movie.getDisplayTitle(),
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
