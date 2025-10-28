package com.infinitum.labs.domain.character.exception

import com.infinitum.labs.domain.common.exception.DomainException
import com.infinitum.labs.domain.common.exception.NotFoundException
import com.infinitum.labs.domain.common.exception.RepositoryException
import com.infinitum.labs.domain.common.exception.ValidationException

/**
 * Sealed hierarchy of exceptions for Character domain operations.
 * These exceptions are focused on the Character model and business rules.
 * They speak the ubiquitous language of the Character domain.
 *
 * All character exceptions inherit from DomainException to maintain
 * consistency with the overall domain exception architecture.
 */
sealed class CharacterException(
    message: String,
    cause: Throwable? = null,
    errorCode: String? = null
) : DomainException(message, cause, errorCode) {

    // ========== NOT FOUND EXCEPTIONS ==========

    /**
     * Thrown when a character with the specified ID does not exist in the system.
     * Business rule: Character IDs must reference existing characters.
     */
    data class CharacterNotFound(
        val characterId: Int
    ) : CharacterException(
        message = "Character with ID $characterId does not exist",
        errorCode = "CHARACTER_NOT_FOUND"
    )

    /**
     * Thrown when a character search by name yields no results.
     * Business rule: Character searches may return empty results.
     */
    data class CharactersNotFoundByName(
        val searchName: String
    ) : CharacterException(
        message = "No characters found matching name '$searchName'",
        errorCode = "CHARACTERS_NOT_FOUND_BY_NAME"
    )

    // ========== VALIDATION EXCEPTIONS (for model invariants) ==========

    /**
     * Thrown when a character ID is invalid.
     * Business rule: Character IDs must be positive integers (>= 1).
     */
    data class InvalidCharacterId(
        val characterId: Int
    ) : CharacterException(
        message = "Invalid character ID: $characterId. ID must be greater than 0",
        errorCode = "INVALID_CHARACTER_ID"
    )

    /**
     * Thrown when a character name is invalid.
     * Business rule: Character names cannot be blank or empty.
     */
    data class InvalidCharacterName(
        val name: String
    ) : CharacterException(
        message = "Invalid character name: name cannot be blank",
        errorCode = "INVALID_CHARACTER_NAME"
    )

    /**
     * Thrown when a character species is invalid.
     * Business rule: Character species cannot be blank.
     */
    data class InvalidCharacterSpecies(
        val species: String
    ) : CharacterException(
        message = "Invalid character species: species cannot be blank",
        errorCode = "INVALID_CHARACTER_SPECIES"
    )

    /**
     * Thrown when a character image URL is invalid.
     * Business rule: Character image URLs cannot be blank and must be valid URLs.
     */
    data class InvalidCharacterImageUrl(
        val imageUrl: String
    ) : CharacterException(
        message = "Invalid character image URL: '$imageUrl' cannot be blank or must be a valid URL",
        errorCode = "INVALID_CHARACTER_IMAGE_URL"
    )

    /**
     * Thrown when a character URL is invalid.
     * Business rule: Character URLs cannot be blank and must be valid URLs.
     */
    data class InvalidCharacterUrl(
        val url: String
    ) : CharacterException(
        message = "Invalid character URL: '$url' cannot be blank or must be a valid URL",
        errorCode = "INVALID_CHARACTER_URL"
    )

    /**
     * Thrown when a character has no episodes.
     * Business rule: Characters must have appeared in at least one episode.
     */
    data class InvalidCharacterEpisodes(
        val episodeCount: Int = 0
    ) : CharacterException(
        message = "Invalid character episodes: character must have appeared in at least one episode (found $episodeCount)",
        errorCode = "INVALID_CHARACTER_EPISODES"
    )

    /**
     * Thrown when a character's created date is invalid.
     * Business rule: Character creation dates must be valid ISO-8601 format and not in the future.
     */
    data class InvalidCharacterCreatedDate(
        val created: String
    ) : CharacterException(
        message = "Invalid character created date: '$created' must be a valid ISO-8601 date and not in the future",
        errorCode = "INVALID_CHARACTER_CREATED_DATE"
    )

    /**
     * Thrown when a character's location data is invalid.
     * Business rule: Location names and URLs must be valid.
     */
    data class InvalidCharacterLocation(
        val locationType: String, // "origin" or "location"
        val reason: String
    ) : CharacterException(
        message = "Invalid character $locationType: $reason",
        errorCode = "INVALID_CHARACTER_LOCATION"
    )

    // ========== USE CASE / QUERY VALIDATION EXCEPTIONS ==========

    /**
     * Thrown when attempting to retrieve characters with invalid pagination.
     * Business rule: Page numbers must be positive integers (>= 1).
     */
    data class InvalidCharacterPage(
        val page: Int
    ) : CharacterException(
        message = "Invalid page number: $page. Page must be greater than 0",
        errorCode = "INVALID_CHARACTER_PAGE"
    )

    /**
     * Thrown when a character search query is invalid.
     * Business rule: Search queries cannot be empty or blank.
     */
    data class InvalidCharacterSearchQuery(
        val query: String
    ) : CharacterException(
        message = "Invalid search query: query cannot be blank",
        errorCode = "INVALID_CHARACTER_SEARCH_QUERY"
    )

    // ========== REPOSITORY / INFRASTRUCTURE EXCEPTIONS ==========

    /**
     * Thrown when the character repository/catalog is temporarily unavailable.
     * Business rule: The character catalog should be accessible, but may be temporarily unavailable.
     */
    data class CharacterRepositoryUnavailable(
        override val message: String = "The character catalog is temporarily unavailable",
        override val cause: Throwable? = null
    ) : CharacterException(
        message = message,
        cause = cause,
        errorCode = "CHARACTER_REPOSITORY_UNAVAILABLE"
    )

    /**
     * Thrown when character data cannot be loaded due to corruption or invalid format.
     * Business rule: Character data must be in a valid, readable format.
     * This is typically used at the data layer when parsing fails.
     */
    data class InvalidCharacterData(
        override val message: String = "Character data is invalid or corrupted",
        override val cause: Throwable? = null
    ) : CharacterException(
        message = message,
        cause = cause,
        errorCode = "INVALID_CHARACTER_DATA"
    )
}