package com.example.sangitplayer.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.sangitplayer.R
import com.example.sangitplayer.navigation.NowPlaying
import com.example.sangitplayer.screens.PlayerScreen

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NowPlayingSong(navController: NavHostController) {
    BottomAppBar(
        elevation = 8.dp,
        contentPadding = PaddingValues(all = 8.dp),
        backgroundColor = MaterialTheme.colors.primary,
        modifier = Modifier
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            .clickable {
                navController.navigate(NowPlaying.route)
            }
    ){
        Row(horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Image(painter = painterResource(id = R.drawable.example),
                    contentDescription = "Song Image",
                    Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .size(50.dp),
                contentScale = ContentScale.FillWidth)
                Column() {
                    Text(text = "Two Weeks / Pendulum", style = MaterialTheme.typography.body1)
                    Text(text = "FKA twigs", style = MaterialTheme.typography.body2)
                }
            }
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Play Button")
            }
        }
    }
}

@Composable
fun RecentlyPlayedSongItem(){
    Column(modifier = Modifier
        .width(100.dp)) {
            Card(elevation = 10.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 25.dp,
                        ambientColor = Color.Blue,
                        spotColor = Color.Cyan,
                        shape = RoundedCornerShape(20.dp)
                    ),
                shape = RoundedCornerShape(10.dp)
            ) {

                Image(painter = painterResource(id = R.drawable.example), contentDescription = "Song Image",
                    contentScale = ContentScale.FillWidth)

            }
        Text(text = "Song Name", style = MaterialTheme.typography.body1)
        Text(text = "Artist Name", style = MaterialTheme.typography.body2)

    }


}

@Composable
fun SongItem(){
    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Image(painter = painterResource(id = R.drawable.example),
                contentDescription = "Song Image",
                Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .size(50.dp))
            Column() {
                Text(text = "Two Weeks / Pendulum", style = MaterialTheme.typography.body1)
                Text(text = "FKA twigs", style = MaterialTheme.typography.body2)
            }
        }

        Text(text = "5:32", style = MaterialTheme.typography.body1)
    }

}

@Composable
fun TopBar(title: String, shouldShowSearch: Boolean = false){
    Box(modifier = Modifier
        .fillMaxWidth()
        .size(80.dp)
        .padding(8.dp),
        contentAlignment = Alignment.CenterEnd) {
        Text(text = title, style = MaterialTheme.typography.h1, modifier = Modifier.align(Alignment.CenterStart))
        if(shouldShowSearch){
            Box(modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .padding(5.dp)
                .background(MaterialTheme.colors.primary), contentAlignment = Alignment.Center) {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon")
                }
            }
        }

    }
}

@Composable
@Preview(showBackground = true)
fun RecentlyPlayedSongItemPreview(){
    RecentlyPlayedSongItem()
}

@Composable
@Preview(showBackground = true)
fun SongItemPreview(){
    SongItem()
}

@Composable
@Preview
fun NowPlayingSongPreview(){
//    NowPlayingSong()
}
