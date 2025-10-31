package com.infinitum.labs.domain.episode.usecase

import com.infinitum.labs.domain.episode.exception.EpisodeException
import com.infinitum.labs.domain.episode.model.Episode
import com.infinitum.labs.domain.episode.repository.EpisodeRepository

class GetEpisodeByIdUseCase(
    private val episodeRepository: EpisodeRepository
) {
    suspend operator fun invoke(episodeId: Int): Result<Episode> {
        validateEpisodeId(episodeId)
        return episodeRepository.getEpisode(episodeId)
    }

    private fun validateEpisodeId(episodeId: Int) {
        if (episodeId < 1) {
            throw EpisodeException.InvalidEpisodeId(episodeId = episodeId)
        }
    }
}