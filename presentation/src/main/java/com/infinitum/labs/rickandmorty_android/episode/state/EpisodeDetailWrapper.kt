package com.infinitum.labs.rickandmorty_android.episode.state

import com.infinitum.labs.domain.episode.model.Episode

internal sealed interface EpisodeDetailWrapper {
    data class UiState(
        val episode: Episode? = null,
        val isLoading: Boolean = false,
        val error: String? = null
    )

    sealed interface Event {
        // User interaction events
        data object Retry : Event
        data object NavigateBack : Event

        // One-time events (handled in UI layer)
        data class ShowError(val message: String) : Event
    }
}