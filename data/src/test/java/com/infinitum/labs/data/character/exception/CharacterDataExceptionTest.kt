package com.infinitum.labs.data.character.exception

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Tests for Character data layer exceptions.
 * These tests validate infrastructure-level errors when accessing character data.
 */
class CharacterDataExceptionTest {

    // ==================== CharacterApiNetworkError Tests ====================

    @Test
    fun `CharacterApiNetworkError should have default message about character API`() {
        // When
        val exception = CharacterDataException.CharacterApiNetworkError()

        // Then
        assertEquals("Cannot connect to character API", exception.message)
        assertTrue(exception.message!!.contains("character API"))
    }

    @Test
    fun `CharacterApiNetworkError should accept custom message and cause`() {
        // Given
        val customMessage = "Character API connection timeout"
        val cause = RuntimeException("Network timeout")

        // When
        val exception = CharacterDataException.CharacterApiNetworkError(
            message = customMessage,
            cause = cause
        )

        // Then
        assertEquals(customMessage, exception.message)
        assertEquals(cause, exception.cause)
    }

    // ==================== CharacterApiServerError Tests ====================

    @Test
    fun `CharacterApiServerError should include status code in message`() {
        // Given
        val statusCode = 500

        // When
        val exception = CharacterDataException.CharacterApiServerError(statusCode = statusCode)

        // Then
        assertTrue(exception.message!!.contains(statusCode.toString()))
        assertTrue(exception.message!!.contains("character API", ignoreCase = true))
        assertEquals(statusCode, exception.statusCode)
    }

    @Test
    fun `CharacterApiServerError should accept custom message with different status codes`() {
        // Given & When
        val exception503 = CharacterDataException.CharacterApiServerError(
            statusCode = 503,
            message = "Character API service unavailable"
        )
        val exception500 = CharacterDataException.CharacterApiServerError(
            statusCode = 500,
            message = "Character API internal server error"
        )

        // Then
        assertEquals("Character API service unavailable", exception503.message)
        assertEquals(503, exception503.statusCode)
        assertEquals("Character API internal server error", exception500.message)
        assertEquals(500, exception500.statusCode)
    }

    // ==================== CharacterApiClientError Tests ====================

    @Test
    fun `CharacterApiClientError should include status code in message`() {
        // Given
        val statusCode = 400

        // When
        val exception = CharacterDataException.CharacterApiClientError(statusCode = statusCode)

        // Then
        assertTrue(exception.message!!.contains(statusCode.toString()))
        assertTrue(exception.message!!.contains("character API", ignoreCase = true))
        assertEquals(statusCode, exception.statusCode)
    }

    @Test
    fun `CharacterApiClientError should handle different 4xx codes`() {
        // Given & When
        val exception400 = CharacterDataException.CharacterApiClientError(statusCode = 400)
        val exception401 = CharacterDataException.CharacterApiClientError(statusCode = 401)
        val exception403 = CharacterDataException.CharacterApiClientError(statusCode = 403)

        // Then
        assertEquals(400, exception400.statusCode)
        assertEquals(401, exception401.statusCode)
        assertEquals(403, exception403.statusCode)
    }

    // ==================== CharacterNotFoundInApi Tests ====================

    @Test
    fun `CharacterNotFoundInApi should include character ID in message`() {
        // Given
        val characterId = 999

        // When
        val exception = CharacterDataException.CharacterNotFoundInApi(characterId = characterId)

        // Then
        assertEquals("Character with ID $characterId not found in API", exception.message)
        assertEquals(characterId, exception.characterId)
        assertTrue(exception.message!!.contains("API"))
    }

    @Test
    fun `CharacterNotFoundInApi should be specific to character context`() {
        // Given & When
        val exception1 = CharacterDataException.CharacterNotFoundInApi(characterId = 1)
        val exception2 = CharacterDataException.CharacterNotFoundInApi(characterId = 500)

        // Then - Both should mention "character" and "API"
        assertTrue(exception1.message!!.contains("Character"))
        assertTrue(exception1.message!!.contains("API"))
        assertTrue(exception2.message!!.contains("Character"))
        assertTrue(exception2.message!!.contains("API"))
    }

    // ==================== CharactersNotFoundInApiByName Tests ====================

    @Test
    fun `CharactersNotFoundInApiByName should include search name in message`() {
        // Given
        val searchName = "Rick"

        // When
        val exception = CharacterDataException.CharactersNotFoundInApiByName(searchName = searchName)

        // Then
        assertEquals("No characters found in API for name '$searchName'", exception.message)
        assertEquals(searchName, exception.searchName)
        assertTrue(exception.message!!.contains("API"))
    }

    @Test
    fun `CharactersNotFoundInApiByName should handle different search queries`() {
        // Given & When
        val exception1 = CharacterDataException.CharactersNotFoundInApiByName("Morty")
        val exception2 = CharacterDataException.CharactersNotFoundInApiByName("Summer")

        // Then
        assertTrue(exception1.message!!.contains("Morty"))
        assertTrue(exception2.message!!.contains("Summer"))
        assertTrue(exception1.message!!.contains("characters"))
        assertTrue(exception2.message!!.contains("characters"))
    }

    // ==================== CharacterDataParseError Tests ====================

    @Test
    fun `CharacterDataParseError should have default message about character data`() {
        // When
        val exception = CharacterDataException.CharacterDataParseError()

        // Then
        assertEquals("Failed to parse character data from API", exception.message)
        assertTrue(exception.message!!.contains("character data"))
    }

    @Test
    fun `CharacterDataParseError should accept custom message and cause`() {
        // Given
        val customMessage = "Invalid JSON format for character response"
        val cause = RuntimeException("JSON parsing failed")

        // When
        val exception = CharacterDataException.CharacterDataParseError(
            message = customMessage,
            cause = cause
        )

        // Then
        assertEquals(customMessage, exception.message)
        assertEquals(cause, exception.cause)
    }

    // ==================== CharacterApiUnexpectedError Tests ====================

    @Test
    fun `CharacterApiUnexpectedError should have default message about character API`() {
        // When
        val exception = CharacterDataException.CharacterApiUnexpectedError()

        // Then
        assertEquals("Unexpected error when accessing character API", exception.message)
        assertTrue(exception.message!!.contains("character API", ignoreCase = true))
    }

    @Test
    fun `CharacterApiUnexpectedError should accept custom message and cause`() {
        // Given
        val customMessage = "Unknown error in character API response"
        val cause = RuntimeException("Unexpected state")

        // When
        val exception = CharacterDataException.CharacterApiUnexpectedError(
            message = customMessage,
            cause = cause
        )

        // Then
        assertEquals(customMessage, exception.message)
        assertEquals(cause, exception.cause)
    }

    // ==================== General Data Exception Tests ====================

    @Test
    fun `all CharacterDataExceptions should be throwable and have messages`() {
        // Given - All character data layer exceptions
        val exceptions = listOf(
            CharacterDataException.CharacterApiNetworkError(),
            CharacterDataException.CharacterApiServerError(500),
            CharacterDataException.CharacterApiClientError(400),
            CharacterDataException.CharacterNotFoundInApi(1),
            CharacterDataException.CharactersNotFoundInApiByName("Rick"),
            CharacterDataException.CharacterDataParseError(),
            CharacterDataException.CharacterApiUnexpectedError()
        )

        // Then
        exceptions.forEach { exception ->
            assertTrue(exception is Throwable)
            assertTrue(exception is CharacterDataException)
            assertNotNull(exception.message)
            assertTrue(exception.message!!.isNotBlank())
        }
    }

    @Test
    fun `CharacterDataException sealed class should enable exhaustive when statements`() {
        // Given
        val networkError = CharacterDataException.CharacterApiNetworkError()
        val serverError = CharacterDataException.CharacterApiServerError(500)
        val clientError = CharacterDataException.CharacterApiClientError(400)
        val notFoundInApi = CharacterDataException.CharacterNotFoundInApi(1)
        val notFoundByName = CharacterDataException.CharactersNotFoundInApiByName("Rick")
        val parseError = CharacterDataException.CharacterDataParseError()
        val unexpectedError = CharacterDataException.CharacterApiUnexpectedError()

        // When - Using exhaustive when
        val results = listOf(
            networkError, serverError, clientError, notFoundInApi,
            notFoundByName, parseError, unexpectedError
        ).map { exception ->
            when (exception) {
                is CharacterDataException.CharacterApiNetworkError -> "network"
                is CharacterDataException.CharacterApiServerError -> "server"
                is CharacterDataException.CharacterApiClientError -> "client"
                is CharacterDataException.CharacterNotFoundInApi -> "not_found_in_api"
                is CharacterDataException.CharactersNotFoundInApiByName -> "not_found_by_name"
                is CharacterDataException.CharacterDataParseError -> "parse"
                is CharacterDataException.CharacterApiUnexpectedError -> "unexpected"
            }
        }

        // Then
        assertEquals(7, results.size)
        assertEquals("network", results[0])
        assertEquals("server", results[1])
        assertEquals("client", results[2])
        assertEquals("not_found_in_api", results[3])
        assertEquals("not_found_by_name", results[4])
        assertEquals("parse", results[5])
        assertEquals("unexpected", results[6])
    }

    @Test
    fun `data exceptions should use Character and API focused language`() {
        // Given - Sample exceptions
        val exceptions = listOf(
            CharacterDataException.CharacterApiNetworkError(),
            CharacterDataException.CharacterNotFoundInApi(1),
            CharacterDataException.CharactersNotFoundInApiByName("Rick"),
            CharacterDataException.CharacterDataParseError()
        )

        // Then - All messages should mention "character" and most should mention "API"
        exceptions.forEach { exception ->
            val message = exception.message?.lowercase() ?: ""
            assertTrue(
                "Exception message should mention 'character': ${exception.message}",
                message.contains("character")
            )
        }
    }

    @Test
    fun `infrastructure exceptions should be in CharacterDataException not CharacterException`() {
        // Given - These are infrastructure concerns
        val networkError = CharacterDataException.CharacterApiNetworkError()
        val serverError = CharacterDataException.CharacterApiServerError(503)
        val parseError = CharacterDataException.CharacterDataParseError()

        // Then - They should be data layer exceptions
        assertTrue(networkError is CharacterDataException)
        assertTrue(serverError is CharacterDataException)
        assertTrue(parseError is CharacterDataException)

        // And messages should mention technical details (API, parsing)
        assertTrue(networkError.message!!.contains("API"))
        assertTrue(serverError.message!!.contains("API"))
        assertTrue(parseError.message!!.contains("parse", ignoreCase = true))
    }
}