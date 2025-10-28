package com.infinitum.labs.domain.character.usecase

import com.infinitum.labs.domain.character.exception.CharacterException
import com.infinitum.labs.domain.character.model.Character
import com.infinitum.labs.domain.character.repository.CharacterRepository

/**
 * Use case for retrieving a paginated list of characters.
 *
 * Business logic:
 * - Validates pagination parameters
 * - Retrieves characters from the repository
 * - Returns characters in domain model format
 *
 * This use case encapsulates the business rules for fetching character lists.
 */
class GetCharactersUseCase(
    private val characterRepository: CharacterRepository
) {
    /**
     * Executes the use case to get a page of characters.
     *
     * @param page The page number to retrieve (must be >= 1)
     * @return Result containing a list of Character models or an exception
     *
     * Business rules applied:
     * - Page number must be positive
     * - Returns empty list if no characters found on that page
     */
    suspend operator fun invoke(page: Int = 1): Result<List<Character>> {
        // Validate business rules before calling repository
        validatePage(page)

        // Delegate to repository for data access
        return characterRepository.getCharacters(page)
    }

    /**
     * Validates that a page number is valid for pagination.
     * Business rule: Page numbers must be positive integers starting at 1.
     */
    private fun validatePage(page: Int) {
        if (page < 1) {
            throw CharacterException.InvalidCharacterPage(page = page)
        }
    }
}