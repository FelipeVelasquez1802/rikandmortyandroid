package com.infinitum.labs.rickandmorty_android.location.state

import com.infinitum.labs.domain.location.model.Location

internal sealed interface LocationDetailWrapper {
    data class UiState(
        val location: Location? = null,
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