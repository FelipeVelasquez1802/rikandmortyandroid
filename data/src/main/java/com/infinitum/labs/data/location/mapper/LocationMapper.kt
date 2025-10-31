package com.infinitum.labs.data.location.mapper

import com.infinitum.labs.data.location.remote.dto.LocationDto
import com.infinitum.labs.domain.location.model.Location

internal object LocationMapper {

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