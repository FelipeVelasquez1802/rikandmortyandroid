package com.infinitum.labs.rickandmorty_android.common.navigation

import kotlinx.serialization.Serializable

internal sealed interface NavigationRoute {

    @Serializable
    data object Splash : NavigationRoute

    @Serializable
    data object Main : NavigationRoute

    @Serializable
    data object CharacterList : NavigationRoute

    @Serializable
    data class CharacterDetail(val characterId: Int) : NavigationRoute

    @Serializable
    data object LocationList : NavigationRoute

    @Serializable
    data object EpisodeList : NavigationRoute
}