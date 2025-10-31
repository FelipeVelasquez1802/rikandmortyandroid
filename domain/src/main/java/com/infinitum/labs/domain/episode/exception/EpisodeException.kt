package com.infinitum.labs.domain.episode.exception

import com.infinitum.labs.domain.common.exception.DomainException

/**
 * Sealed hierarchy of exceptions for Episode domain operations.
 * These exceptions are focused on the Episode model and business rules.
 * They speak the ubiquitous language of the Episode domain.
 *
 * All episode exceptions inherit from DomainException to maintain
 * consistency with the overall domain exception architecture.
 */
sealed class EpisodeException(
    message: String,
    cause: Throwable? = null,
    errorCode: String? = null
) : DomainException(message, cause, errorCode) {

    // ========== NOT FOUND EXCEPTIONS ==========

    /**
     * Thrown when an episode with the specified ID does not exist in the system.
     * Business rule: Episode IDs must reference existing episodes.
     */
    data class EpisodeNotFound(
        val episodeId: Int
    ) : EpisodeException(
        message = "Episode with ID $episodeId does not exist",
        errorCode = "EPISODE_NOT_FOUND"
    )

    /**
     * Thrown when an episode search by name yields no results.
     * Business rule: Episode searches may return empty results.
     */
    data class EpisodesNotFoundByName(
        val searchName: String
    ) : EpisodeException(
        message = "No episodes found matching name '$searchName'",
        errorCode = "EPISODES_NOT_FOUND_BY_NAME"
    )

    // ========== VALIDATION EXCEPTIONS (for model invariants) ==========

    /**
     * Thrown when an episode ID is invalid.
     * Business rule: Episode IDs must be positive integers (>= 1).
     */
    data class InvalidEpisodeId(
        val episodeId: Int
    ) : EpisodeException(
        message = "Invalid episode ID: $episodeId. ID must be greater than 0",
        errorCode = "INVALID_EPISODE_ID"
    )

    /**
     * Thrown when an episode name is invalid.
     * Business rule: Episode names cannot be blank or empty.
     */
    data class InvalidEpisodeName(
        val name: String
    ) : EpisodeException(
        message = "Invalid episode name: name cannot be blank",
        errorCode = "INVALID_EPISODE_NAME"
    )

    /**
     * Thrown when an episode air date is invalid.
     * Business rule: Episode air dates cannot be blank.
     */
    data class InvalidEpisodeAirDate(
        val airDate: String
    ) : EpisodeException(
        message = "Invalid episode air date: air date cannot be blank",
        errorCode = "INVALID_EPISODE_AIR_DATE"
    )

    /**
     * Thrown when an episode code is invalid.
     * Business rule: Episode codes cannot be blank and should follow format (e.g., "S01E01").
     */
    data class InvalidEpisodeCode(
        val episodeCode: String
    ) : EpisodeException(
        message = "Invalid episode code: episode code cannot be blank",
        errorCode = "INVALID_EPISODE_CODE"
    )

    /**
     * Thrown when an episode URL is invalid.
     * Business rule: Episode URLs cannot be blank and must be valid HTTP/HTTPS URLs.
     */
    data class InvalidEpisodeUrl(
        val url: String
    ) : EpisodeException(
        message = "Invalid episode URL: '$url' cannot be blank or must be a valid URL",
        errorCode = "INVALID_EPISODE_URL"
    )

    /**
     * Thrown when an episode's created date is invalid.
     * Business rule: Episode creation dates must be valid ISO-8601 format.
     */
    data class InvalidEpisodeCreatedDate(
        val created: String
    ) : EpisodeException(
        message = "Invalid episode created date: '$created' must be a valid ISO-8601 date",
        errorCode = "INVALID_EPISODE_CREATED_DATE"
    )

    // ========== USE CASE / QUERY VALIDATION EXCEPTIONS ==========

    /**
     * Thrown when attempting to retrieve episodes with invalid pagination.
     * Business rule: Page numbers must be positive integers (>= 1).
     */
    data class InvalidEpisodePage(
        val page: Int
    ) : EpisodeException(
        message = "Invalid page number: $page. Page must be greater than 0",
        errorCode = "INVALID_EPISODE_PAGE"
    )

    /**
     * Thrown when an episode search query is invalid.
     * Business rule: Search queries cannot be empty or blank.
     */
    data class InvalidEpisodeSearchQuery(
        val query: String
    ) : EpisodeException(
        message = "Invalid search query: query cannot be blank",
        errorCode = "INVALID_EPISODE_SEARCH_QUERY"
    )

    // ========== REPOSITORY / INFRASTRUCTURE EXCEPTIONS ==========

    /**
     * Thrown when the episode repository/catalog is temporarily unavailable.
     * Business rule: The episode catalog should be accessible, but may be temporarily unavailable.
     */
    data class EpisodeRepositoryUnavailable(
        override val message: String = "The episode catalog is temporarily unavailable",
        override val cause: Throwable? = null
    ) : EpisodeException(
        message = message,
        cause = cause,
        errorCode = "EPISODE_REPOSITORY_UNAVAILABLE"
    )

    /**
     * Thrown when episode data cannot be loaded due to corruption or invalid format.
     * Business rule: Episode data must be in a valid, readable format.
     * This is typically used at the data layer when parsing fails.
     */
    data class InvalidEpisodeData(
        override val message: String = "Episode data is invalid or corrupted",
        override val cause: Throwable? = null
    ) : EpisodeException(
        message = message,
        cause = cause,
        errorCode = "INVALID_EPISODE_DATA"
    )
}