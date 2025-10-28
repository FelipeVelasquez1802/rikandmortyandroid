package com.infinitum.labs.domain.character.usecase

import com.infinitum.labs.domain.character.exception.CharacterException
import com.infinitum.labs.domain.character.model.Character
import com.infinitum.labs.domain.character.repository.CharacterRepository

/**
 * Use case for retrieving a specific character by their ID.
 *
 * Business logic:
 * - Validates character ID
 * - Retrieves the character from the repository
 * - Returns the character in domain model format
 *
 * This use case encapsulates the business rules for fetching a single character.
 */
class GetCharacterByIdUseCase(
    private val characterRepository: CharacterRepository
) {
    /**
     * Executes the use case to get a character by their ID.
     *
     * @param characterId The unique identifier of the character (must be >= 1)
     * @return Result containing the Character model or an exception
     *
     * Business rules applied:
     * - Character ID must be positive
     * - Throws CharacterNotFoundException if character doesn't exist
     */
    suspend operator fun invoke(characterId: Int): Result<Character> {
        // Validate business rules before calling repository
        validateCharacterId(characterId)

        // Delegate to repository for data access
        return characterRepository.getCharacter(characterId)
    }

    /**
     * Validates that a character ID is valid.
     * Business rule: Character IDs must be positive integers.
     */
    private fun validateCharacterId(characterId: Int) {
        if (characterId < 1) {
            throw CharacterException.InvalidCharacterId(characterId = characterId)
        }
    }
}