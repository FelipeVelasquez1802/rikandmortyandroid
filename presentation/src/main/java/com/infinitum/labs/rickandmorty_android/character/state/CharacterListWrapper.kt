package com.infinitum.labs.rickandmorty_android.character.state

import com.infinitum.labs.domain.character.model.Character
import com.infinitum.labs.domain.common.model.DataSource

internal sealed interface CharacterListWrapper {
    data class UiState(
        val characters: List<Character> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null,
        val currentPage: Int = 1,
        val canLoadMore: Boolean = true,
        val dataSource: DataSource = DataSource.UNKNOWN
    )

    sealed interface Event {
        data object Retry : Event
        data object LoadNextPage : Event
        data class OnCharacterClick(val characterId: Int) : Event

        data class NavigateToDetail(val characterId: Int) : Event
        data class ShowError(val message: String) : Event
    }
}