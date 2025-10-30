package com.infinitum.labs.domain.location.usecase

import com.infinitum.labs.domain.location.exception.LocationException
import com.infinitum.labs.domain.location.model.Location
import com.infinitum.labs.domain.location.repository.LocationRepository

/**
 * Use case for retrieving a paginated list of locations.
 *
 * Business logic:
 * - Validates pagination parameters
 * - Retrieves locations from the repository
 * - Returns locations in domain model format
 *
 * This use case encapsulates the business rules for fetching location lists.
 */
class GetLocationsUseCase(
    private val locationRepository: LocationRepository
) {
    /**
     * Executes the use case to get a page of locations.
     *
     * @param page The page number to retrieve (must be >= 1)
     * @return Result containing a list of Location models or an exception
     *
     * Business rules applied:
     * - Page number must be positive
     * - Returns empty list if no locations found on that page
     */
    suspend operator fun invoke(page: Int = 1): Result<List<Location>> {
        // Validate business rules before calling repository
        validatePage(page)

        // Delegate to repository for data access
        return locationRepository.getLocations(page)
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
