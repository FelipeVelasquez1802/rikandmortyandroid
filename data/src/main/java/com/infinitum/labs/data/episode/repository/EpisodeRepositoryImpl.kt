package com.infinitum.labs.data.episode.repository

import com.infinitum.labs.data.episode.exception.EpisodeDataException
import com.infinitum.labs.data.episode.mapper.EpisodeMapper
import com.infinitum.labs.data.episode.remote.datasource.EpisodeRemoteDataSource
import com.infinitum.labs.domain.episode.exception.EpisodeException
import com.infinitum.labs.domain.episode.model.Episode
import com.infinitum.labs.domain.episode.repository.EpisodeRepository
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.serialization.JsonConvertException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

internal class EpisodeRepositoryImpl(
    private val remoteDataSource: EpisodeRemoteDataSource
) : EpisodeRepository {

    override suspend fun getEpisodes(page: Int): Result<List<Episode>> {
        return try {
            val response = remoteDataSource.getEpisodes(page)
            val episodes = response.results.map { EpisodeMapper.toDomain(it) }
            Result.success(episodes)
        } catch (e: Exception) {
            Result.failure(e.toEpisodeDomainException())
        }
    }

    override suspend fun getEpisode(id: Int): Result<Episode> {
        return try {
            val episodeDto = remoteDataSource.getEpisode(id)
            val episode = EpisodeMapper.toDomain(episodeDto)
            Result.success(episode)
        } catch (e: Exception) {
            Result.failure(e.toEpisodeDomainException(episodeId = id))
        }
    }

    override suspend fun getEpisodesByName(name: String, page: Int): Result<List<Episode>> {
        return try {
            val response = remoteDataSource.getEpisodesByName(name, page)
            val episodes = response.results.map { EpisodeMapper.toDomain(it) }
            Result.success(episodes)
        } catch (e: Exception) {
            Result.failure(e.toEpisodeDomainException(searchName = name))
        }
    }

    private fun Exception.toEpisodeDomainException(
        episodeId: Int? = null,
        searchName: String? = null
    ): EpisodeException {
        val dataException = when (this) {
            is EpisodeException -> return this
            is EpisodeDataException -> this

            is ClientRequestException -> {
                when (response.status.value) {
                    404 -> {
                        if (episodeId != null) {
                            EpisodeDataException.EpisodeNotFoundInApi(episodeId)
                        } else if (searchName != null) {
                            EpisodeDataException.EpisodesNotFoundInApiByName(searchName)
                        } else {
                            EpisodeDataException.EpisodeApiClientError(404)
                        }
                    }

                    else -> EpisodeDataException.EpisodeApiClientError(
                        statusCode = response.status.value,
                        message = "Episode API request failed: ${response.status.description}"
                    )
                }
            }

            is ServerResponseException -> EpisodeDataException.EpisodeApiServerError(
                statusCode = response.status.value,
                message = "Episode API server error: ${response.status.description}"
            )

            is UnknownHostException -> EpisodeDataException.EpisodeApiNetworkError(
                message = "Cannot reach episode API server",
                cause = this
            )

            is SocketTimeoutException -> EpisodeDataException.EpisodeApiNetworkError(
                message = "Connection to episode API timed out",
                cause = this
            )

            is ConnectException -> EpisodeDataException.EpisodeApiNetworkError(
                message = "Failed to connect to episode API",
                cause = this
            )

            is JsonConvertException -> EpisodeDataException.EpisodeDataParseError(
                message = "Failed to parse episode data from API",
                cause = this
            )

            else -> EpisodeDataException.EpisodeApiUnexpectedError(
                message = message ?: "Unexpected error when accessing episode data",
                cause = this
            )
        }

        return dataException.toEpisodeDomainException()
    }

    private fun EpisodeDataException.toEpisodeDomainException(): EpisodeException {
        return when (this) {
            is EpisodeDataException.EpisodeNotFoundInApi ->
                EpisodeException.EpisodeNotFound(episodeId = episodeId)

            is EpisodeDataException.EpisodesNotFoundInApiByName ->
                EpisodeException.EpisodesNotFoundByName(searchName = searchName)

            is EpisodeDataException.EpisodeApiNetworkError,
            is EpisodeDataException.EpisodeApiServerError,
            is EpisodeDataException.EpisodeApiClientError ->
                EpisodeException.EpisodeRepositoryUnavailable(
                    message = "The episode catalog is temporarily unavailable",
                    cause = this
                )

            is EpisodeDataException.EpisodeDataParseError ->
                EpisodeException.InvalidEpisodeData(
                    message = "Episode data is invalid or corrupted",
                    cause = this
                )

            is EpisodeDataException.EpisodeApiUnexpectedError ->
                EpisodeException.EpisodeRepositoryUnavailable(
                    message = "The episode catalog encountered an unexpected error",
                    cause = this
                )
        }
    }

}