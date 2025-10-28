package com.infinitum.labs.data.character.repository

import com.infinitum.labs.data.character.exception.CharacterDataException
import com.infinitum.labs.data.character.mapper.toDomain
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

/**
 * Repository implementation for Character domain operations.
 * Handles mapping from infrastructure exceptions to domain exceptions.
 */
class CharacterRepositoryImpl(
    private val remoteDataSource: CharacterRemoteDataSource
) : CharacterRepository {

    override suspend fun getCharacters(page: Int): Result<List<Character>> {
        return try {
            // Input validation is handled by Use Cases
            // Character constructor validates all fields on instantiation
            val response = remoteDataSource.getCharacters(page)
            val characters = response.results.map { it.toDomain() }
            Result.success(characters)
        } catch (e: Exception) {
            Result.failure(e.toCharacterDomainException())
        }
    }

    override suspend fun getCharacter(id: Int): Result<Character> {
        return try {
            // Input validation is handled by Use Cases
            // Character constructor validates all fields on instantiation
            val characterDto = remoteDataSource.getCharacter(id)
            val character = characterDto.toDomain()
            Result.success(character)
        } catch (e: Exception) {
            Result.failure(e.toCharacterDomainException(characterId = id))
        }
    }

    override suspend fun getCharactersByName(name: String, page: Int): Result<List<Character>> {
        return try {
            // Input validation is handled by Use Cases
            // Character constructor validates all fields on instantiation
            val response = remoteDataSource.getCharactersByName(name, page)
            val characters = response.results.map { it.toDomain() }
            Result.success(characters)
        } catch (e: Exception) {
            Result.failure(e.toCharacterDomainException(searchName = name))
        }
    }

    /**
     * Maps any exception to a Character domain exception.
     * Flow: Ktor Exception → CharacterDataException → CharacterException
     */
    private fun Exception.toCharacterDomainException(
        characterId: Int? = null,
        searchName: String? = null
    ): CharacterException {
        // First, convert infrastructure exceptions to CharacterDataException
        val dataException = when (this) {
            // Already domain or data exception - pass through
            is CharacterException -> return this
            is CharacterDataException -> this

            // HTTP 404 - specific handling based on context
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

            // HTTP 5xx - server errors
            is ServerResponseException -> CharacterDataException.CharacterApiServerError(
                statusCode = response.status.value,
                message = "Character API server error: ${response.status.description}"
            )

            // Network connectivity errors
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

            // JSON parsing errors
            is JsonConvertException -> CharacterDataException.CharacterDataParseError(
                message = "Failed to parse character data from API",
                cause = this
            )

            // Unknown errors
            else -> CharacterDataException.CharacterApiUnexpectedError(
                message = message ?: "Unexpected error when accessing character data",
                cause = this
            )
        }

        // Then, map CharacterDataException to CharacterException (domain)
        return dataException.toCharacterDomainException()
    }

    /**
     * Maps infrastructure CharacterDataException to domain CharacterException.
     * This translation keeps the domain clean and focused on Character operations.
     */
    private fun CharacterDataException.toCharacterDomainException(): CharacterException {
        return when (this) {
            // Direct mapping: API not found → Domain not found
            is CharacterDataException.CharacterNotFoundInApi ->
                CharacterException.CharacterNotFound(characterId = characterId)

            is CharacterDataException.CharactersNotFoundInApiByName ->
                CharacterException.CharactersNotFoundByName(searchName = searchName)

            // Infrastructure failures → Character repository unavailable
            is CharacterDataException.CharacterApiNetworkError,
            is CharacterDataException.CharacterApiServerError,
            is CharacterDataException.CharacterApiClientError ->
                CharacterException.CharacterRepositoryUnavailable(
                    message = "The character catalog is temporarily unavailable",
                    cause = this
                )

            // Parse errors → Invalid character data
            is CharacterDataException.CharacterDataParseError ->
                CharacterException.InvalidCharacterData(
                    message = "Character data is invalid or corrupted",
                    cause = this
                )

            // Unexpected API errors
            is CharacterDataException.CharacterApiUnexpectedError ->
                CharacterException.CharacterRepositoryUnavailable(
                    message = "The character catalog encountered an unexpected error",
                    cause = this
                )
        }
    }

}