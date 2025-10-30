package com.infinitum.labs.rickandmorty_android.location.list

import androidx.lifecycle.viewModelScope
import com.infinitum.labs.domain.location.exception.LocationException
import com.infinitum.labs.domain.location.usecase.GetLocationsUseCase
import com.infinitum.labs.rickandmorty_android.common.viewmodel.BaseViewModel
import com.infinitum.labs.rickandmorty_android.location.state.LocationListWrapper
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class LocationListViewModel(
    private val getLocationsUseCase: GetLocationsUseCase
) : BaseViewModel<LocationListWrapper.UiState, LocationListWrapper.Event>(
    initialState = LocationListWrapper.UiState()
) {

    init {
        loadLocations()
    }

    internal fun onEvent(event: LocationListWrapper.Event) {
        when (event) {
            // User interaction events - handle in ViewModel
            LocationListWrapper.Event.Retry -> handleRetry()
            LocationListWrapper.Event.LoadNextPage -> handleLoadNextPage()
            is LocationListWrapper.Event.OnLocationClick -> handleLocationClick(event.locationId)

            // One-time events - handled in UI layer
            is LocationListWrapper.Event.NavigateToDetail -> { /* Handled in UI */ }
            is LocationListWrapper.Event.ShowError -> { /* Handled in UI */ }
        }
    }

    private fun handleLocationClick(locationId: Int) {
        viewModelScope.launch {
            channelEvent.send(LocationListWrapper.Event.NavigateToDetail(locationId))
        }
    }

    private fun handleLoadNextPage() {
        if (!_state.value.canLoadMore || _state.value.isLoading) return

        _state.update { it.copy(currentPage = it.currentPage + 1) }
        loadLocations()
    }

    private fun handleRetry() {
        _state.update { it.copy(currentPage = 1, locations = emptyList()) }
        loadLocations()
    }

    private fun loadLocations() {
        if (_state.value.isLoading) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            getLocationsUseCase(_state.value.currentPage)
                .onSuccess { locations ->
                    _state.update {
                        it.copy(
                            locations = if (it.currentPage == 1) locations else it.locations + locations,
                            isLoading = false,
                            error = null,
                            canLoadMore = locations.isNotEmpty()
                        )
                    }
                }
                .onFailure { exception ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = exception.toUserFriendlyMessage()
                        )
                    }
                }
        }
    }

    /**
     * Converts Location domain exceptions to user-friendly error messages.
     * Messages are focused on the Location model and user actions.
     */
    private fun Throwable.toUserFriendlyMessage(): String {
        return when (this) {
            // Location not found scenarios
            is LocationException.LocationNotFound ->
                "Location #${locationId} does not exist"

            is LocationException.LocationsNotFoundByName ->
                "No locations found matching '$searchName'"

            // Location validation errors
            is LocationException.InvalidLocationId ->
                "Invalid location ID: ${locationId}"

            is LocationException.InvalidLocationPage ->
                "Invalid page number: ${page}"

            is LocationException.InvalidLocationSearchQuery ->
                "Please enter a valid location name to search"

            // Location repository/catalog errors
            is LocationException.LocationRepositoryUnavailable ->
                "Unable to load locations. Please check your connection and try again."

            is LocationException.InvalidLocationData ->
                "Location data is corrupted. Please try again later."

            // Fallback for unexpected errors
            else -> message ?: "An unexpected error occurred. Please try again."
        }
    }

    override fun onStart() = Unit
}