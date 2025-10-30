package com.infinitum.labs.domain.episode.repository

import com.infinitum.labs.domain.episode.model.Episode

/**
 * Repository interface for Episode domain operations.
 * Defines the contract for accessing episode data.
 *
 * All methods return Result<T> to handle success and failure cases explicitly.
 * This follows the Railway Oriented Programming pattern for error handling.
 */
interface EpisodeRepository {
    /**
     * Retrieves a paginated list of episodes.
     *
     * @param page The page number to retrieve (1-indexed). Defaults to 1.
     * @return Result containing a list of episodes, or a failure with EpisodeException.
     */
    suspend fun getEpisodes(page: Int = 1): Result<List<Episode>>

    /**
     * Retrieves a single episode by its ID.
     *
     * @param id The unique identifier of the episode.
     * @return Result containing the episode, or a failure with EpisodeException.
     */
    suspend fun getEpisode(id: Int): Result<Episode>

    /**
     * Searches for episodes by name with pagination support.
     *
     * @param name The name to search for (partial matching).
     * @param page The page number to retrieve (1-indexed). Defaults to 1.
     * @return Result containing a list of matching episodes, or a failure with EpisodeException.
     */
    suspend fun getEpisodesByName(name: String, page: Int = 1): Result<List<Episode>>
}