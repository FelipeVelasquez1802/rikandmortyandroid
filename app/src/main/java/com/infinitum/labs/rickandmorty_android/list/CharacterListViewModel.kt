package com.infinitum.labs.rickandmorty_android.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
                            error = exception.message ?: "Unknown error occurred"
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
}