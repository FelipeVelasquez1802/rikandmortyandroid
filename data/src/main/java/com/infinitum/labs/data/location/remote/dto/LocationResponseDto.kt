package com.infinitum.labs.data.location.remote.dto

import com.infinitum.labs.data.character.remote.dto.InfoDto
import kotlinx.serialization.Serializable

/**
 * Data Transfer Object for paginated Location responses from the Rick and Morty API.
 * Contains pagination information and the list of locations.
 *
 * Marked as internal to prevent leaking data layer details to other modules.
 */
@Serializable
internal data class LocationResponseDto(
    val info: InfoDto,
    val results: List<LocationDto>
)