package com.example.sangitplayer.navigation

interface Destination {
    val route: String
}

object Home: Destination{
    override val route: String = "Home"
}

object NowPlaying: Destination{
    override val route: String = "NowPlaying"

}