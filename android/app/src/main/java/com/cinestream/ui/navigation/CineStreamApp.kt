package com.cinestream.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController

@Composable
fun CineStreamApp() {
    val navController = rememberNavController()
    CineStreamNavGraph(navController = navController)
}
