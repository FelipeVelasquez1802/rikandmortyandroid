package com.infinitum.labs.domain.character.usecase

import com.infinitum.labs.domain.character.exception.CharacterException
import com.infinitum.labs.domain.character.model.Character
import com.infinitum.labs.domain.character.model.builder.CharacterBuilder
import com.infinitum.labs.domain.character.repository.CharacterRepository
import com.infinitum.labs.domain.common.model.DataResult
import com.infinitum.labs.domain.common.model.DataSource
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test

class SearchCharactersByNameUseCaseTest {

    private lateinit var useCase: SearchCharactersByNameUseCase
    private lateinit var mockRepository: FakeCharacterRepository

    @Before
    fun setUp() {
        mockRepository = FakeCharacterRepository()
        useCase = SearchCharactersByNameUseCase(mockRepository)
    }

    @Test
    fun `invoke with valid name and page should return characters from repository`() = runTest {
        // Given
        val searchName = "Rick"
        val page = 1
        val expectedCharacters = listOf(
            CharacterBuilder.rickSanchez().build()
        )
        mockRepository.charactersToReturn = Result.success(DataResult(expectedCharacters, DataSource.API))

        // When
        val result = useCase.invoke(searchName, page)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(expectedCharacters, result.getOrNull()?.data)
        assertEquals(searchName, mockRepository.lastNameSearched)
        assertEquals(page, mockRepository.lastPageRequested)
    }

    @Test
    fun `invoke with name only should use default page 1`() = runTest {
        // Given
        val searchName = "Morty"
        mockRepository.charactersToReturn = Result.success(DataResult(emptyList(), DataSource.API))

        // When
        useCase.invoke(searchName)

        // Then
        assertEquals(searchName, mockRepository.lastNameSearched)
        assertEquals(1, mockRepository.lastPageRequested)
    }

    @Test
    fun `invoke with blank name should throw InvalidCharacterSearchQuery exception`() = runTest {
        // Given
        val blankName = "   "

        // When & Then
        try {
            useCase.invoke(blankName)
            fail("Should have thrown InvalidCharacterSearchQuery exception")
        } catch (e: CharacterException.InvalidCharacterSearchQuery) {
            assertEquals(blankName, e.query)
        }
    }

    @Test
    fun `invoke with empty name should throw InvalidCharacterSearchQuery exception`() = runTest {
        // Given
        val emptyName = ""

        // When & Then
        try {
            useCase.invoke(emptyName)
            fail("Should have thrown InvalidCharacterSearchQuery exception")
        } catch (e: CharacterException.InvalidCharacterSearchQuery) {
            assertEquals(emptyName, e.query)
        }
    }

    @Test
    fun `invoke with invalid page should throw InvalidCharacterPage exception`() = runTest {
        // Given
        val validName = "Rick"
        val invalidPage = 0

        // When & Then
        try {
            useCase.invoke(validName, invalidPage)
            fail("Should have thrown InvalidCharacterPage exception")
        } catch (e: CharacterException.InvalidCharacterPage) {
            assertEquals(invalidPage, e.page)
        }
    }

    @Test
    fun `invoke with negative page should throw InvalidCharacterPage exception`() = runTest {
        // Given
        val validName = "Summer"
        val negativePage = -1

        // When & Then
        try {
            useCase.invoke(validName, negativePage)
            fail("Should have thrown InvalidCharacterPage exception")
        } catch (e: CharacterException.InvalidCharacterPage) {
            assertEquals(negativePage, e.page)
        }
    }

    @Test
    fun `invoke should propagate CharactersNotFoundByName exception from repository`() = runTest {
        // Given
        val searchName = "NonExistent"
        val error = CharacterException.CharactersNotFoundByName(searchName)
        mockRepository.charactersToReturn = Result.failure(error)

        // When
        val result = useCase.invoke(searchName)

        // Then
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull() as? CharacterException.CharactersNotFoundByName
        assertEquals(searchName, exception?.searchName)
    }

    @Test
    fun `invoke should propagate repository errors`() = runTest {
        // Given
        val error = CharacterException.CharacterRepositoryUnavailable()
        mockRepository.charactersToReturn = Result.failure(error)

        // When
        val result = useCase.invoke("Rick", 1)

        // Then
        assertTrue(result.isFailure)
        assertEquals(error, result.exceptionOrNull())
    }

    @Test
    fun `invoke with different search queries should work correctly`() = runTest {
        // Given
        val queries = listOf("Rick", "Morty", "Summer", "Beth")
        mockRepository.charactersToReturn = Result.success(DataResult(emptyList(), DataSource.API))

        // When
        queries.forEach { query ->
            useCase.invoke(query)
        }

        // Then
        assertEquals("Beth", mockRepository.lastNameSearched) // Last query searched
    }

    @Test
    fun `invoke with pagination should request correct pages`() = runTest {
        // Given
        val searchName = "Rick"
        val pages = listOf(1, 2, 3)
        mockRepository.charactersToReturn = Result.success(DataResult(emptyList(), DataSource.API))

        // When
        pages.forEach { page ->
            useCase.invoke(searchName, page)
        }

        // Then
        assertEquals(3, mockRepository.lastPageRequested) // Last page requested
    }

    // Fake repository for testing
    private class FakeCharacterRepository : CharacterRepository {
        var charactersToReturn: Result<DataResult<List<Character>>> = Result.success(DataResult(emptyList(), DataSource.API))
        var lastNameSearched: String = ""
        var lastPageRequested: Int = 0

        override suspend fun getCharacters(page: Int): Result<DataResult<List<Character>>> {
            throw NotImplementedError("Not used in these tests")
        }

        override suspend fun getCharacter(id: Int): Result<DataResult<Character>> {
            throw NotImplementedError("Not used in these tests")
        }

        override suspend fun getCharactersByName(name: String, page: Int): Result<DataResult<List<Character>>> {
            lastNameSearched = name
            lastPageRequested = page
            return charactersToReturn
        }
    }
}