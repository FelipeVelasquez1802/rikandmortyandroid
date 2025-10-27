package com.infinitum.labs.domain.character.repository

import com.infinitum.labs.domain.character.model.Character

interface CharacterRepository {
    suspend fun getCharacters(page: Int = 1): Result<List<Character>>
    suspend fun getCharacter(id: Int): Result<Character>
    suspend fun getCharactersByName(name: String, page: Int = 1): Result<List<Character>>
}