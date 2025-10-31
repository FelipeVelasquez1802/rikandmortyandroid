package com.infinitum.labs.rickandmorty_android.character.state

import com.infinitum.labs.domain.character.model.Character

internal sealed interface CharacterListWrapper {
    data class UiState(
        val characters: List<Character> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null,
        val currentPage: Int = 1,
        val canLoadMore: Boolean = true
    )

    sealed interface Event {
        // User interaction events
        data object Retry : Event
        data object LoadNextPage : Event
        data class OnCharacterClick(val characterId: Int) : Event

        // One-time events (handled in UI layer)
        data class NavigateToDetail(val characterId: Int) : Event
        data class ShowError(val message: String) : Event
    }
}