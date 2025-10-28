package com.infinitum.labs.data.character.exception

/**
 * Sealed hierarchy of exceptions for Character data layer operations.
 * These represent infrastructure/technical failures when accessing character data.
 * Unlike domain exceptions, these are focused on HOW we access characters (API, DB, etc.)
 */
sealed class CharacterDataException(
    message: String,
    cause: Throwable? = null
) : Exception(message, cause) {

    /**
     * Thrown when the character API cannot be reached due to network issues.
     */
    data class CharacterApiNetworkError(
        override val message: String = "Cannot connect to character API",
        override val cause: Throwable? = null
    ) : CharacterDataException(message, cause)

    /**
     * Thrown when the character API returns a server error (5xx).
     */
    data class CharacterApiServerError(
        val statusCode: Int,
        override val message: String = "Character API server error (HTTP $statusCode)"
    ) : CharacterDataException(message)

    /**
     * Thrown when a character API request is malformed or unauthorized (4xx, excluding 404).
     */
    data class CharacterApiClientError(
        val statusCode: Int,
        override val message: String = "Character API request failed (HTTP $statusCode)"
    ) : CharacterDataException(message)

    /**
     * Thrown when a character is not found in the remote API (HTTP 404).
     */
    data class CharacterNotFoundInApi(
        val characterId: Int
    ) : CharacterDataException("Character with ID $characterId not found in API")

    /**
     * Thrown when no characters match the search query in the API.
     */
    data class CharactersNotFoundInApiByName(
        val searchName: String
    ) : CharacterDataException("No characters found in API for name '$searchName'")

    /**
     * Thrown when character data from the API cannot be parsed.
     */
    data class CharacterDataParseError(
        override val message: String = "Failed to parse character data from API",
        override val cause: Throwable? = null
    ) : CharacterDataException(message, cause)

    /**
     * Thrown when the character API returns an unexpected response.
     */
    data class CharacterApiUnexpectedError(
        override val message: String = "Unexpected error when accessing character API",
        override val cause: Throwable? = null
    ) : CharacterDataException(message, cause)
}