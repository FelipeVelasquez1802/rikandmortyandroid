package com.infinitum.labs.rickandmorty_android.episode.state

import com.infinitum.labs.domain.episode.model.Episode

internal sealed interface EpisodeListWrapper {
    data class UiState(
        val episodes: List<Episode> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null,
        val currentPage: Int = 1,
        val canLoadMore: Boolean = true
    )

    sealed interface Event {
        // User interaction events
        data object Retry : Event
        data object LoadNextPage : Event
        data class OnEpisodeClick(val episodeId: Int) : Event

        // One-time events (handled in UI layer)
        data class NavigateToDetail(val episodeId: Int) : Event
        data class ShowError(val message: String) : Event
    }
}