package com.infinitum.labs.domain.location.usecase

import com.infinitum.labs.domain.location.exception.LocationException
import com.infinitum.labs.domain.location.model.Location
import com.infinitum.labs.domain.location.repository.LocationRepository

class GetLocationsUseCase(
    private val locationRepository: LocationRepository
) {
    suspend operator fun invoke(page: Int = 1): Result<List<Location>> {
        validatePage(page)
        return locationRepository.getLocations(page)
    }

    private fun validatePage(page: Int) {
        if (page < 1) {
            throw LocationException.InvalidLocationPage(page = page)
        }
    }
}
