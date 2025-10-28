package com.infinitum.labs.domain.character.exception

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Tests for Character domain exceptions.
 * These tests validate that exceptions speak the ubiquitous language of the Character domain.
 */
class CharacterExceptionTest {

    // ==================== CharacterNotFound Tests ====================

    @Test
    fun `CharacterNotFound should include character ID in message`() {
        // Given
        val characterId = 123

        // When
        val exception = CharacterException.CharacterNotFound(characterId = characterId)

        // Then
        assertEquals("Character with ID $characterId does not exist", exception.message)
        assertEquals(characterId, exception.characterId)
        assertTrue(exception is CharacterException)
    }

    @Test
    fun `CharacterNotFound should be specific to character ID`() {
        // Given & When
        val exception1 = CharacterException.CharacterNotFound(characterId = 1)
        val exception2 = CharacterException.CharacterNotFound(characterId = 999)

        // Then
        assertTrue(exception1.message!!.contains("1"))
        assertTrue(exception2.message!!.contains("999"))
    }

    // ==================== CharactersNotFoundByName Tests ====================

    @Test
    fun `CharactersNotFoundByName should include search name in message`() {
        // Given
        val searchName = "Rick"

        // When
        val exception = CharacterException.CharactersNotFoundByName(searchName = searchName)

        // Then
        assertEquals("No characters found matching name '$searchName'", exception.message)
        assertEquals(searchName, exception.searchName)
    }

    @Test
    fun `CharactersNotFoundByName should handle different search queries`() {
        // Given & When
        val exception1 = CharacterException.CharactersNotFoundByName("Morty")
        val exception2 = CharacterException.CharactersNotFoundByName("Summer")

        // Then
        assertTrue(exception1.message!!.contains("Morty"))
        assertTrue(exception2.message!!.contains("Summer"))
    }

    // ==================== InvalidCharacterPage Tests ====================

    @Test
    fun `InvalidCharacterPage should include page number in message`() {
        // Given
        val invalidPage = 0

        // When
        val exception = CharacterException.InvalidCharacterPage(page = invalidPage)

        // Then
        assertTrue(exception.message!!.contains(invalidPage.toString()))
        assertEquals(invalidPage, exception.page)
    }

    @Test
    fun `InvalidCharacterPage should communicate business rule violation`() {
        // Given
        val negativePageException = CharacterException.InvalidCharacterPage(page = -1)
        val zeroPageException = CharacterException.InvalidCharacterPage(page = 0)

        // Then - Both should indicate that page must be greater than 0
        assertTrue(negativePageException.message!!.contains("greater than 0"))
        assertTrue(zeroPageException.message!!.contains("greater than 0"))
    }

    // ==================== InvalidCharacterId Tests ====================

    @Test
    fun `InvalidCharacterId should include character ID in message`() {
        // Given
        val invalidId = -5

        // When
        val exception = CharacterException.InvalidCharacterId(characterId = invalidId)

        // Then
        assertTrue(exception.message!!.contains(invalidId.toString()))
        assertEquals(invalidId, exception.characterId)
    }

    @Test
    fun `InvalidCharacterId should communicate business rule violation`() {
        // Given & When
        val exception = CharacterException.InvalidCharacterId(characterId = 0)

        // Then - Should indicate ID must be greater than 0
        assertTrue(exception.message!!.contains("greater than 0"))
    }

    // ==================== InvalidCharacterSearchQuery Tests ====================

    @Test
    fun `InvalidCharacterSearchQuery should include query in message`() {
        // Given
        val blankQuery = "   "

        // When
        val exception = CharacterException.InvalidCharacterSearchQuery(query = blankQuery)

        // Then
        assertTrue(exception.message!!.contains("query cannot be blank"))
        assertEquals(blankQuery, exception.query)
    }

    @Test
    fun `InvalidCharacterSearchQuery should communicate business rule`() {
        // Given & When
        val exception = CharacterException.InvalidCharacterSearchQuery("")

        // Then
        assertTrue(exception.message!!.contains("blank"))
    }

    // ==================== CharacterRepositoryUnavailable Tests ====================

    @Test
    fun `CharacterRepositoryUnavailable should have default catalog message`() {
        // When
        val exception = CharacterException.CharacterRepositoryUnavailable()

        // Then
        assertEquals("The character catalog is temporarily unavailable", exception.message)
        assertTrue(exception.message!!.contains("catalog"))
    }

    @Test
    fun `CharacterRepositoryUnavailable should accept custom message and cause`() {
        // Given
        val customMessage = "Character API is down for maintenance"
        val cause = RuntimeException("Network timeout")

        // When
        val exception = CharacterException.CharacterRepositoryUnavailable(
            message = customMessage,
            cause = cause
        )

        // Then
        assertEquals(customMessage, exception.message)
        assertEquals(cause, exception.cause)
    }

    @Test
    fun `CharacterRepositoryUnavailable should preserve infrastructure error as cause`() {
        // Given
        val networkError = RuntimeException("Connection refused")

        // When
        val exception = CharacterException.CharacterRepositoryUnavailable(cause = networkError)

        // Then
        assertEquals(networkError, exception.cause)
    }

    // ==================== InvalidCharacterData Tests ====================

    @Test
    fun `InvalidCharacterData should have default corruption message`() {
        // When
        val exception = CharacterException.InvalidCharacterData()

        // Then
        assertEquals("Character data is invalid or corrupted", exception.message)
        assertTrue(exception.message!!.contains("corrupted") || exception.message!!.contains("invalid"))
    }

    @Test
    fun `InvalidCharacterData should accept custom message and cause`() {
        // Given
        val customMessage = "Character JSON format is invalid"
        val cause = RuntimeException("Parse error")

        // When
        val exception = CharacterException.InvalidCharacterData(
            message = customMessage,
            cause = cause
        )

        // Then
        assertEquals(customMessage, exception.message)
        assertEquals(cause, exception.cause)
    }

    // ==================== General Domain Exception Tests ====================

    @Test
    fun `all CharacterExceptions should be throwable and have messages`() {
        // Given - All character domain exceptions
        val exceptions = listOf(
            CharacterException.CharacterNotFound(characterId = 1),
            CharacterException.CharactersNotFoundByName(searchName = "Rick"),
            CharacterException.InvalidCharacterPage(page = 0),
            CharacterException.InvalidCharacterId(characterId = -1),
            CharacterException.InvalidCharacterSearchQuery(query = ""),
            CharacterException.CharacterRepositoryUnavailable(),
            CharacterException.InvalidCharacterData()
        )

        // Then
        exceptions.forEach { exception ->
            assertTrue(exception is Throwable)
            assertTrue(exception is CharacterException)
            assertNotNull(exception.message)
            assertTrue(exception.message!!.isNotBlank())
        }
    }

    @Test
    fun `CharacterException sealed class should enable exhaustive when statements`() {
        // Given - Different character exceptions
        val notFound = CharacterException.CharacterNotFound(1)
        val notFoundByName = CharacterException.CharactersNotFoundByName("Rick")
        val invalidPage = CharacterException.InvalidCharacterPage(0)
        val invalidId = CharacterException.InvalidCharacterId(-1)
        val invalidQuery = CharacterException.InvalidCharacterSearchQuery("")
        val repoUnavailable = CharacterException.CharacterRepositoryUnavailable()
        val invalidData = CharacterException.InvalidCharacterData()

        // When - Using exhaustive when
        val results = listOf(
            notFound, notFoundByName, invalidPage, invalidId,
            invalidQuery, repoUnavailable, invalidData
        ).map { exception ->
            when (exception) {
                is CharacterException.CharacterNotFound -> "character_not_found"
                is CharacterException.CharactersNotFoundByName -> "characters_not_found_by_name"
                is CharacterException.InvalidCharacterPage -> "invalid_page"
                is CharacterException.InvalidCharacterId -> "invalid_id"
                is CharacterException.InvalidCharacterSearchQuery -> "invalid_query"
                is CharacterException.CharacterRepositoryUnavailable -> "repo_unavailable"
                is CharacterException.InvalidCharacterData -> "invalid_data"
            }
        }

        // Then
        assertEquals(7, results.size)
        assertEquals("character_not_found", results[0])
        assertEquals("characters_not_found_by_name", results[1])
        assertEquals("invalid_page", results[2])
        assertEquals("invalid_id", results[3])
        assertEquals("invalid_query", results[4])
        assertEquals("repo_unavailable", results[5])
        assertEquals("invalid_data", results[6])
    }

    @Test
    fun `domain exceptions should use Character-focused language`() {
        // Given - Sample exceptions
        val exceptions = listOf(
            CharacterException.CharacterNotFound(1),
            CharacterException.CharactersNotFoundByName("Rick"),
            CharacterException.CharacterRepositoryUnavailable(),
            CharacterException.InvalidCharacterData()
        )

        // Then - All messages should mention "character" or "characters"
        exceptions.forEach { exception ->
            val message = exception.message?.lowercase() ?: ""
            assertTrue(
                "Exception message should mention 'character': ${exception.message}",
                message.contains("character")
            )
        }
    }
}