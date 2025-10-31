package com.infinitum.labs.data.episode.mapper

import com.infinitum.labs.data.episode.remote.dto.EpisodeDto
import com.infinitum.labs.domain.episode.model.Episode

/**
 * Mapper object for converting between Episode DTOs and domain models.
 * Handles the transformation between the data layer and domain layer representations.
 *
 * Marked as internal to prevent leaking data layer details to other modules.
 */
internal object EpisodeMapper {

    /**
     * Converts an EpisodeDto from the API into an Episode domain model.
     *
     * @param dto The data transfer object from the API
     * @return A domain model with validated business rules
     * @throws EpisodeException if validation fails during domain model construction
     */
    fun toDomain(dto: EpisodeDto): Episode {
        return Episode(
            id = dto.id,
            name = dto.name,
            airDate = dto.airDate,
            episode = dto.episode,
            characters = dto.characters,
            url = dto.url,
            created = dto.created
        )
    }
}