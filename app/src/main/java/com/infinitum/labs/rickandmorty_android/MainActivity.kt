package com.infinitum.labs.rickandmorty_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.infinitum.labs.rickandmorty_android.list.CharacterListScreen
import com.infinitum.labs.rickandmorty_android.ui.theme.RickAndMortyAndroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RickAndMortyAndroidTheme {
                CharacterListScreen()
            }
        }
    }
}