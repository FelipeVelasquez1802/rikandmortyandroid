package com.infinitum.labs.data.character.remote.datasource

import com.infinitum.labs.data.character.remote.dto.CharacterDto
import com.infinitum.labs.data.character.remote.dto.CharacterResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

interface CharacterRemoteDataSource {
    suspend fun getCharacters(page: Int): CharacterResponseDto
    suspend fun getCharacter(id: Int): CharacterDto
    suspend fun getCharactersByName(name: String, page: Int): CharacterResponseDto
}

class CharacterRemoteDataSourceImpl(
    private val httpClient: HttpClient
) : CharacterRemoteDataSource {

    companion object {
        private const val BASE_URL = "https://rickandmortyapi.com/api"
        private const val CHARACTER_ENDPOINT = "$BASE_URL/character"
    }

    override suspend fun getCharacters(page: Int): CharacterResponseDto {
        return httpClient.get(CHARACTER_ENDPOINT) {
            parameter("page", page)
        }.body()
    }

    override suspend fun getCharacter(id: Int): CharacterDto {
        return httpClient.get("$CHARACTER_ENDPOINT/$id").body()
    }

    override suspend fun getCharactersByName(name: String, page: Int): CharacterResponseDto {
        return httpClient.get(CHARACTER_ENDPOINT) {
            parameter("name", name)
            parameter("page", page)
        }.body()
    }
}