package com.cinestream.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.cinestream.ui.components.CineStreamBottomNav
import com.cinestream.ui.screens.*

@Composable
fun CineStreamNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Route.Login.route
    ) {
        composable(Route.Login.route) {
            LoginScreen(navController = navController)
        }
        composable(Route.Profile.route) {
            ProfileScreen(navController = navController)
        }
        composable(Route.Home.route) {
            MainContent(navController, Route.Home.route)
        }
        composable(Route.Search.route) {
            MainContent(navController, Route.Search.route)
        }
        composable(Route.Favorites.route) {
            MainContent(navController, Route.Favorites.route)
        }
        composable(Route.Settings.route) {
            MainContent(navController, Route.Settings.route)
        }
        composable(Route.Movies.route) {
            MoviesPage(navController)
        }
        composable(Route.TVShows.route) {
            TVShowsPage(navController)
        }
        composable(
            Route.Details.route,
            arguments = listOf(
                androidx.navigation.navArgument("movieId") { 
                    type = androidx.navigation.NavType.IntType 
                },
                androidx.navigation.navArgument("mediaType") { 
                    type = androidx.navigation.NavType.StringType 
                }
            )
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt("movieId") ?: 0
            val mediaType = backStackEntry.arguments?.getString("mediaType") ?: "movie"
            DetailsPage(navController, movieId, mediaType)
        }
        composable(
            Route.ActorDetails.route,
            arguments = listOf(
                androidx.navigation.navArgument("actorId") { 
                    type = androidx.navigation.NavType.IntType 
                }
            )
        ) { backStackEntry ->
            val actorId = backStackEntry.arguments?.getInt("actorId") ?: 0
            ActorDetailsPage(navController, actorId)
        }
        composable(Route.Search.route) {
            SearchScreen(navController)
        }
        composable(Route.Cinema.route) {
            CinemaPage(navController)
        }
        composable(Route.Discovery.route) {
            DiscoveryPage(navController)
        }
        composable(
            Route.Player.route,
            arguments = listOf(
                androidx.navigation.navArgument("movieId") { 
                    type = androidx.navigation.NavType.IntType 
                },
                androidx.navigation.navArgument("mediaType") { 
                    type = androidx.navigation.NavType.StringType 
                }
            )
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt("movieId") ?: 0
            val mediaType = backStackEntry.arguments?.getString("mediaType") ?: "movie"
            PlayerScreen(navController, movieId, mediaType)
        }
        composable(Route.Favorites.route) {
            FavoritesScreen(navController)
        }
        composable(Route.History.route) {
            HistoryScreen(navController)
        }
    }
}

@Composable
private fun MainContent(navController: NavHostController, currentRoute: String) {
    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.weight(1f)) {
            when (currentRoute) {
                Route.Home.route -> HomeScreen(navController)
                Route.Search.route -> SearchScreen(navController)
                Route.Favorites.route -> FavoritesScreen(navController)
                Route.Settings.route -> SettingsScreen(navController)
                else -> HomeScreen(navController)
            }
        }
        CineStreamBottomNav(navController)
    }
}

@Composable
fun FavoritesScreen(navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize())
}
