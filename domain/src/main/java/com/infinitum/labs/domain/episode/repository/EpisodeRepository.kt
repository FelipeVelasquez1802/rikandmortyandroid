package com.infinitum.labs.domain.episode.repository

import com.infinitum.labs.domain.episode.model.Episode

interface EpisodeRepository {
    suspend fun getEpisodes(page: Int = 1): Result<List<Episode>>
    suspend fun getEpisode(id: Int): Result<Episode>
    suspend fun getEpisodesByName(name: String, page: Int = 1): Result<List<Episode>>
}