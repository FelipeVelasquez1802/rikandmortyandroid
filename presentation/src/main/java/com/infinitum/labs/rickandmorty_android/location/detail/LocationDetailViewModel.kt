package com.infinitum.labs.rickandmorty_android.location.detail

import androidx.lifecycle.viewModelScope
import com.infinitum.labs.domain.location.exception.LocationException
import com.infinitum.labs.domain.location.usecase.GetLocationByIdUseCase
import com.infinitum.labs.rickandmorty_android.common.viewmodel.BaseViewModel
import com.infinitum.labs.rickandmorty_android.location.state.LocationDetailWrapper
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class LocationDetailViewModel(
    private val locationId: Int,
    private val getLocationByIdUseCase: GetLocationByIdUseCase
) : BaseViewModel<LocationDetailWrapper.UiState, LocationDetailWrapper.Event>(
    initialState = LocationDetailWrapper.UiState()
) {

    init {
        loadLocation()
    }

    internal fun onEvent(event: LocationDetailWrapper.Event) {
        when (event) {
            // User interaction events - handle in ViewModel
            LocationDetailWrapper.Event.Retry -> handleRetry()
            LocationDetailWrapper.Event.NavigateBack -> { /* Handled in UI */ }

            // One-time events - handled in UI layer
            is LocationDetailWrapper.Event.ShowError -> { /* Handled in UI */ }
        }
    }

    private fun handleRetry() {
        loadLocation()
    }

    private fun loadLocation() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            getLocationByIdUseCase(locationId)
                .onSuccess { location ->
                    _state.update {
                        it.copy(
                            location = location,
                            isLoading = false,
                            error = null
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
     */
    private fun Throwable.toUserFriendlyMessage(): String {
        return when (this) {
            is LocationException.LocationNotFound ->
                "Location #${locationId} does not exist"

            is LocationException.InvalidLocationId ->
                "Invalid location ID: $locationId"

            is LocationException.LocationRepositoryUnavailable ->
                "Unable to load location. Please check your connection and try again."

            is LocationException.InvalidLocationData ->
                "Location data is corrupted. Please try again later."

            else -> message ?: "An unexpected error occurred. Please try again."
        }
    }

    override fun onStart() = Unit
}