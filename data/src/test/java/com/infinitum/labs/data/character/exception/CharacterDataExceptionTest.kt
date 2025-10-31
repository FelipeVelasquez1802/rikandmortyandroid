package com.infinitum.labs.data.character.exception

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CharacterDataExceptionTest {

    @Test
    fun `given no parameters when creating CharacterApiNetworkError then has default message about character API`() {
        val exception = CharacterDataException.CharacterApiNetworkError()

        assertEquals("Cannot connect to character API", exception.message)
        assertTrue(exception.message!!.contains("character API"))
    }

    @Test
    fun `given custom message and cause when creating CharacterApiNetworkError then preserves both`() {
        val customMessage = "Character API connection timeout"
        val cause = RuntimeException("Network timeout")

        val exception = CharacterDataException.CharacterApiNetworkError(
            message = customMessage,
            cause = cause
        )

        assertEquals(customMessage, exception.message)
        assertEquals(cause, exception.cause)
    }

    @Test
    fun `given status code when creating CharacterApiServerError then includes code in message`() {
        val statusCode = 500

        val exception = CharacterDataException.CharacterApiServerError(statusCode = statusCode)

        assertTrue(exception.message!!.contains(statusCode.toString()))
        assertTrue(exception.message!!.contains("character API", ignoreCase = true))
        assertEquals(statusCode, exception.statusCode)
    }

    @Test
    fun `given different status codes when creating CharacterApiServerError then creates appropriate exceptions`() {
        val exception503 = CharacterDataException.CharacterApiServerError(
            statusCode = 503,
            message = "Character API service unavailable"
        )
        val exception500 = CharacterDataException.CharacterApiServerError(
            statusCode = 500,
            message = "Character API internal server error"
        )

        assertEquals("Character API service unavailable", exception503.message)
        assertEquals(503, exception503.statusCode)
        assertEquals("Character API internal server error", exception500.message)
        assertEquals(500, exception500.statusCode)
    }

    @Test
    fun `given status code when creating CharacterApiClientError then includes code in message`() {
        val statusCode = 400

        val exception = CharacterDataException.CharacterApiClientError(statusCode = statusCode)

        assertTrue(exception.message!!.contains(statusCode.toString()))
        assertTrue(exception.message!!.contains("character API", ignoreCase = true))
        assertEquals(statusCode, exception.statusCode)
    }

    @Test
    fun `given different 4xx codes when creating CharacterApiClientError then stores status code correctly`() {
        val exception400 = CharacterDataException.CharacterApiClientError(statusCode = 400)
        val exception401 = CharacterDataException.CharacterApiClientError(statusCode = 401)
        val exception403 = CharacterDataException.CharacterApiClientError(statusCode = 403)

        assertEquals(400, exception400.statusCode)
        assertEquals(401, exception401.statusCode)
        assertEquals(403, exception403.statusCode)
    }

    @Test
    fun `given character ID when creating CharacterNotFoundInApi then includes ID in message`() {
        val characterId = 999

        val exception = CharacterDataException.CharacterNotFoundInApi(characterId = characterId)

        assertEquals("Character with ID $characterId not found in API", exception.message)
        assertEquals(characterId, exception.characterId)
        assertTrue(exception.message!!.contains("API"))
    }

    @Test
    fun `given different character IDs when creating CharacterNotFoundInApi then mentions character and API`() {
        val exception1 = CharacterDataException.CharacterNotFoundInApi(characterId = 1)
        val exception2 = CharacterDataException.CharacterNotFoundInApi(characterId = 500)

        assertTrue(exception1.message!!.contains("Character"))
        assertTrue(exception1.message!!.contains("API"))
        assertTrue(exception2.message!!.contains("Character"))
        assertTrue(exception2.message!!.contains("API"))
    }

    @Test
    fun `given search name when creating CharactersNotFoundInApiByName then includes name in message`() {
        val searchName = "Rick"

        val exception = CharacterDataException.CharactersNotFoundInApiByName(searchName = searchName)

        assertEquals("No characters found in API for name '$searchName'", exception.message)
        assertEquals(searchName, exception.searchName)
        assertTrue(exception.message!!.contains("API"))
    }

    @Test
    fun `given different search queries when creating CharactersNotFoundInApiByName then includes query in message`() {
        val exception1 = CharacterDataException.CharactersNotFoundInApiByName("Morty")
        val exception2 = CharacterDataException.CharactersNotFoundInApiByName("Summer")

        assertTrue(exception1.message!!.contains("Morty"))
        assertTrue(exception2.message!!.contains("Summer"))
        assertTrue(exception1.message!!.contains("characters"))
        assertTrue(exception2.message!!.contains("characters"))
    }

    @Test
    fun `given no parameters when creating CharacterDataParseError then has default message`() {
        val exception = CharacterDataException.CharacterDataParseError()

        assertEquals("Failed to parse character data from API", exception.message)
        assertTrue(exception.message!!.contains("character data"))
    }

    @Test
    fun `given custom message and cause when creating CharacterDataParseError then preserves both`() {
        val customMessage = "Invalid JSON format for character response"
        val cause = RuntimeException("JSON parsing failed")

        val exception = CharacterDataException.CharacterDataParseError(
            message = customMessage,
            cause = cause
        )

        assertEquals(customMessage, exception.message)
        assertEquals(cause, exception.cause)
    }

    @Test
    fun `given no parameters when creating CharacterApiUnexpectedError then has default message`() {
        val exception = CharacterDataException.CharacterApiUnexpectedError()

        assertEquals("Unexpected error when accessing character API", exception.message)
        assertTrue(exception.message!!.contains("character API", ignoreCase = true))
    }

    @Test
    fun `given custom message and cause when creating CharacterApiUnexpectedError then preserves both`() {
        val customMessage = "Unknown error in character API response"
        val cause = RuntimeException("Unexpected state")

        val exception = CharacterDataException.CharacterApiUnexpectedError(
            message = customMessage,
            cause = cause
        )

        assertEquals(customMessage, exception.message)
        assertEquals(cause, exception.cause)
    }

    @Test
    fun `given all CharacterDataExceptions when checking throwability then all are throwable with messages`() {
        val exceptions = listOf(
            CharacterDataException.CharacterApiNetworkError(),
            CharacterDataException.CharacterApiServerError(500),
            CharacterDataException.CharacterApiClientError(400),
            CharacterDataException.CharacterNotFoundInApi(1),
            CharacterDataException.CharactersNotFoundInApiByName("Rick"),
            CharacterDataException.CharacterDataParseError(),
            CharacterDataException.CharacterApiUnexpectedError()
        )

        exceptions.forEach { exception ->
            assertTrue(exception is Throwable)
            assertTrue(exception is CharacterDataException)
            assertNotNull(exception.message)
            assertTrue(exception.message!!.isNotBlank())
        }
    }

    @Test
    fun `given CharacterDataException sealed class when using exhaustive when then covers all exception types`() {
        val networkError = CharacterDataException.CharacterApiNetworkError()
        val serverError = CharacterDataException.CharacterApiServerError(500)
        val clientError = CharacterDataException.CharacterApiClientError(400)
        val notFoundInApi = CharacterDataException.CharacterNotFoundInApi(1)
        val notFoundByName = CharacterDataException.CharactersNotFoundInApiByName("Rick")
        val parseError = CharacterDataException.CharacterDataParseError()
        val unexpectedError = CharacterDataException.CharacterApiUnexpectedError()

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
    fun `given data exceptions when checking message content then all mention character`() {
        val exceptions = listOf(
            CharacterDataException.CharacterApiNetworkError(),
            CharacterDataException.CharacterNotFoundInApi(1),
            CharacterDataException.CharactersNotFoundInApiByName("Rick"),
            CharacterDataException.CharacterDataParseError()
        )

        exceptions.forEach { exception ->
            val message = exception.message?.lowercase() ?: ""
            assertTrue(
                "Exception message should mention 'character': ${exception.message}",
                message.contains("character")
            )
        }
    }

    @Test
    fun `given infrastructure exceptions when checking type then are CharacterDataException not CharacterException`() {
        val networkError = CharacterDataException.CharacterApiNetworkError()
        val serverError = CharacterDataException.CharacterApiServerError(503)
        val parseError = CharacterDataException.CharacterDataParseError()

        assertTrue(networkError is CharacterDataException)
        assertTrue(serverError is CharacterDataException)
        assertTrue(parseError is CharacterDataException)
        assertTrue(networkError.message!!.contains("API"))
        assertTrue(serverError.message!!.contains("API"))
        assertTrue(parseError.message!!.contains("parse", ignoreCase = true))
    }
}