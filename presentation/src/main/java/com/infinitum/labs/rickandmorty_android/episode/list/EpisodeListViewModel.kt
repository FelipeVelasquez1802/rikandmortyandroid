package com.infinitum.labs.rickandmorty_android.episode.list

import androidx.lifecycle.viewModelScope
import com.infinitum.labs.domain.episode.exception.EpisodeException
import com.infinitum.labs.domain.episode.usecase.GetEpisodesUseCase
import com.infinitum.labs.rickandmorty_android.common.viewmodel.BaseViewModel
import com.infinitum.labs.rickandmorty_android.episode.state.EpisodeListWrapper
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class EpisodeListViewModel(
    private val getEpisodesUseCase: GetEpisodesUseCase
) : BaseViewModel<EpisodeListWrapper.UiState, EpisodeListWrapper.Event>(
    initialState = EpisodeListWrapper.UiState()
) {

    init {
        loadEpisodes()
    }

    internal fun onEvent(event: EpisodeListWrapper.Event) {
        when (event) {
            EpisodeListWrapper.Event.Retry -> handleRetry()
            EpisodeListWrapper.Event.LoadNextPage -> handleLoadNextPage()
            is EpisodeListWrapper.Event.OnEpisodeClick -> handleEpisodeClick(event.episodeId)

            is EpisodeListWrapper.Event.NavigateToDetail -> { }
            is EpisodeListWrapper.Event.ShowError -> { }
        }
    }

    private fun handleEpisodeClick(episodeId: Int) {
        viewModelScope.launch {
            channelEvent.send(EpisodeListWrapper.Event.NavigateToDetail(episodeId))
        }
    }

    private fun handleLoadNextPage() {
        if (!_state.value.canLoadMore || _state.value.isLoading) return

        _state.update { it.copy(currentPage = it.currentPage + 1) }
        loadEpisodes()
    }

    private fun handleRetry() {
        _state.update { it.copy(currentPage = 1, episodes = emptyList()) }
        loadEpisodes()
    }

    private fun loadEpisodes() {
        if (_state.value.isLoading) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            getEpisodesUseCase(_state.value.currentPage)
                .onSuccess { episodes ->
                    _state.update {
                        it.copy(
                            episodes = if (it.currentPage == 1) episodes else it.episodes + episodes,
                            isLoading = false,
                            error = null,
                            canLoadMore = episodes.isNotEmpty()
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

    private fun Throwable.toUserFriendlyMessage(): String {
        return when (this) {
            is EpisodeException.EpisodeNotFound ->
                "Episode #${episodeId} does not exist"

            is EpisodeException.EpisodesNotFoundByName ->
                "No episodes found matching '$searchName'"

            is EpisodeException.InvalidEpisodeId ->
                "Invalid episode ID: ${episodeId}"

            is EpisodeException.InvalidEpisodePage ->
                "Invalid page number: ${page}"

            is EpisodeException.InvalidEpisodeSearchQuery ->
                "Please enter a valid episode name to search"

            is EpisodeException.EpisodeRepositoryUnavailable ->
                "Unable to load episodes. Please check your connection and try again."

            is EpisodeException.InvalidEpisodeData ->
                "Episode data is corrupted. Please try again later."

            else -> message ?: "An unexpected error occurred. Please try again."
        }
    }

    override fun onStart() = Unit
}