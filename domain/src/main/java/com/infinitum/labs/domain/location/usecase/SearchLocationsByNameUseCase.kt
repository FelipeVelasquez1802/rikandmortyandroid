package com.infinitum.labs.domain.location.usecase

import com.infinitum.labs.domain.location.exception.LocationException
import com.infinitum.labs.domain.location.model.Location
import com.infinitum.labs.domain.location.repository.LocationRepository

class SearchLocationsByNameUseCase(
    private val locationRepository: LocationRepository
) {
    suspend operator fun invoke(
        name: String,
        page: Int = 1
    ): Result<List<Location>> {
        validateSearchQuery(name)
        validatePage(page)
        return locationRepository.getLocationsByName(name, page)
    }

    private fun validateSearchQuery(query: String) {
        if (query.isBlank()) {
            throw LocationException.InvalidLocationSearchQuery(query = query)
        }
    }

    private fun validatePage(page: Int) {
        if (page < 1) {
            throw LocationException.InvalidLocationPage(page = page)
        }
    }
}