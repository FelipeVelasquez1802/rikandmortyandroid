package com.infinitum.labs.domain.episode.usecase

import com.infinitum.labs.domain.episode.exception.EpisodeException
import com.infinitum.labs.domain.episode.model.Episode
import com.infinitum.labs.domain.episode.repository.EpisodeRepository

/**
 * Use case for searching episodes by name with pagination.
 *
 * Business logic:
 * - Validates search query and pagination parameters
 * - Searches episodes in the repository
 * - Returns matching episodes in domain model format
 *
 * This use case encapsulates the business rules for episode search operations.
 */
class SearchEpisodesByNameUseCase(
    private val episodeRepository: EpisodeRepository
) {
    /**
     * Executes the use case to search for episodes by name.
     *
     * @param name The search query (episode name or partial name)
     * @param page The page number for pagination (must be >= 1)
     * @return Result containing a list of matching Episode models or an exception
     *
     * Business rules applied:
     * - Search query cannot be blank
     * - Page number must be positive
     * - Returns empty list if no episodes match the search
     * - Search is case-insensitive (handled by API)
     */
    suspend operator fun invoke(
        name: String,
        page: Int = 1
    ): Result<List<Episode>> {
        // Validate business rules before calling repository
        validateSearchQuery(name)
        validatePage(page)

        // Delegate to repository for data access
        return episodeRepository.getEpisodesByName(name, page)
    }

    /**
     * Validates that a search query is valid.
     * Business rule: Search queries cannot be blank.
     */
    private fun validateSearchQuery(query: String) {
        if (query.isBlank()) {
            throw EpisodeException.InvalidEpisodeSearchQuery(query = query)
        }
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