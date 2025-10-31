package com.infinitum.labs.rickandmorty_android.common.navigation

import kotlinx.serialization.Serializable

/**
 * Type-safe navigation routes using kotlinx.serialization.
 * All routes are marked as internal to maintain encapsulation.
 */
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