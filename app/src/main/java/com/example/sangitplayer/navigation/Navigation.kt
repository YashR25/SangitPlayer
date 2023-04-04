package com.example.sangitplayer.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sangitplayer.screens.HomeScreen
import com.example.sangitplayer.screens.PlayerScreen

@Composable
fun Navigation(navController: NavHostController){

    NavHost(navController = navController, startDestination = Home.route){
        composable(Home.route){
            HomeScreen(navController)
        }
        composable(NowPlaying.route){
            PlayerScreen()
        }
    }
}