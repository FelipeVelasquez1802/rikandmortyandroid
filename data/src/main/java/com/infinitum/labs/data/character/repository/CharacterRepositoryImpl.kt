package com.infinitum.labs.data.character.repository

import com.infinitum.labs.data.character.mapper.toDomain
import com.infinitum.labs.data.character.remote.datasource.CharacterRemoteDataSource
import com.infinitum.labs.domain.character.model.Character
import com.infinitum.labs.domain.character.repository.CharacterRepository

class CharacterRepositoryImpl(
    private val remoteDataSource: CharacterRemoteDataSource
) : CharacterRepository {

    override suspend fun getCharacters(page: Int): Result<List<Character>> {
        return try {
            val response = remoteDataSource.getCharacters(page)
            val characters = response.results.map { it.toDomain() }
            Result.success(characters)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCharacter(id: Int): Result<Character> {
        return try {
            val characterDto = remoteDataSource.getCharacter(id)
            val character = characterDto.toDomain()
            Result.success(character)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCharactersByName(name: String, page: Int): Result<List<Character>> {
        return try {
            val response = remoteDataSource.getCharactersByName(name, page)
            val characters = response.results.map { it.toDomain() }
            Result.success(characters)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}