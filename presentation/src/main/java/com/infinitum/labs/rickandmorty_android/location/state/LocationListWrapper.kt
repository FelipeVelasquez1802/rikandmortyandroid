package com.infinitum.labs.rickandmorty_android.location.state

import com.infinitum.labs.domain.location.model.Location

internal sealed interface LocationListWrapper {
    data class UiState(
        val locations: List<Location> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null,
        val currentPage: Int = 1,
        val canLoadMore: Boolean = true
    )

    sealed interface Event {
        // User interaction events
        data object Retry : Event
        data object LoadNextPage : Event
        data class OnLocationClick(val locationId: Int) : Event

        // One-time events (handled in UI layer)
        data class NavigateToDetail(val locationId: Int) : Event
        data class ShowError(val message: String) : Event
    }
}