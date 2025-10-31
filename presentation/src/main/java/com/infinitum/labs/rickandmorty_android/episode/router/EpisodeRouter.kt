package com.infinitum.labs.rickandmorty_android.episode.router

import com.infinitum.labs.rickandmorty_android.common.router.BaseRouter

/**
 * Episode feature navigation actions.
 * These are the possible navigation events from episode screens.
 */
internal sealed class EpisodeRouter : BaseRouter {
    data class NavigateToDetail(val episodeId: Int) : EpisodeRouter()
    data object NavigateBack : EpisodeRouter()
}