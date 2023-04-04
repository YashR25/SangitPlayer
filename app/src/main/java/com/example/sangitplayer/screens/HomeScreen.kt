package com.example.sangitplayer.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.sangitplayer.component.NowPlayingSong
import com.example.sangitplayer.component.RecentlyPlayedSongItem
import com.example.sangitplayer.component.SongItem
import com.example.sangitplayer.component.TopBar

@Composable
fun HomeScreen(navController: NavHostController) {
    val scrollState = rememberScrollState()
    val columnScrollState = rememberScrollState()
    Scaffold(topBar = { TopBar(title = "Sangit", shouldShowSearch = true)}, bottomBar = { NowPlayingSong(navController)}) { padding ->
        Column(modifier = Modifier.padding(horizontal = 8.dp)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Recently Played", style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.padding(vertical = 8.dp))
                Text(text = "See all >", style = MaterialTheme.typography.body2)
            }
            Row(
                Modifier
                    .horizontalScroll(enabled = true, state = scrollState)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                RecentlyPlayedSongItem()
                RecentlyPlayedSongItem()
                RecentlyPlayedSongItem()
                RecentlyPlayedSongItem()
            }
            Text(text = "Song List", style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.padding(vertical = 8.dp))
            Column(Modifier.verticalScroll(columnScrollState)) {
                SongItem()
                SongItem()
                SongItem()
                SongItem()
                SongItem()
                SongItem()
                SongItem()
            }
        }
    }

}