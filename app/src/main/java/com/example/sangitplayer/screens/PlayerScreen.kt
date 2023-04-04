package com.example.sangitplayer.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.sangitplayer.R
import com.example.sangitplayer.component.CircularSlider
import com.example.sangitplayer.component.TopBar
import com.example.sangitplayer.ui.theme.charcoal

@Composable
fun PlayerScreen() {
    var sliderProgress by remember {
        mutableStateOf(0.5f)
    }
    Scaffold(topBar = { TopBar(title = "Play", shouldShowSearch = false)}) {
        Column(modifier = Modifier
            .padding(it)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly) {
            Box(modifier = Modifier
                .fillMaxWidth(0.8f)
                .fillMaxHeight(0.5f), contentAlignment = Alignment.Center){
                CircularSlider(padding = 0f,
                    modifier = Modifier.fillMaxSize(),
                    stroke = 30f,
                thumbColor = MaterialTheme.colors.primary,
                progressColor = MaterialTheme.colors.primary)
                Image(painter = painterResource(id = R.drawable.example),
                    contentDescription = "Song Image",
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .clip(CircleShape),
                    contentScale = ContentScale.FillWidth)
            }
            Text(text = "2:45/3:25", style = MaterialTheme.typography.body2)
            Text(text = "Two Weeks / Pendulum", style = MaterialTheme.typography.subtitle2, color = MaterialTheme.colors.primary)
            Text(text = "X1", style = MaterialTheme.typography.button)
            Text(text = "FKA twigs", style = MaterialTheme.typography.body2)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(painter = painterResource(id = R.drawable.baseline_repeat_24), contentDescription = "Next")
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(painter = painterResource(id = R.drawable.baseline_skip_previous_24),
                        contentDescription = "Previous",
                        modifier = Modifier.size(40.dp))
                }
                IconButton(onClick = { /*TODO*/ }, Modifier.background(shape = CircleShape,
                    color = MaterialTheme.colors.primary)) {
                    Icon(painter = painterResource(id = R.drawable.baseline_play_arrow_24),
                        contentDescription = "Play",
                        modifier = Modifier
                            .size(70.dp)
                            .padding(8.dp))
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(painter = painterResource(id = R.drawable.baseline_skip_next_24),
                        contentDescription = "Next",
                        modifier = Modifier.size(40.dp))
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(painter = painterResource(id = R.drawable.baseline_shuffle_24), contentDescription = "Shuffle")
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(painter = painterResource(id = R.drawable.baseline_volume_down_24), contentDescription = "Shuffle")
                }
                Slider(value = sliderProgress,
                    onValueChange = {progress -> sliderProgress = progress},
                    colors = SliderDefaults.colors(
                    inactiveTrackColor = charcoal,
                    activeTickColor = MaterialTheme.colors.primary,
                    thumbColor = MaterialTheme.colors.primary
                ),
                modifier = Modifier.fillMaxWidth(0.8f))
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(painter = painterResource(id = R.drawable.baseline_volume_up_24), contentDescription = "Shuffle")
                }
            }

        }
    }
}