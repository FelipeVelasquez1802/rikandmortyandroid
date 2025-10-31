package com.infinitum.labs.domain.location.usecase

import com.infinitum.labs.domain.location.exception.LocationException
import com.infinitum.labs.domain.location.model.Location
import com.infinitum.labs.domain.location.repository.LocationRepository

class GetLocationByIdUseCase(
    private val locationRepository: LocationRepository
) {
    suspend operator fun invoke(locationId: Int): Result<Location> {
        validateLocationId(locationId)
        return locationRepository.getLocation(locationId)
    }

    private fun validateLocationId(locationId: Int) {
        if (locationId < 1) {
            throw LocationException.InvalidLocationId(locationId = locationId)
        }
    }
}