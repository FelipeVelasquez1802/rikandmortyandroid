package com.infinitum.labs.data.character.repository

import com.infinitum.labs.data.character.exception.CharacterDataException
import com.infinitum.labs.data.character.mapper.CharacterMapper
import com.infinitum.labs.data.character.remote.datasource.CharacterRemoteDataSource
import com.infinitum.labs.domain.character.exception.CharacterException
import com.infinitum.labs.domain.character.model.Character
import com.infinitum.labs.domain.character.repository.CharacterRepository
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.serialization.JsonConvertException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

internal class CharacterRepositoryImpl(
    private val remoteDataSource: CharacterRemoteDataSource
) : CharacterRepository {

    override suspend fun getCharacters(page: Int): Result<List<Character>> {
        return try {
            val response = remoteDataSource.getCharacters(page)
            val characters = response.results.map { CharacterMapper.toDomain(it) }
            Result.success(characters)
        } catch (e: Exception) {
            Result.failure(e.toCharacterDomainException())
        }
    }

    override suspend fun getCharacter(id: Int): Result<Character> {
        return try {
            val characterDto = remoteDataSource.getCharacter(id)
            val character = CharacterMapper.toDomain(characterDto)
            Result.success(character)
        } catch (e: Exception) {
            Result.failure(e.toCharacterDomainException(characterId = id))
        }
    }

    override suspend fun getCharactersByName(name: String, page: Int): Result<List<Character>> {
        return try {
            val response = remoteDataSource.getCharactersByName(name, page)
            val characters = response.results.map { CharacterMapper.toDomain(it) }
            Result.success(characters)
        } catch (e: Exception) {
            Result.failure(e.toCharacterDomainException(searchName = name))
        }
    }

    private fun Exception.toCharacterDomainException(
        characterId: Int? = null,
        searchName: String? = null
    ): CharacterException {
        val dataException = when (this) {
            is CharacterException -> return this
            is CharacterDataException -> this

            is ClientRequestException -> {
                when (response.status.value) {
                    404 -> {
                        if (characterId != null) {
                            CharacterDataException.CharacterNotFoundInApi(characterId)
                        } else if (searchName != null) {
                            CharacterDataException.CharactersNotFoundInApiByName(searchName)
                        } else {
                            CharacterDataException.CharacterApiClientError(404)
                        }
                    }

                    else -> CharacterDataException.CharacterApiClientError(
                        statusCode = response.status.value,
                        message = "Character API request failed: ${response.status.description}"
                    )
                }
            }

            is ServerResponseException -> CharacterDataException.CharacterApiServerError(
                statusCode = response.status.value,
                message = "Character API server error: ${response.status.description}"
            )

            is UnknownHostException -> CharacterDataException.CharacterApiNetworkError(
                message = "Cannot reach character API server",
                cause = this
            )

            is SocketTimeoutException -> CharacterDataException.CharacterApiNetworkError(
                message = "Connection to character API timed out",
                cause = this
            )

            is ConnectException -> CharacterDataException.CharacterApiNetworkError(
                message = "Failed to connect to character API",
                cause = this
            )

            is JsonConvertException -> CharacterDataException.CharacterDataParseError(
                message = "Failed to parse character data from API",
                cause = this
            )

            else -> CharacterDataException.CharacterApiUnexpectedError(
                message = message ?: "Unexpected error when accessing character data",
                cause = this
            )
        }

        return dataException.toCharacterDomainException()
    }

    private fun CharacterDataException.toCharacterDomainException(): CharacterException {
        return when (this) {
            is CharacterDataException.CharacterNotFoundInApi ->
                CharacterException.CharacterNotFound(characterId = characterId)

            is CharacterDataException.CharactersNotFoundInApiByName ->
                CharacterException.CharactersNotFoundByName(searchName = searchName)

            is CharacterDataException.CharacterApiNetworkError,
            is CharacterDataException.CharacterApiServerError,
            is CharacterDataException.CharacterApiClientError ->
                CharacterException.CharacterRepositoryUnavailable(
                    message = "The character catalog is temporarily unavailable",
                    cause = this
                )

            is CharacterDataException.CharacterDataParseError ->
                CharacterException.InvalidCharacterData(
                    message = "Character data is invalid or corrupted",
                    cause = this
                )

            is CharacterDataException.CharacterApiUnexpectedError ->
                CharacterException.CharacterRepositoryUnavailable(
                    message = "The character catalog encountered an unexpected error",
                    cause = this
                )
        }
    }

}