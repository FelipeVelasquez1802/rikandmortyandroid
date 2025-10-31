package com.infinitum.labs.rickandmorty_android.character.state

import com.infinitum.labs.domain.character.model.Character

internal sealed interface CharacterDetailWrapper {
    data class UiState(
        val character: Character? = null,
        val isLoading: Boolean = false,
        val error: String? = null
    )

    sealed interface Event {
        data object OnBackClick : Event
        data object OnRetryClick : Event

        data object NavigateBack : Event
        data class ShowError(val message: String) : Event
    }
}