package com.infinitum.labs.rickandmorty_android.character.list

import com.infinitum.labs.domain.character.model.Character

data class CharacterListState(
    val characters: List<Character> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val currentPage: Int = 1,
    val canLoadMore: Boolean = true
)