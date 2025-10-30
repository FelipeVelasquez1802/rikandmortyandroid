package com.infinitum.labs.data.episode.remote.datasource

import com.infinitum.labs.data.episode.remote.dto.EpisodeDto
import com.infinitum.labs.data.episode.remote.dto.EpisodeResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

internal interface EpisodeRemoteDataSource {
    suspend fun getEpisodes(page: Int): EpisodeResponseDto
    suspend fun getEpisode(id: Int): EpisodeDto
    suspend fun getEpisodesByName(name: String, page: Int): EpisodeResponseDto
}

internal class EpisodeRemoteDataSourceImpl(
    private val httpClient: HttpClient
) : EpisodeRemoteDataSource {

    companion object {
        private const val BASE_URL = "https://rickandmortyapi.com/api"
        private const val EPISODE_ENDPOINT = "$BASE_URL/episode"
    }

    override suspend fun getEpisodes(page: Int): EpisodeResponseDto {
        return httpClient.get(EPISODE_ENDPOINT) {
            parameter("page", page)
        }.body()
    }

    override suspend fun getEpisode(id: Int): EpisodeDto {
        return httpClient.get("$EPISODE_ENDPOINT/$id").body()
    }

    override suspend fun getEpisodesByName(name: String, page: Int): EpisodeResponseDto {
        return httpClient.get(EPISODE_ENDPOINT) {
            parameter("name", name)
            parameter("page", page)
        }.body()
    }
}