package com.infinitum.labs.data.episode.remote.dto

import com.infinitum.labs.data.character.remote.dto.InfoDto
import kotlinx.serialization.Serializable

@Serializable
internal data class EpisodeResponseDto(
    val info: InfoDto,
    val results: List<EpisodeDto>
)