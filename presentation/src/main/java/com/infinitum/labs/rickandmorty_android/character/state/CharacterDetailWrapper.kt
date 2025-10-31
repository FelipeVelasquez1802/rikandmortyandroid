package com.infinitum.labs.rickandmorty_android.character.state

import com.infinitum.labs.domain.character.model.Character

internal sealed interface CharacterDetailWrapper {
    data class UiState(
        val character: Character? = null,
        val isLoading: Boolean = false,
        val error: String? = null
    )

    sealed interface Event {
        // User interaction events
        data object OnBackClick : Event
        data object OnRetryClick : Event

        // One-time events (handled in UI layer)
        data object NavigateBack : Event
        data class ShowError(val message: String) : Event
    }
}