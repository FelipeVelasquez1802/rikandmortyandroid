package com.infinitum.labs.domain.episode.usecase

import com.infinitum.labs.domain.episode.exception.EpisodeException
import com.infinitum.labs.domain.episode.model.Episode
import com.infinitum.labs.domain.episode.repository.EpisodeRepository

/**
 * Use case for retrieving a paginated list of episodes.
 *
 * Business logic:
 * - Validates pagination parameters
 * - Retrieves episodes from the repository
 * - Returns episodes in domain model format
 *
 * This use case encapsulates the business rules for fetching episode lists.
 */
class GetEpisodesUseCase(
    private val episodeRepository: EpisodeRepository
) {
    /**
     * Executes the use case to get a page of episodes.
     *
     * @param page The page number to retrieve (must be >= 1)
     * @return Result containing a list of Episode models or an exception
     *
     * Business rules applied:
     * - Page number must be positive
     * - Returns empty list if no episodes found on that page
     */
    suspend operator fun invoke(page: Int = 1): Result<List<Episode>> {
        // Validate business rules before calling repository
        validatePage(page)

        // Delegate to repository for data access
        return episodeRepository.getEpisodes(page)
    }

    /**
     * Validates that a page number is valid for pagination.
     * Business rule: Page numbers must be positive integers starting at 1.
     */
    private fun validatePage(page: Int) {
        if (page < 1) {
            throw EpisodeException.InvalidEpisodePage(page = page)
        }
    }
}