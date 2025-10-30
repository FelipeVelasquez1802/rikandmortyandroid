package com.infinitum.labs.domain.location.usecase

import com.infinitum.labs.domain.location.exception.LocationException
import com.infinitum.labs.domain.location.model.Location
import com.infinitum.labs.domain.location.repository.LocationRepository

/**
 * Use case for retrieving a specific location by its ID.
 *
 * Business logic:
 * - Validates location ID
 * - Retrieves the location from the repository
 * - Returns the location in domain model format
 *
 * This use case encapsulates the business rules for fetching a single location.
 */
class GetLocationByIdUseCase(
    private val locationRepository: LocationRepository
) {
    /**
     * Executes the use case to get a location by its ID.
     *
     * @param locationId The unique identifier of the location (must be >= 1)
     * @return Result containing the Location model or an exception
     *
     * Business rules applied:
     * - Location ID must be positive
     * - Throws LocationNotFoundException if location doesn't exist
     */
    suspend operator fun invoke(locationId: Int): Result<Location> {
        // Validate business rules before calling repository
        validateLocationId(locationId)

        // Delegate to repository for data access
        return locationRepository.getLocation(locationId)
    }

    /**
     * Validates that a location ID is valid.
     * Business rule: Location IDs must be positive integers.
     */
    private fun validateLocationId(locationId: Int) {
        if (locationId < 1) {
            throw LocationException.InvalidLocationId(locationId = locationId)
        }
    }
}