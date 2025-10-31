package com.infinitum.labs.data.location.remote.dto

import com.infinitum.labs.data.character.remote.dto.InfoDto
import kotlinx.serialization.Serializable

@Serializable
internal data class LocationResponseDto(
    val info: InfoDto,
    val results: List<LocationDto>
)