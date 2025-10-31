package com.infinitum.labs.domain.location.usecase

import com.infinitum.labs.domain.location.exception.LocationException
import com.infinitum.labs.domain.location.model.Location
import com.infinitum.labs.domain.location.repository.LocationRepository

/**
 * Use case for searching locations by name with pagination.
 *
 * Business logic:
 * - Validates search query and pagination parameters
 * - Searches locations in the repository
 * - Returns matching locations in domain model format
 *
 * This use case encapsulates the business rules for location search operations.
 */
class SearchLocationsByNameUseCase(
    private val locationRepository: LocationRepository
) {
    /**
     * Executes the use case to search for locations by name.
     *
     * @param name The search query (location name or partial name)
     * @param page The page number for pagination (must be >= 1)
     * @return Result containing a list of matching Location models or an exception
     *
     * Business rules applied:
     * - Search query cannot be blank
     * - Page number must be positive
     * - Returns empty list if no locations match the search
     * - Search is case-insensitive (handled by API)
     */
    suspend operator fun invoke(
        name: String,
        page: Int = 1
    ): Result<List<Location>> {
        // Validate business rules before calling repository
        validateSearchQuery(name)
        validatePage(page)

        // Delegate to repository for data access
        return locationRepository.getLocationsByName(name, page)
    }

    /**
     * Validates that a search query is valid.
     * Business rule: Search queries cannot be blank.
     */
    private fun validateSearchQuery(query: String) {
        if (query.isBlank()) {
            throw LocationException.InvalidLocationSearchQuery(query = query)
        }
    }

    /**
     * Validates that a page number is valid for pagination.
     * Business rule: Page numbers must be positive integers starting at 1.
     */
    private fun validatePage(page: Int) {
        if (page < 1) {
            throw LocationException.InvalidLocationPage(page = page)
        }
    }
}