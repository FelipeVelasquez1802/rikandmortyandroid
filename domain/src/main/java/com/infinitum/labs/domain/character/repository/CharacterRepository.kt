package com.infinitum.labs.domain.character.repository

import com.infinitum.labs.domain.character.model.Character
import com.infinitum.labs.domain.common.model.DataResult

interface CharacterRepository {
    suspend fun getCharacters(page: Int = 1): Result<DataResult<List<Character>>>
    suspend fun getCharacter(id: Int): Result<DataResult<Character>>
    suspend fun getCharactersByName(name: String, page: Int = 1): Result<DataResult<List<Character>>>
}