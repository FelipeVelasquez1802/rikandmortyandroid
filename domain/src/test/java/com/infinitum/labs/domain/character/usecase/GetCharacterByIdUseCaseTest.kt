package com.infinitum.labs.domain.character.usecase

import com.infinitum.labs.domain.character.exception.CharacterException
import com.infinitum.labs.domain.character.model.Character
import com.infinitum.labs.domain.character.model.builder.CharacterBuilder
import com.infinitum.labs.domain.character.repository.CharacterRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test

class GetCharacterByIdUseCaseTest {

    private lateinit var useCase: GetCharacterByIdUseCase
    private lateinit var mockRepository: FakeCharacterRepository

    @Before
    fun setUp() {
        mockRepository = FakeCharacterRepository()
        useCase = GetCharacterByIdUseCase(mockRepository)
    }

    @Test
    fun `invoke with valid ID should return character from repository`() = runTest {
        // Given
        val characterId = 1
        val expectedCharacter = CharacterBuilder.rickSanchez().build()
        mockRepository.characterToReturn = Result.success(expectedCharacter)

        // When
        val result = useCase.invoke(characterId)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(expectedCharacter, result.getOrNull())
        assertEquals(characterId, mockRepository.lastIdRequested)
    }

    @Test
    fun `invoke with different IDs should request correct characters`() = runTest {
        // Given
        val ids = listOf(1, 2, 3, 100)
        mockRepository.characterToReturn = Result.success(CharacterBuilder.aCharacter().build())

        // When & Then
        ids.forEach { id ->
            useCase.invoke(id)
            assertEquals(id, mockRepository.lastIdRequested)
        }
    }

    @Test
    fun `invoke with invalid ID should throw InvalidCharacterId exception`() = runTest {
        // Given
        val invalidId = 0

        // When & Then
        try {
            useCase.invoke(invalidId)
            fail("Should have thrown InvalidCharacterId exception")
        } catch (e: CharacterException.InvalidCharacterId) {
            assertEquals(invalidId, e.characterId)
        }
    }

    @Test
    fun `invoke with negative ID should throw InvalidCharacterId exception`() = runTest {
        // Given
        val negativeId = -5

        // When & Then
        try {
            useCase.invoke(negativeId)
            fail("Should have thrown InvalidCharacterId exception")
        } catch (e: CharacterException.InvalidCharacterId) {
            assertEquals(negativeId, e.characterId)
        }
    }

    @Test
    fun `invoke should propagate CharacterNotFound exception from repository`() = runTest {
        // Given
        val characterId = 999
        val error = CharacterException.CharacterNotFound(characterId)
        mockRepository.characterToReturn = Result.failure(error)

        // When
        val result = useCase.invoke(characterId)

        // Then
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull() as? CharacterException.CharacterNotFound
        assertEquals(characterId, exception?.characterId)
    }

    @Test
    fun `invoke should propagate repository unavailable exception`() = runTest {
        // Given
        val error = CharacterException.CharacterRepositoryUnavailable()
        mockRepository.characterToReturn = Result.failure(error)

        // When
        val result = useCase.invoke(1)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is CharacterException.CharacterRepositoryUnavailable)
    }

    // Fake repository for testing
    private class FakeCharacterRepository : CharacterRepository {
        var characterToReturn: Result<Character> = Result.success(CharacterBuilder.aCharacter().build())
        var lastIdRequested: Int = 0

        override suspend fun getCharacters(page: Int): Result<List<Character>> {
            throw NotImplementedError("Not used in these tests")
        }

        override suspend fun getCharacter(id: Int): Result<Character> {
            lastIdRequested = id
            return characterToReturn
        }

        override suspend fun getCharactersByName(name: String, page: Int): Result<List<Character>> {
            throw NotImplementedError("Not used in these tests")
        }
    }
}