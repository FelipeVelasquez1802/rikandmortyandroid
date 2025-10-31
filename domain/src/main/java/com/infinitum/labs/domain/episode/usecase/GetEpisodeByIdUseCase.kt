package com.infinitum.labs.domain.episode.usecase

import com.infinitum.labs.domain.episode.exception.EpisodeException
import com.infinitum.labs.domain.episode.model.Episode
import com.infinitum.labs.domain.episode.repository.EpisodeRepository

/**
 * Use case for retrieving a specific episode by its ID.
 *
 * Business logic:
 * - Validates episode ID
 * - Retrieves the episode from the repository
 * - Returns the episode in domain model format
 *
 * This use case encapsulates the business rules for fetching a single episode.
 */
class GetEpisodeByIdUseCase(
    private val episodeRepository: EpisodeRepository
) {
    /**
     * Executes the use case to get an episode by its ID.
     *
     * @param episodeId The unique identifier of the episode (must be >= 1)
     * @return Result containing the Episode model or an exception
     *
     * Business rules applied:
     * - Episode ID must be positive
     * - Throws EpisodeNotFoundException if episode doesn't exist
     */
    suspend operator fun invoke(episodeId: Int): Result<Episode> {
        // Validate business rules before calling repository
        validateEpisodeId(episodeId)

        // Delegate to repository for data access
        return episodeRepository.getEpisode(episodeId)
    }

    /**
     * Validates that an episode ID is valid.
     * Business rule: Episode IDs must be positive integers.
     */
    private fun validateEpisodeId(episodeId: Int) {
        if (episodeId < 1) {
            throw EpisodeException.InvalidEpisodeId(episodeId = episodeId)
        }
    }
}