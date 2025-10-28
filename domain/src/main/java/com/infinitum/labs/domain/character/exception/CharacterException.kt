package com.infinitum.labs.domain.character.exception

/**
 * Sealed hierarchy of exceptions for Character domain operations.
 * These exceptions are focused on the Character model and business rules.
 * They speak the ubiquitous language of the Character domain.
 */
sealed class CharacterException(
    message: String,
    cause: Throwable? = null
) : Exception(message, cause) {

    /**
     * Thrown when a character with the specified ID does not exist in the system.
     * Business rule: Character IDs must reference existing characters.
     */
    data class CharacterNotFound(
        val characterId: Int
    ) : CharacterException("Character with ID $characterId does not exist")

    /**
     * Thrown when a character search by name yields no results.
     * Business rule: Character searches may return empty results.
     */
    data class CharactersNotFoundByName(
        val searchName: String
    ) : CharacterException("No characters found matching name '$searchName'")

    /**
     * Thrown when attempting to retrieve characters with invalid pagination.
     * Business rule: Page numbers must be positive integers.
     */
    data class InvalidCharacterPage(
        val page: Int
    ) : CharacterException("Invalid page number: $page. Page must be greater than 0")

    /**
     * Thrown when a character ID is invalid.
     * Business rule: Character IDs must be positive integers.
     */
    data class InvalidCharacterId(
        val characterId: Int
    ) : CharacterException("Invalid character ID: $characterId. ID must be greater than 0")

    /**
     * Thrown when a character search query is invalid.
     * Business rule: Search queries cannot be empty or blank.
     */
    data class InvalidCharacterSearchQuery(
        val query: String
    ) : CharacterException("Invalid search query: query cannot be blank")

    /**
     * Thrown when the character repository/catalog is temporarily unavailable.
     * Business rule: The character catalog should be accessible, but may be temporarily unavailable.
     */
    data class CharacterRepositoryUnavailable(
        override val message: String = "The character catalog is temporarily unavailable",
        override val cause: Throwable? = null
    ) : CharacterException(message, cause)

    /**
     * Thrown when character data cannot be loaded due to corruption or invalid format.
     * Business rule: Character data must be in a valid, readable format.
     */
    data class InvalidCharacterData(
        override val message: String = "Character data is invalid or corrupted",
        override val cause: Throwable? = null
    ) : CharacterException(message, cause)
}