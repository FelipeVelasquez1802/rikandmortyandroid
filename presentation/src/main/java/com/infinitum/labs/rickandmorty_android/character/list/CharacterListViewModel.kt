package com.infinitum.labs.rickandmorty_android.character.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinitum.labs.domain.character.exception.CharacterException
import com.infinitum.labs.domain.character.repository.CharacterRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CharacterListViewModel(
    private val characterRepository: CharacterRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CharacterListState())
    val state: StateFlow<CharacterListState> = _state.asStateFlow()

    init {
        loadCharacters()
    }

    fun loadCharacters() {
        if (_state.value.isLoading) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            characterRepository.getCharacters(_state.value.currentPage)
                .onSuccess { characters ->
                    _state.update {
                        it.copy(
                            characters = if (it.currentPage == 1) characters else it.characters + characters,
                            isLoading = false,
                            error = null,
                            canLoadMore = characters.isNotEmpty()
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

    fun loadNextPage() {
        if (!_state.value.canLoadMore || _state.value.isLoading) return

        _state.update { it.copy(currentPage = it.currentPage + 1) }
        loadCharacters()
    }

    fun retry() {
        _state.update { it.copy(currentPage = 1, characters = emptyList()) }
        loadCharacters()
    }

    /**
     * Converts Character domain exceptions to user-friendly error messages.
     * Messages are focused on the Character model and user actions.
     */
    private fun Throwable.toUserFriendlyMessage(): String {
        return when (this) {
            // Character not found scenarios
            is CharacterException.CharacterNotFound ->
                "Character #${characterId} does not exist"

            is CharacterException.CharactersNotFoundByName ->
                "No characters found matching '$searchName'"

            // Character validation errors
            is CharacterException.InvalidCharacterId ->
                "Invalid character ID: ${characterId}"

            is CharacterException.InvalidCharacterPage ->
                "Invalid page number: ${page}"

            is CharacterException.InvalidCharacterSearchQuery ->
                "Please enter a valid character name to search"

            // Character repository/catalog errors
            is CharacterException.CharacterRepositoryUnavailable ->
                "Unable to load characters. Please check your connection and try again."

            is CharacterException.InvalidCharacterData ->
                "Character data is corrupted. Please try again later."

            // Fallback for unexpected errors
            else -> message ?: "An unexpected error occurred. Please try again."
        }
    }
}