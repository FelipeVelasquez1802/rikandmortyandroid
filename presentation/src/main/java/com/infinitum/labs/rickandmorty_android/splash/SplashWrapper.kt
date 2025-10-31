package com.infinitum.labs.rickandmorty_android.splash

internal sealed interface SplashWrapper {
    data class UiState(
        val isLoading: Boolean = true
    )

    sealed interface Event {
        // One-time events
        data object NavigateToMain : Event
    }
}