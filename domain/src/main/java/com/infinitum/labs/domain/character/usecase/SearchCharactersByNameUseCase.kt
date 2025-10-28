package com.infinitum.labs.domain.character.usecase

import com.infinitum.labs.domain.character.exception.CharacterException
import com.infinitum.labs.domain.character.model.Character
import com.infinitum.labs.domain.character.repository.CharacterRepository

/**
 * Use case for searching characters by name with pagination.
 *
 * Business logic:
 * - Validates search query and pagination parameters
 * - Searches characters in the repository
 * - Returns matching characters in domain model format
 *
 * This use case encapsulates the business rules for character search operations.
 */
class SearchCharactersByNameUseCase(
    private val characterRepository: CharacterRepository
) {
    /**
     * Executes the use case to search for characters by name.
     *
     * @param name The search query (character name or partial name)
     * @param page The page number for pagination (must be >= 1)
     * @return Result containing a list of matching Character models or an exception
     *
     * Business rules applied:
     * - Search query cannot be blank
     * - Page number must be positive
     * - Returns empty list if no characters match the search
     * - Search is case-insensitive (handled by API)
     */
    suspend operator fun invoke(
        name: String,
        page: Int = 1
    ): Result<List<Character>> {
        // Validate business rules before calling repository
        validateSearchQuery(name)
        validatePage(page)

        // Delegate to repository for data access
        return characterRepository.getCharactersByName(name, page)
    }

    /**
     * Validates that a search query is valid.
     * Business rule: Search queries cannot be blank.
     */
    private fun validateSearchQuery(query: String) {
        if (query.isBlank()) {
            throw CharacterException.InvalidCharacterSearchQuery(query = query)
        }
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