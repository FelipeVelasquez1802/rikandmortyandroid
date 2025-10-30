package com.infinitum.labs.rickandmorty_android.character.detail

import androidx.lifecycle.viewModelScope
import com.infinitum.labs.domain.character.exception.CharacterException
import com.infinitum.labs.domain.character.usecase.GetCharacterByIdUseCase
import com.infinitum.labs.rickandmorty_android.character.state.CharacterDetailWrapper
import com.infinitum.labs.rickandmorty_android.common.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class CharacterDetailViewModel(
    private val characterId: Int,
    private val getCharacterByIdUseCase: GetCharacterByIdUseCase
) : BaseViewModel<CharacterDetailWrapper.UiState, CharacterDetailWrapper.Event>(
    initialState = CharacterDetailWrapper.UiState()
) {

    init {
        loadCharacter()
    }

    internal fun onEvent(event: CharacterDetailWrapper.Event) {
        when (event) {
            // User interaction events - handle in ViewModel
            CharacterDetailWrapper.Event.OnBackClick -> handleBackClick()
            CharacterDetailWrapper.Event.OnRetryClick -> handleRetryClick()

            // One-time events - handled in UI layer
            CharacterDetailWrapper.Event.NavigateBack -> { /* Handled in UI */ }
            is CharacterDetailWrapper.Event.ShowError -> { /* Handled in UI */ }
        }
    }

    private fun handleBackClick() {
        viewModelScope.launch {
            channelEvent.send(CharacterDetailWrapper.Event.NavigateBack)
        }
    }

    private fun handleRetryClick() {
        loadCharacter()
    }

    private fun loadCharacter() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            getCharacterByIdUseCase(characterId)
                .onSuccess { character ->
                    _state.update {
                        it.copy(
                            character = character,
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
     * Converts Character domain exceptions to user-friendly error messages.
     */
    private fun Throwable.toUserFriendlyMessage(): String {
        return when (this) {
            is CharacterException.CharacterNotFound ->
                "Character #${characterId} does not exist"

            is CharacterException.InvalidCharacterId ->
                "Invalid character ID: $characterId"

            is CharacterException.CharacterRepositoryUnavailable ->
                "Unable to load character. Please check your connection and try again."

            is CharacterException.InvalidCharacterData ->
                "Character data is corrupted. Please try again later."

            else -> message ?: "An unexpected error occurred. Please try again."
        }
    }

    override fun onStart() = Unit
}