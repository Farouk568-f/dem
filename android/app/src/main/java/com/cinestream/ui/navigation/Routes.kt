package com.cinestream.ui.navigation

sealed class Route(val route: String) {
    object Login : Route("login")
    object Profile : Route("profile")
    object Home : Route("home")
    object Movies : Route("movies")
    object TVShows : Route("tvshows")
    object Search : Route("search")
    object Discovery : Route("discovery")
    object AISearch : Route("ai_search")
    object Player : Route("player/{movieId}/{mediaType}") {
        fun createRoute(movieId: Int, mediaType: String) = "player/$movieId/$mediaType"
    }
    object Details : Route("details/{movieId}/{mediaType}") {
        fun createRoute(movieId: Int, mediaType: String) = "details/$movieId/$mediaType"
    }
    object ActorDetails : Route("actor/{actorId}") {
        fun createRoute(actorId: Int) = "actor/$actorId"
    }
    object Favorites : Route("favorites")
    object History : Route("history")
    object Settings : Route("settings")
    object Addons : Route("addons")
    object Cinema : Route("cinema")
    object IPTV : Route("iptv")
    object Shorts : Route("shorts")
    object LiveRoom : Route("liveroom")
    object Admin : Route("admin")
    object Studio : Route("studio")
    object QRApprove : Route("qr_approve")
    object You : Route("you")
}
