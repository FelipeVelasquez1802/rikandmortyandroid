package com.infinitum.labs.domain.character.usecase

import com.infinitum.labs.domain.character.exception.CharacterException
import com.infinitum.labs.domain.character.model.Character
import com.infinitum.labs.domain.character.repository.CharacterRepository
import com.infinitum.labs.domain.common.model.DataResult

class GetCharactersUseCase(
    private val characterRepository: CharacterRepository
) {
    suspend operator fun invoke(page: Int = 1): Result<DataResult<List<Character>>> {
        validatePage(page)
        return characterRepository.getCharacters(page)
    }

    private fun validatePage(page: Int) {
        if (page < 1) {
            throw CharacterException.InvalidCharacterPage(page = page)
        }
    }
}