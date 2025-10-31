package com.infinitum.labs.domain.character.usecase

import com.infinitum.labs.domain.character.exception.CharacterException
import com.infinitum.labs.domain.character.model.Character
import com.infinitum.labs.domain.character.repository.CharacterRepository
import com.infinitum.labs.domain.common.model.DataResult

class SearchCharactersByNameUseCase(
    private val characterRepository: CharacterRepository
) {
    suspend operator fun invoke(
        name: String,
        page: Int = 1
    ): Result<DataResult<List<Character>>> {
        validateSearchQuery(name)
        validatePage(page)
        return characterRepository.getCharactersByName(name, page)
    }

    private fun validateSearchQuery(query: String) {
        if (query.isBlank()) {
            throw CharacterException.InvalidCharacterSearchQuery(query = query)
        }
    }

    private fun validatePage(page: Int) {
        if (page < 1) {
            throw CharacterException.InvalidCharacterPage(page = page)
        }
    }
}