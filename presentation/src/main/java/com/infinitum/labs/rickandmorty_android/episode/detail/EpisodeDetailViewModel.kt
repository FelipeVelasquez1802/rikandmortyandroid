package com.infinitum.labs.rickandmorty_android.episode.detail

import androidx.lifecycle.viewModelScope
import com.infinitum.labs.domain.episode.exception.EpisodeException
import com.infinitum.labs.domain.episode.usecase.GetEpisodeByIdUseCase
import com.infinitum.labs.rickandmorty_android.common.viewmodel.BaseViewModel
import com.infinitum.labs.rickandmorty_android.episode.state.EpisodeDetailWrapper
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class EpisodeDetailViewModel(
    private val episodeId: Int,
    private val getEpisodeByIdUseCase: GetEpisodeByIdUseCase
) : BaseViewModel<EpisodeDetailWrapper.UiState, EpisodeDetailWrapper.Event>(
    initialState = EpisodeDetailWrapper.UiState()
) {

    init {
        loadEpisode()
    }

    internal fun onEvent(event: EpisodeDetailWrapper.Event) {
        when (event) {
            // User interaction events - handle in ViewModel
            EpisodeDetailWrapper.Event.Retry -> handleRetry()
            EpisodeDetailWrapper.Event.NavigateBack -> { /* Handled in UI */ }

            // One-time events - handled in UI layer
            is EpisodeDetailWrapper.Event.ShowError -> { /* Handled in UI */ }
        }
    }

    private fun handleRetry() {
        loadEpisode()
    }

    private fun loadEpisode() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            getEpisodeByIdUseCase(episodeId)
                .onSuccess { episode ->
                    _state.update {
                        it.copy(
                            episode = episode,
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
     * Converts Episode domain exceptions to user-friendly error messages.
     */
    private fun Throwable.toUserFriendlyMessage(): String {
        return when (this) {
            is EpisodeException.EpisodeNotFound ->
                "Episode #${episodeId} does not exist"

            is EpisodeException.InvalidEpisodeId ->
                "Invalid episode ID: $episodeId"

            is EpisodeException.EpisodeRepositoryUnavailable ->
                "Unable to load episode. Please check your connection and try again."

            is EpisodeException.InvalidEpisodeData ->
                "Episode data is corrupted. Please try again later."

            else -> message ?: "An unexpected error occurred. Please try again."
        }
    }

    override fun onStart() = Unit
}