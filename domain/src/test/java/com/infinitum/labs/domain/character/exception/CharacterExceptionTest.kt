package com.infinitum.labs.domain.character.exception

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CharacterExceptionTest {

    @Test
    fun `given character ID when creating CharacterNotFound exception then includes ID in message`() {
        val characterId = 123

        val exception = CharacterException.CharacterNotFound(characterId = characterId)

        assertEquals("Character with ID $characterId does not exist", exception.message)
        assertEquals(characterId, exception.characterId)
    }

    @Test
    fun `given different character IDs when creating CharacterNotFound exceptions then messages contain specific IDs`() {
        val exception1 = CharacterException.CharacterNotFound(characterId = 1)
        val exception2 = CharacterException.CharacterNotFound(characterId = 999)

        assertTrue(exception1.message!!.contains("1"))
        assertTrue(exception2.message!!.contains("999"))
    }

    @Test
    fun `given search name when creating CharactersNotFoundByName exception then includes name in message`() {
        val searchName = "Rick"

        val exception = CharacterException.CharactersNotFoundByName(searchName = searchName)

        assertEquals("No characters found matching name '$searchName'", exception.message)
        assertEquals(searchName, exception.searchName)
    }

    @Test
    fun `given different search names when creating CharactersNotFoundByName exceptions then messages contain specific names`() {
        val exception1 = CharacterException.CharactersNotFoundByName("Morty")
        val exception2 = CharacterException.CharactersNotFoundByName("Summer")

        assertTrue(exception1.message!!.contains("Morty"))
        assertTrue(exception2.message!!.contains("Summer"))
    }

    @Test
    fun `given invalid page number when creating InvalidCharacterPage exception then includes page in message`() {
        val invalidPage = 0

        val exception = CharacterException.InvalidCharacterPage(page = invalidPage)

        assertTrue(exception.message!!.contains(invalidPage.toString()))
        assertEquals(invalidPage, exception.page)
    }

    @Test
    fun `given negative or zero page when creating InvalidCharacterPage exception then communicates business rule violation`() {
        val negativePageException = CharacterException.InvalidCharacterPage(page = -1)
        val zeroPageException = CharacterException.InvalidCharacterPage(page = 0)

        assertTrue(negativePageException.message!!.contains("greater than 0"))
        assertTrue(zeroPageException.message!!.contains("greater than 0"))
    }

    @Test
    fun `given invalid character ID when creating InvalidCharacterId exception then includes ID in message`() {
        val invalidId = -5

        val exception = CharacterException.InvalidCharacterId(characterId = invalidId)

        assertTrue(exception.message!!.contains(invalidId.toString()))
        assertEquals(invalidId, exception.characterId)
    }

    @Test
    fun `given zero ID when creating InvalidCharacterId exception then communicates business rule violation`() {
        val exception = CharacterException.InvalidCharacterId(characterId = 0)

        assertTrue(exception.message!!.contains("greater than 0"))
    }

    @Test
    fun `given blank query when creating InvalidCharacterSearchQuery exception then indicates query cannot be blank`() {
        val blankQuery = "   "

        val exception = CharacterException.InvalidCharacterSearchQuery(query = blankQuery)

        assertTrue(exception.message!!.contains("query cannot be blank"))
        assertEquals(blankQuery, exception.query)
    }

    @Test
    fun `given empty query when creating InvalidCharacterSearchQuery exception then communicates business rule`() {
        val exception = CharacterException.InvalidCharacterSearchQuery("")

        assertTrue(exception.message!!.contains("blank"))
    }

    @Test
    fun `given no parameters when creating CharacterRepositoryUnavailable exception then has default catalog message`() {
        val exception = CharacterException.CharacterRepositoryUnavailable()

        assertEquals("The character catalog is temporarily unavailable", exception.message)
        assertTrue(exception.message!!.contains("catalog"))
    }

    @Test
    fun `given custom message and cause when creating CharacterRepositoryUnavailable exception then preserves both`() {
        val customMessage = "Character API is down for maintenance"
        val cause = RuntimeException("Network timeout")

        val exception = CharacterException.CharacterRepositoryUnavailable(
            message = customMessage,
            cause = cause
        )

        assertEquals(customMessage, exception.message)
        assertEquals(cause, exception.cause)
    }

    @Test
    fun `given network error when creating CharacterRepositoryUnavailable exception then preserves infrastructure error as cause`() {
        val networkError = RuntimeException("Connection refused")

        val exception = CharacterException.CharacterRepositoryUnavailable(cause = networkError)

        assertEquals(networkError, exception.cause)
    }

    @Test
    fun `given no parameters when creating InvalidCharacterData exception then has default corruption message`() {
        val exception = CharacterException.InvalidCharacterData()

        assertEquals("Character data is invalid or corrupted", exception.message)
        assertTrue(exception.message!!.contains("corrupted") || exception.message!!.contains("invalid"))
    }

    @Test
    fun `given custom message and cause when creating InvalidCharacterData exception then preserves both`() {
        val customMessage = "Character JSON format is invalid"
        val cause = RuntimeException("Parse error")

        val exception = CharacterException.InvalidCharacterData(
            message = customMessage,
            cause = cause
        )

        assertEquals(customMessage, exception.message)
        assertEquals(cause, exception.cause)
    }

    @Test
    fun `given all character exceptions when verifying throwability then all are throwable and have messages`() {
        val exceptions = listOf(
            CharacterException.CharacterNotFound(characterId = 1),
            CharacterException.CharactersNotFoundByName(searchName = "Rick"),
            CharacterException.InvalidCharacterPage(page = 0),
            CharacterException.InvalidCharacterId(characterId = -1),
            CharacterException.InvalidCharacterSearchQuery(query = ""),
            CharacterException.CharacterRepositoryUnavailable(),
            CharacterException.InvalidCharacterData()
        )

        exceptions.forEach { exception ->
            assertTrue(exception is Throwable)
            assertNotNull(exception.message)
            assertTrue(exception.message!!.isNotBlank())
        }
    }

    @Test
    fun `given CharacterException sealed class when using exhaustive when statement then covers all exception types`() {
        val notFound = CharacterException.CharacterNotFound(1)
        val notFoundByName = CharacterException.CharactersNotFoundByName("Rick")
        val invalidPage = CharacterException.InvalidCharacterPage(0)
        val invalidId = CharacterException.InvalidCharacterId(-1)
        val invalidQuery = CharacterException.InvalidCharacterSearchQuery("")
        val repoUnavailable = CharacterException.CharacterRepositoryUnavailable()
        val invalidData = CharacterException.InvalidCharacterData()
        val invalidName = CharacterException.InvalidCharacterName("")
        val invalidSpecies = CharacterException.InvalidCharacterSpecies("")
        val invalidImageUrl = CharacterException.InvalidCharacterImageUrl("")
        val invalidUrl = CharacterException.InvalidCharacterUrl("")
        val invalidEpisodes = CharacterException.InvalidCharacterEpisodes(0)
        val invalidCreatedDate = CharacterException.InvalidCharacterCreatedDate("")
        val invalidLocation = CharacterException.InvalidCharacterLocation("origin", "test")

        val results = listOf(
            notFound, notFoundByName, invalidPage, invalidId, invalidQuery,
            repoUnavailable, invalidData, invalidName, invalidSpecies,
            invalidImageUrl, invalidUrl, invalidEpisodes, invalidCreatedDate, invalidLocation
        ).map { exception ->
            when (exception) {
                is CharacterException.CharacterNotFound -> "character_not_found"
                is CharacterException.CharactersNotFoundByName -> "characters_not_found_by_name"
                is CharacterException.InvalidCharacterPage -> "invalid_page"
                is CharacterException.InvalidCharacterId -> "invalid_id"
                is CharacterException.InvalidCharacterSearchQuery -> "invalid_query"
                is CharacterException.CharacterRepositoryUnavailable -> "repo_unavailable"
                is CharacterException.InvalidCharacterData -> "invalid_data"
                is CharacterException.InvalidCharacterName -> "invalid_name"
                is CharacterException.InvalidCharacterSpecies -> "invalid_species"
                is CharacterException.InvalidCharacterImageUrl -> "invalid_image_url"
                is CharacterException.InvalidCharacterUrl -> "invalid_url"
                is CharacterException.InvalidCharacterEpisodes -> "invalid_episodes"
                is CharacterException.InvalidCharacterCreatedDate -> "invalid_created_date"
                is CharacterException.InvalidCharacterLocation -> "invalid_location"
            }
        }

        assertEquals(14, results.size)
        assertEquals("character_not_found", results[0])
        assertEquals("characters_not_found_by_name", results[1])
        assertEquals("invalid_page", results[2])
        assertEquals("invalid_id", results[3])
        assertEquals("invalid_query", results[4])
        assertEquals("repo_unavailable", results[5])
        assertEquals("invalid_data", results[6])
        assertEquals("invalid_name", results[7])
        assertEquals("invalid_species", results[8])
        assertEquals("invalid_image_url", results[9])
        assertEquals("invalid_url", results[10])
        assertEquals("invalid_episodes", results[11])
        assertEquals("invalid_created_date", results[12])
        assertEquals("invalid_location", results[13])
    }

    @Test
    fun `given domain exceptions when checking message content then all use Character-focused language`() {
        val exceptions = listOf(
            CharacterException.CharacterNotFound(1),
            CharacterException.CharactersNotFoundByName("Rick"),
            CharacterException.CharacterRepositoryUnavailable(),
            CharacterException.InvalidCharacterData()
        )

        exceptions.forEach { exception ->
            val message = exception.message?.lowercase() ?: ""
            assertTrue(
                "Exception message should mention 'character': ${exception.message}",
                message.contains("character")
            )
        }
    }
}