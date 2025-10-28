package com.infinitum.labs.domain.character.model

import com.infinitum.labs.domain.character.exception.CharacterException
import com.infinitum.labs.domain.character.model.builder.CharacterBuilder
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test

/**
 * Tests for Character model validations.
 * These tests validate that the Character model enforces its business rules.
 */
class CharacterValidationTest {

    // ==================== Character ID Validation Tests ====================

    @Test
    fun `Character with valid ID should be created successfully`() {
        // Given
        val validId = 1

        // When
        val character = CharacterBuilder.rickSanchez().withId(validId).build()

        // Then
        assertEquals(validId, character.id)
    }

    @Test
    fun `Character with invalid ID should throw InvalidCharacterId exception`() {
        // Given
        val invalidId = 0

        // When & Then
        try {
            CharacterBuilder.rickSanchez().withId(invalidId).build()
            fail("Should have thrown InvalidCharacterId exception")
        } catch (e: CharacterException.InvalidCharacterId) {
            assertEquals(invalidId, e.characterId)
        }
    }

    @Test
    fun `Character with negative ID should throw InvalidCharacterId exception`() {
        // Given
        val negativeId = -5

        // When & Then
        try {
            CharacterBuilder.rickSanchez().withId(negativeId).build()
            fail("Should have thrown InvalidCharacterId exception")
        } catch (e: CharacterException.InvalidCharacterId) {
            assertEquals(negativeId, e.characterId)
        }
    }

    @Test
    fun `validateCharacterId companion function should validate ID`() {
        // Given
        val validId = 10

        // When & Then - Should not throw
        Character.validateCharacterId(validId)
    }

    @Test
    fun `validateCharacterId companion function should throw for invalid ID`() {
        // Given
        val invalidId = 0

        // When & Then
        try {
            Character.validateCharacterId(invalidId)
            fail("Should have thrown InvalidCharacterId exception")
        } catch (e: CharacterException.InvalidCharacterId) {
            assertEquals(invalidId, e.characterId)
        }
    }

    // ==================== Character Name Validation Tests ====================

    @Test
    fun `Character with valid name should be created successfully`() {
        // Given
        val validName = "Rick Sanchez"

        // When
        val character = CharacterBuilder.rickSanchez().withName(validName).build()

        // Then
        assertEquals(validName, character.name)
    }

    @Test
    fun `Character with blank name should throw InvalidCharacterData exception`() {
        // When & Then
        try {
            CharacterBuilder.rickSanchez().withName("   ").build()
            fail("Should have thrown InvalidCharacterData exception")
        } catch (e: CharacterException.InvalidCharacterData) {
            assert(e.message!!.contains("name"))
        }
    }

    @Test
    fun `Character with empty name should throw InvalidCharacterData exception`() {
        // When & Then
        try {
            CharacterBuilder.rickSanchez().withName("").build()
            fail("Should have thrown InvalidCharacterData exception")
        } catch (e: CharacterException.InvalidCharacterData) {
            assert(e.message!!.contains("name"))
        }
    }

    // ==================== Character Image Validation Tests ====================

    @Test
    fun `Character with valid image URL should be created successfully`() {
        // Given
        val validImageUrl = "https://rickandmortyapi.com/api/character/avatar/1.jpeg"

        // When
        val character = CharacterBuilder.rickSanchez().withImage(validImageUrl).build()

        // Then
        assertEquals(validImageUrl, character.image)
    }

    @Test
    fun `Character with blank image URL should throw InvalidCharacterData exception`() {
        // When & Then
        try {
            CharacterBuilder.rickSanchez().withImage("   ").build()
            fail("Should have thrown InvalidCharacterData exception")
        } catch (e: CharacterException.InvalidCharacterData) {
            assert(e.message!!.contains("image"))
        }
    }

    @Test
    fun `Character with empty image URL should throw InvalidCharacterData exception`() {
        // When & Then
        try {
            CharacterBuilder.rickSanchez().withImage("").build()
            fail("Should have thrown InvalidCharacterData exception")
        } catch (e: CharacterException.InvalidCharacterData) {
            assert(e.message!!.contains("image"))
        }
    }

    // ==================== Character URL Validation Tests ====================

    @Test
    fun `Character with valid URL should be created successfully`() {
        // Given
        val validUrl = "https://rickandmortyapi.com/api/character/1"

        // When
        val character = CharacterBuilder.rickSanchez().withUrl(validUrl).build()

        // Then
        assertEquals(validUrl, character.url)
    }

    @Test
    fun `Character with blank URL should throw InvalidCharacterData exception`() {
        // When & Then
        try {
            CharacterBuilder.rickSanchez().withUrl("   ").build()
            fail("Should have thrown InvalidCharacterData exception")
        } catch (e: CharacterException.InvalidCharacterData) {
            assert(e.message!!.contains("URL"))
        }
    }

    @Test
    fun `Character with empty URL should throw InvalidCharacterData exception`() {
        // When & Then
        try {
            CharacterBuilder.rickSanchez().withUrl("").build()
            fail("Should have thrown InvalidCharacterData exception")
        } catch (e: CharacterException.InvalidCharacterData) {
            assert(e.message!!.contains("URL"))
        }
    }

    // ==================== Character Episodes Validation Tests ====================

    @Test
    fun `Character with valid episodes should be created successfully`() {
        // Given
        val validEpisodes = listOf(
            "https://rickandmortyapi.com/api/episode/1",
            "https://rickandmortyapi.com/api/episode/2"
        )

        // When
        val character = CharacterBuilder.rickSanchez().withEpisode(validEpisodes).build()

        // Then
        assertEquals(validEpisodes, character.episode)
    }

    @Test
    fun `Character with empty episodes list should throw InvalidCharacterData exception`() {
        // When & Then
        try {
            CharacterBuilder.rickSanchez().withEpisode(emptyList()).build()
            fail("Should have thrown InvalidCharacterData exception")
        } catch (e: CharacterException.InvalidCharacterData) {
            assert(e.message!!.contains("episode"))
        }
    }

    // ==================== Search Query Validation Tests ====================

    @Test
    fun `validateSearchQuery should accept valid query`() {
        // Given
        val validQuery = "Rick"

        // When & Then - Should not throw
        Character.validateSearchQuery(validQuery)
    }

    @Test
    fun `validateSearchQuery should throw for blank query`() {
        // Given
        val blankQuery = "   "

        // When & Then
        try {
            Character.validateSearchQuery(blankQuery)
            fail("Should have thrown InvalidCharacterSearchQuery exception")
        } catch (e: CharacterException.InvalidCharacterSearchQuery) {
            assertEquals(blankQuery, e.query)
        }
    }

    @Test
    fun `validateSearchQuery should throw for empty query`() {
        // Given
        val emptyQuery = ""

        // When & Then
        try {
            Character.validateSearchQuery(emptyQuery)
            fail("Should have thrown InvalidCharacterSearchQuery exception")
        } catch (e: CharacterException.InvalidCharacterSearchQuery) {
            assertEquals(emptyQuery, e.query)
        }
    }

    // ==================== Page Validation Tests ====================

    @Test
    fun `validatePage should accept valid page numbers`() {
        // Given
        val validPages = listOf(1, 2, 10, 100)

        // When & Then - Should not throw
        validPages.forEach { page ->
            Character.validatePage(page)
        }
    }

    @Test
    fun `validatePage should throw for page number zero`() {
        // Given
        val invalidPage = 0

        // When & Then
        try {
            Character.validatePage(invalidPage)
            fail("Should have thrown InvalidCharacterPage exception")
        } catch (e: CharacterException.InvalidCharacterPage) {
            assertEquals(invalidPage, e.page)
        }
    }

    @Test
    fun `validatePage should throw for negative page number`() {
        // Given
        val negativePage = -1

        // When & Then
        try {
            Character.validatePage(negativePage)
            fail("Should have thrown InvalidCharacterPage exception")
        } catch (e: CharacterException.InvalidCharacterPage) {
            assertEquals(negativePage, e.page)
        }
    }

    // ==================== Complete Validation Tests ====================

    @Test
    fun `validate method should check all fields`() {
        // Given
        val validCharacter = CharacterBuilder.rickSanchez().build()

        // When & Then - Should not throw
        validCharacter.validate()
    }

    @Test
    fun `Character with all valid fields should pass validation`() {
        // Given & When
        val character = Character(
            id = 1,
            name = "Rick Sanchez",
            status = CharacterStatus.ALIVE,
            species = "Human",
            type = "",
            gender = CharacterGender.MALE,
            origin = CharacterLocation(name = "Earth", url = "https://rickandmortyapi.com/api/location/1"),
            location = CharacterLocation(name = "Earth", url = "https://rickandmortyapi.com/api/location/20"),
            image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
            episode = listOf("https://rickandmortyapi.com/api/episode/1"),
            url = "https://rickandmortyapi.com/api/character/1",
            created = "2017-11-04T18:48:46.250Z"
        )

        // Then
        assertEquals(1, character.id)
        assertEquals("Rick Sanchez", character.name)
    }
}