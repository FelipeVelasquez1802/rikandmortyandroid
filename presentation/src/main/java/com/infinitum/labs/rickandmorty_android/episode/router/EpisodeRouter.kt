package com.infinitum.labs.rickandmorty_android.episode.router

import com.infinitum.labs.rickandmorty_android.common.router.BaseRouter

internal sealed class EpisodeRouter : BaseRouter {
    data class NavigateToDetail(val episodeId: Int) : EpisodeRouter()
    data object NavigateBack : EpisodeRouter()
}