package com.infinitum.labs.domain.episode.exception

import com.infinitum.labs.domain.common.exception.DomainException

sealed class EpisodeException(
    message: String,
    cause: Throwable? = null,
    errorCode: String? = null
) : DomainException(message, cause, errorCode) {

    data class EpisodeNotFound(
        val episodeId: Int
    ) : EpisodeException(
        message = "Episode with ID $episodeId does not exist",
        errorCode = "EPISODE_NOT_FOUND"
    )

    data class EpisodesNotFoundByName(
        val searchName: String
    ) : EpisodeException(
        message = "No episodes found matching name '$searchName'",
        errorCode = "EPISODES_NOT_FOUND_BY_NAME"
    )

    data class InvalidEpisodeId(
        val episodeId: Int
    ) : EpisodeException(
        message = "Invalid episode ID: $episodeId. ID must be greater than 0",
        errorCode = "INVALID_EPISODE_ID"
    )

    data class InvalidEpisodeName(
        val name: String
    ) : EpisodeException(
        message = "Invalid episode name: name cannot be blank",
        errorCode = "INVALID_EPISODE_NAME"
    )

    data class InvalidEpisodeAirDate(
        val airDate: String
    ) : EpisodeException(
        message = "Invalid episode air date: air date cannot be blank",
        errorCode = "INVALID_EPISODE_AIR_DATE"
    )

    data class InvalidEpisodeCode(
        val episodeCode: String
    ) : EpisodeException(
        message = "Invalid episode code: episode code cannot be blank",
        errorCode = "INVALID_EPISODE_CODE"
    )

    data class InvalidEpisodeUrl(
        val url: String
    ) : EpisodeException(
        message = "Invalid episode URL: '$url' cannot be blank or must be a valid URL",
        errorCode = "INVALID_EPISODE_URL"
    )

    data class InvalidEpisodeCreatedDate(
        val created: String
    ) : EpisodeException(
        message = "Invalid episode created date: '$created' must be a valid ISO-8601 date",
        errorCode = "INVALID_EPISODE_CREATED_DATE"
    )

    data class InvalidEpisodePage(
        val page: Int
    ) : EpisodeException(
        message = "Invalid page number: $page. Page must be greater than 0",
        errorCode = "INVALID_EPISODE_PAGE"
    )

    data class InvalidEpisodeSearchQuery(
        val query: String
    ) : EpisodeException(
        message = "Invalid search query: query cannot be blank",
        errorCode = "INVALID_EPISODE_SEARCH_QUERY"
    )

    data class EpisodeRepositoryUnavailable(
        override val message: String = "The episode catalog is temporarily unavailable",
        override val cause: Throwable? = null
    ) : EpisodeException(
        message = message,
        cause = cause,
        errorCode = "EPISODE_REPOSITORY_UNAVAILABLE"
    )

    data class InvalidEpisodeData(
        override val message: String = "Episode data is invalid or corrupted",
        override val cause: Throwable? = null
    ) : EpisodeException(
        message = message,
        cause = cause,
        errorCode = "INVALID_EPISODE_DATA"
    )
}