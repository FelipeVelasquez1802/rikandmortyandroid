package com.infinitum.labs.data.episode.remote.dto

import com.infinitum.labs.data.character.remote.dto.InfoDto
import kotlinx.serialization.Serializable

/**
 * Data Transfer Object for paginated Episode responses from the Rick and Morty API.
 * Contains pagination information and the list of episodes.
 *
 * Marked as internal to prevent leaking data layer details to other modules.
 */
@Serializable
internal data class EpisodeResponseDto(
    val info: InfoDto,
    val results: List<EpisodeDto>
)