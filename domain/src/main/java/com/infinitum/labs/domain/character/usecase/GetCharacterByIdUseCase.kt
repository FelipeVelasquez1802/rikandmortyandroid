package com.infinitum.labs.domain.character.usecase

import com.infinitum.labs.domain.character.exception.CharacterException
import com.infinitum.labs.domain.character.model.Character
import com.infinitum.labs.domain.character.repository.CharacterRepository
import com.infinitum.labs.domain.common.model.DataResult

class GetCharacterByIdUseCase(
    private val characterRepository: CharacterRepository
) {
    suspend operator fun invoke(characterId: Int): Result<DataResult<Character>> {
        validateCharacterId(characterId)
        return characterRepository.getCharacter(characterId)
    }

    private fun validateCharacterId(characterId: Int) {
        if (characterId < 1) {
            throw CharacterException.InvalidCharacterId(characterId = characterId)
        }
    }
}