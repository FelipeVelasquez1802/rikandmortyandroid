package com.infinitum.labs.rickandmorty_android.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.infinitum.labs.rickandmorty_android.character.router.CharacterRouter
import com.infinitum.labs.rickandmorty_android.character.view.CharacterListScreen
import com.infinitum.labs.rickandmorty_android.episode.router.EpisodeRouter
import com.infinitum.labs.rickandmorty_android.episode.view.EpisodeListScreen
import com.infinitum.labs.rickandmorty_android.location.router.LocationRouter
import com.infinitum.labs.rickandmorty_android.location.view.LocationListScreen

@Suppress("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
internal fun MainScreen(
    onNavigate: (CharacterRouter) -> Unit = {},
    onNavigateLocation: (LocationRouter) -> Unit = {},
    onNavigateEpisode: (EpisodeRouter) -> Unit = {}
) {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        }
    ) { _ ->
        when (selectedTab) {
            0 -> CharacterListScreen(
                onGoTo = onNavigate
            )
            1 -> LocationListScreen(
                onGoTo = onNavigateLocation
            )
            2 -> EpisodeListScreen(
                onGoTo = onNavigateEpisode
            )
        }
    }
}

@Composable
private fun BottomNavigationBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    val items = listOf(
        BottomNavItem("Characters", Icons.AutoMirrored.Filled.List),
        BottomNavItem("Locations", Icons.Default.LocationOn),
        BottomNavItem("Episodes", Icons.Default.PlayArrow)
    )

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = selectedTab == index,
                onClick = { onTabSelected(index) }
            )
        }
    }
}

private data class BottomNavItem(
    val label: String,
    val icon: ImageVector
)