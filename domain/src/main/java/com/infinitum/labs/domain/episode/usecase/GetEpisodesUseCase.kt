package com.infinitum.labs.domain.episode.usecase

import com.infinitum.labs.domain.episode.exception.EpisodeException
import com.infinitum.labs.domain.episode.model.Episode
import com.infinitum.labs.domain.episode.repository.EpisodeRepository

class GetEpisodesUseCase(
    private val episodeRepository: EpisodeRepository
) {
    suspend operator fun invoke(page: Int = 1): Result<List<Episode>> {
        validatePage(page)
        return episodeRepository.getEpisodes(page)
    }

    private fun validatePage(page: Int) {
        if (page < 1) {
            throw EpisodeException.InvalidEpisodePage(page = page)
        }
    }
}