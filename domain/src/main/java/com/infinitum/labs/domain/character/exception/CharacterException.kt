package com.infinitum.labs.domain.character.exception

import com.infinitum.labs.domain.common.exception.DomainException
import com.infinitum.labs.domain.common.exception.NotFoundException
import com.infinitum.labs.domain.common.exception.RepositoryException
import com.infinitum.labs.domain.common.exception.ValidationException

sealed class CharacterException(
    message: String,
    cause: Throwable? = null,
    errorCode: String? = null
) : DomainException(message, cause, errorCode) {

    data class CharacterNotFound(
        val characterId: Int
    ) : CharacterException(
        message = "Character with ID $characterId does not exist",
        errorCode = "CHARACTER_NOT_FOUND"
    )

    data class CharactersNotFoundByName(
        val searchName: String
    ) : CharacterException(
        message = "No characters found matching name '$searchName'",
        errorCode = "CHARACTERS_NOT_FOUND_BY_NAME"
    )

    data class InvalidCharacterId(
        val characterId: Int
    ) : CharacterException(
        message = "Invalid character ID: $characterId. ID must be greater than 0",
        errorCode = "INVALID_CHARACTER_ID"
    )

    data class InvalidCharacterName(
        val name: String
    ) : CharacterException(
        message = "Invalid character name: name cannot be blank",
        errorCode = "INVALID_CHARACTER_NAME"
    )

    data class InvalidCharacterSpecies(
        val species: String
    ) : CharacterException(
        message = "Invalid character species: species cannot be blank",
        errorCode = "INVALID_CHARACTER_SPECIES"
    )

    data class InvalidCharacterImageUrl(
        val imageUrl: String
    ) : CharacterException(
        message = "Invalid character image URL: '$imageUrl' cannot be blank or must be a valid URL",
        errorCode = "INVALID_CHARACTER_IMAGE_URL"
    )

    data class InvalidCharacterUrl(
        val url: String
    ) : CharacterException(
        message = "Invalid character URL: '$url' cannot be blank or must be a valid URL",
        errorCode = "INVALID_CHARACTER_URL"
    )

    data class InvalidCharacterEpisodes(
        val episodeCount: Int = 0
    ) : CharacterException(
        message = "Invalid character episodes: character must have appeared in at least one episode (found $episodeCount)",
        errorCode = "INVALID_CHARACTER_EPISODES"
    )

    data class InvalidCharacterCreatedDate(
        val created: String
    ) : CharacterException(
        message = "Invalid character created date: '$created' must be a valid ISO-8601 date and not in the future",
        errorCode = "INVALID_CHARACTER_CREATED_DATE"
    )

    data class InvalidCharacterLocation(
        val locationType: String,
        val reason: String
    ) : CharacterException(
        message = "Invalid character $locationType: $reason",
        errorCode = "INVALID_CHARACTER_LOCATION"
    )

    data class InvalidCharacterPage(
        val page: Int
    ) : CharacterException(
        message = "Invalid page number: $page. Page must be greater than 0",
        errorCode = "INVALID_CHARACTER_PAGE"
    )

    data class InvalidCharacterSearchQuery(
        val query: String
    ) : CharacterException(
        message = "Invalid search query: query cannot be blank",
        errorCode = "INVALID_CHARACTER_SEARCH_QUERY"
    )

    data class CharacterRepositoryUnavailable(
        override val message: String = "The character catalog is temporarily unavailable",
        override val cause: Throwable? = null
    ) : CharacterException(
        message = message,
        cause = cause,
        errorCode = "CHARACTER_REPOSITORY_UNAVAILABLE"
    )

    data class InvalidCharacterData(
        override val message: String = "Character data is invalid or corrupted",
        override val cause: Throwable? = null
    ) : CharacterException(
        message = message,
        cause = cause,
        errorCode = "INVALID_CHARACTER_DATA"
    )
}