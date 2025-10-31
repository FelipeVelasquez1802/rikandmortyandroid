package com.infinitum.labs.rickandmorty_android.character.list

import androidx.lifecycle.viewModelScope
import com.infinitum.labs.domain.character.exception.CharacterException
import com.infinitum.labs.domain.character.usecase.GetCharactersUseCase
import com.infinitum.labs.rickandmorty_android.character.state.CharacterListWrapper
import com.infinitum.labs.rickandmorty_android.common.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class CharacterListViewModel(
    private val getCharactersUseCase: GetCharactersUseCase
) : BaseViewModel<CharacterListWrapper.UiState, CharacterListWrapper.Event>(
    initialState = CharacterListWrapper.UiState()
) {

    init {
        loadCharacters()
    }

    internal fun onEvent(event: CharacterListWrapper.Event) {
        when (event) {
            CharacterListWrapper.Event.Retry -> handleRetry()
            CharacterListWrapper.Event.LoadNextPage -> handleLoadNextPage()
            is CharacterListWrapper.Event.OnCharacterClick -> handleCharacterClick(event.characterId)

            is CharacterListWrapper.Event.NavigateToDetail -> { }
            is CharacterListWrapper.Event.ShowError -> { }
        }
    }

    private fun handleCharacterClick(characterId: Int) {
        viewModelScope.launch {
            channelEvent.send(CharacterListWrapper.Event.NavigateToDetail(characterId))
        }
    }

    private fun handleLoadNextPage() {
        if (!_state.value.canLoadMore || _state.value.isLoading) return

        _state.update { it.copy(currentPage = it.currentPage + 1) }
        loadCharacters()
    }

    private fun handleRetry() {
        _state.update { it.copy(currentPage = 1, characters = emptyList()) }
        loadCharacters()
    }

    @Deprecated("Use onEvent(CharacterListWrapper.Event.LoadNextPage) instead", ReplaceWith("onEvent(CharacterListWrapper.Event.LoadNextPage)"))
    fun loadNextPage() = handleLoadNextPage()

    @Deprecated("Use onEvent(CharacterListWrapper.Event.Retry) instead", ReplaceWith("onEvent(CharacterListWrapper.Event.Retry)"))
    fun retry() = handleRetry()

    private fun loadCharacters() {
        if (_state.value.isLoading) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            getCharactersUseCase(_state.value.currentPage)
                .onSuccess { dataResult ->
                    _state.update {
                        it.copy(
                            characters = if (it.currentPage == 1) dataResult.data else it.characters + dataResult.data,
                            isLoading = false,
                            error = null,
                            canLoadMore = dataResult.data.isNotEmpty(),
                            dataSource = dataResult.source
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
            is CharacterException.CharacterNotFound ->
                "Character #${characterId} does not exist"

            is CharacterException.CharactersNotFoundByName ->
                "No characters found matching '$searchName'"

            is CharacterException.InvalidCharacterId ->
                "Invalid character ID: ${characterId}"

            is CharacterException.InvalidCharacterPage ->
                "Invalid page number: ${page}"

            is CharacterException.InvalidCharacterSearchQuery ->
                "Please enter a valid character name to search"

            is CharacterException.CharacterRepositoryUnavailable ->
                "Unable to load characters. Please check your connection and try again."

            is CharacterException.InvalidCharacterData ->
                "Character data is corrupted. Please try again later."

            else -> message ?: "An unexpected error occurred. Please try again."
        }
    }

    override fun onStart() = Unit
}