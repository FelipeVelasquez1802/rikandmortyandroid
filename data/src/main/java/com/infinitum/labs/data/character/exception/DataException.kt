package com.infinitum.labs.data.character.exception

internal sealed class CharacterDataException(
    message: String,
    cause: Throwable? = null
) : Exception(message, cause) {

    data class CharacterApiNetworkError(
        override val message: String = "Cannot connect to character API",
        override val cause: Throwable? = null
    ) : CharacterDataException(message, cause)

    data class CharacterApiServerError(
        val statusCode: Int,
        override val message: String = "Character API server error (HTTP $statusCode)"
    ) : CharacterDataException(message)

    data class CharacterApiClientError(
        val statusCode: Int,
        override val message: String = "Character API request failed (HTTP $statusCode)"
    ) : CharacterDataException(message)

    data class CharacterNotFoundInApi(
        val characterId: Int
    ) : CharacterDataException("Character with ID $characterId not found in API")

    data class CharactersNotFoundInApiByName(
        val searchName: String
    ) : CharacterDataException("No characters found in API for name '$searchName'")

    data class CharacterDataParseError(
        override val message: String = "Failed to parse character data from API",
        override val cause: Throwable? = null
    ) : CharacterDataException(message, cause)

    data class CharacterApiUnexpectedError(
        override val message: String = "Unexpected error when accessing character API",
        override val cause: Throwable? = null
    ) : CharacterDataException(message, cause)
}