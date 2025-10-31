package com.infinitum.labs.domain.episode.usecase

import com.infinitum.labs.domain.episode.exception.EpisodeException
import com.infinitum.labs.domain.episode.model.Episode
import com.infinitum.labs.domain.episode.repository.EpisodeRepository

class SearchEpisodesByNameUseCase(
    private val episodeRepository: EpisodeRepository
) {
    suspend operator fun invoke(
        name: String,
        page: Int = 1
    ): Result<List<Episode>> {
        validateSearchQuery(name)
        validatePage(page)
        return episodeRepository.getEpisodesByName(name, page)
    }

    private fun validateSearchQuery(query: String) {
        if (query.isBlank()) {
            throw EpisodeException.InvalidEpisodeSearchQuery(query = query)
        }
    }

    private fun validatePage(page: Int) {
        if (page < 1) {
            throw EpisodeException.InvalidEpisodePage(page = page)
        }
    }
}