package com.infinitum.labs.data.location.mapper

import com.infinitum.labs.data.location.remote.dto.LocationDto
import com.infinitum.labs.domain.location.model.Location

/**
 * Mapper object for converting between Location DTOs and domain models.
 * Handles the transformation between the data layer and domain layer representations.
 *
 * Marked as internal to prevent leaking data layer details to other modules.
 */
internal object LocationMapper {

    /**
     * Converts a LocationDto from the API into a Location domain model.
     *
     * @param dto The data transfer object from the API
     * @return A domain model with validated business rules
     * @throws LocationException if validation fails during domain model construction
     */
    fun toDomain(dto: LocationDto): Location {
        return Location(
            id = dto.id,
            name = dto.name,
            type = dto.type,
            dimension = dto.dimension,
            residents = dto.residents,
            url = dto.url,
            created = dto.created
        )
    }
}