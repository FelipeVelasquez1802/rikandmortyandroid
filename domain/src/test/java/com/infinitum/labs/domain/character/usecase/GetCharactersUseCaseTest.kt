package com.infinitum.labs.domain.character.usecase

import com.infinitum.labs.domain.character.exception.CharacterException
import com.infinitum.labs.domain.character.model.builder.CharacterBuilder
import com.infinitum.labs.domain.character.repository.CharacterRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test

class GetCharactersUseCaseTest {

    private lateinit var useCase: GetCharactersUseCase
    private lateinit var mockRepository: FakeCharacterRepository

    @Before
    fun setUp() {
        mockRepository = FakeCharacterRepository()
        useCase = GetCharactersUseCase(mockRepository)
    }

    @Test
    fun `invoke with valid page should return characters from repository`() = runTest {
        // Given
        val page = 1
        val expectedCharacters = listOf(
            CharacterBuilder.rickSanchez().build(),
            CharacterBuilder.mortySmith().build()
        )
        mockRepository.charactersToReturn = Result.success(expectedCharacters)

        // When
        val result = useCase.invoke(page)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(expectedCharacters, result.getOrNull())
        assertEquals(page, mockRepository.lastPageRequested)
    }

    @Test
    fun `invoke with default page should use page 1`() = runTest {
        // Given
        mockRepository.charactersToReturn = Result.success(emptyList())

        // When
        useCase.invoke()

        // Then
        assertEquals(1, mockRepository.lastPageRequested)
    }

    @Test
    fun `invoke with invalid page should throw InvalidCharacterPage exception`() = runTest {
        // Given
        val invalidPage = 0

        // When & Then
        try {
            useCase.invoke(invalidPage)
            fail("Should have thrown InvalidCharacterPage exception")
        } catch (e: CharacterException.InvalidCharacterPage) {
            assertEquals(invalidPage, e.page)
        }
    }

    @Test
    fun `invoke with negative page should throw InvalidCharacterPage exception`() = runTest {
        // Given
        val negativePage = -1

        // When & Then
        try {
            useCase.invoke(negativePage)
            fail("Should have thrown InvalidCharacterPage exception")
        } catch (e: CharacterException.InvalidCharacterPage) {
            assertEquals(negativePage, e.page)
        }
    }

    @Test
    fun `invoke should propagate repository errors`() = runTest {
        // Given
        val error = CharacterException.CharacterRepositoryUnavailable()
        mockRepository.charactersToReturn = Result.failure(error)

        // When
        val result = useCase.invoke(page = 1)

        // Then
        assertTrue(result.isFailure)
        assertEquals(error, result.exceptionOrNull())
    }

    @Test
    fun `invoke with multiple pages should work correctly`() = runTest {
        // Given
        val pages = listOf(1, 2, 3, 10)
        mockRepository.charactersToReturn = Result.success(emptyList())

        // When
        pages.forEach { page ->
            useCase.invoke(page)
        }

        // Then
        assertEquals(10, mockRepository.lastPageRequested) // Last page requested
    }

    // Fake repository for testing
    private class FakeCharacterRepository : CharacterRepository {
        var charactersToReturn: Result<List<com.infinitum.labs.domain.character.model.Character>> =
            Result.success(emptyList())
        var lastPageRequested: Int = 0

        override suspend fun getCharacters(page: Int): Result<List<com.infinitum.labs.domain.character.model.Character>> {
            lastPageRequested = page
            return charactersToReturn
        }

        override suspend fun getCharacter(id: Int): Result<com.infinitum.labs.domain.character.model.Character> {
            throw NotImplementedError("Not used in these tests")
        }

        override suspend fun getCharactersByName(name: String, page: Int): Result<List<com.infinitum.labs.domain.character.model.Character>> {
            throw NotImplementedError("Not used in these tests")
        }
    }
}