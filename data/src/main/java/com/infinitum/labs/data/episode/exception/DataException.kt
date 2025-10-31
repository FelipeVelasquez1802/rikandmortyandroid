package com.infinitum.labs.data.episode.exception

internal sealed class EpisodeDataException(
    message: String,
    cause: Throwable? = null
) : Exception(message, cause) {

    data class EpisodeApiNetworkError(
        override val message: String = "Cannot connect to episode API",
        override val cause: Throwable? = null
    ) : EpisodeDataException(message, cause)

    data class EpisodeApiServerError(
        val statusCode: Int,
        override val message: String = "Episode API server error (HTTP $statusCode)"
    ) : EpisodeDataException(message)

    data class EpisodeApiClientError(
        val statusCode: Int,
        override val message: String = "Episode API request failed (HTTP $statusCode)"
    ) : EpisodeDataException(message)

    data class EpisodeNotFoundInApi(
        val episodeId: Int
    ) : EpisodeDataException("Episode with ID $episodeId not found in API")

    data class EpisodesNotFoundInApiByName(
        val searchName: String
    ) : EpisodeDataException("No episodes found in API for name '$searchName'")

    data class EpisodeDataParseError(
        override val message: String = "Failed to parse episode data from API",
        override val cause: Throwable? = null
    ) : EpisodeDataException(message, cause)

    data class EpisodeApiUnexpectedError(
        override val message: String = "Unexpected error when accessing episode API",
        override val cause: Throwable? = null
    ) : EpisodeDataException(message, cause)
}