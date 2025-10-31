package com.infinitum.labs.data.episode.mapper

import com.infinitum.labs.data.episode.remote.dto.EpisodeDto
import com.infinitum.labs.domain.episode.model.Episode

internal object EpisodeMapper {

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