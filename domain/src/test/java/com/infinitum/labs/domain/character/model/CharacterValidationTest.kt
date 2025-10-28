package com.infinitum.labs.domain.character.model

import com.infinitum.labs.domain.character.exception.CharacterException
import com.infinitum.labs.domain.character.model.builder.CharacterBuilder
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test

class CharacterValidationTest {

    @Test
    fun `given valid ID when creating character then returns character successfully`() {
        val validId = 1

        val character = CharacterBuilder.rickSanchez().withId(validId).build()

        assertEquals(validId, character.id)
    }

    @Test
    fun `given zero ID when creating character then throws InvalidCharacterId`() {
        val invalidId = 0

        try {
            CharacterBuilder.rickSanchez().withId(invalidId).build()
            fail("Should have thrown InvalidCharacterId exception")
        } catch (e: CharacterException.InvalidCharacterId) {
            assertEquals(invalidId, e.characterId)
        }
    }

    @Test
    fun `given negative ID when creating character then throws InvalidCharacterId`() {
        val negativeId = -5

        try {
            CharacterBuilder.rickSanchez().withId(negativeId).build()
            fail("Should have thrown InvalidCharacterId exception")
        } catch (e: CharacterException.InvalidCharacterId) {
            assertEquals(negativeId, e.characterId)
        }
    }

    @Test
    fun `given valid name when creating character then returns character successfully`() {
        val validName = "Rick Sanchez"

        val character = CharacterBuilder.rickSanchez().withName(validName).build()

        assertEquals(validName, character.name)
    }

    @Test
    fun `given blank name when creating character then throws InvalidCharacterName`() {
        try {
            CharacterBuilder.rickSanchez().withName("   ").build()
            fail("Should have thrown InvalidCharacterName exception")
        } catch (e: CharacterException.InvalidCharacterName) {
            assertEquals("   ", e.name)
        }
    }

    @Test
    fun `given empty name when creating character then throws InvalidCharacterName`() {
        try {
            CharacterBuilder.rickSanchez().withName("").build()
            fail("Should have thrown InvalidCharacterName exception")
        } catch (e: CharacterException.InvalidCharacterName) {
            assertEquals("", e.name)
        }
    }

    @Test
    fun `given valid image URL when creating character then returns character successfully`() {
        val validImageUrl = "https://rickandmortyapi.com/api/character/avatar/1.jpeg"

        val character = CharacterBuilder.rickSanchez().withImage(validImageUrl).build()

        assertEquals(validImageUrl, character.image)
    }

    @Test
    fun `given blank image URL when creating character then throws InvalidCharacterImageUrl`() {
        try {
            CharacterBuilder.rickSanchez().withImage("   ").build()
            fail("Should have thrown InvalidCharacterImageUrl exception")
        } catch (e: CharacterException.InvalidCharacterImageUrl) {
            assertEquals("   ", e.imageUrl)
        }
    }

    @Test
    fun `given empty image URL when creating character then throws InvalidCharacterImageUrl`() {
        try {
            CharacterBuilder.rickSanchez().withImage("").build()
            fail("Should have thrown InvalidCharacterImageUrl exception")
        } catch (e: CharacterException.InvalidCharacterImageUrl) {
            assertEquals("", e.imageUrl)
        }
    }

    @Test
    fun `given valid character URL when creating character then returns character successfully`() {
        val validUrl = "https://rickandmortyapi.com/api/character/1"

        val character = CharacterBuilder.rickSanchez().withUrl(validUrl).build()

        assertEquals(validUrl, character.url)
    }

    @Test
    fun `given blank URL when creating character then throws InvalidCharacterUrl`() {
        try {
            CharacterBuilder.rickSanchez().withUrl("   ").build()
            fail("Should have thrown InvalidCharacterUrl exception")
        } catch (e: CharacterException.InvalidCharacterUrl) {
            assertEquals("   ", e.url)
        }
    }

    @Test
    fun `given empty URL when creating character then throws InvalidCharacterUrl`() {
        try {
            CharacterBuilder.rickSanchez().withUrl("").build()
            fail("Should have thrown InvalidCharacterUrl exception")
        } catch (e: CharacterException.InvalidCharacterUrl) {
            assertEquals("", e.url)
        }
    }

    @Test
    fun `given valid episodes when creating character then returns character successfully`() {
        val validEpisodes = listOf(
            "https://rickandmortyapi.com/api/episode/1",
            "https://rickandmortyapi.com/api/episode/2"
        )

        val character = CharacterBuilder.rickSanchez().withEpisode(validEpisodes).build()

        assertEquals(validEpisodes, character.episode)
    }

    @Test
    fun `given empty episodes list when creating character then throws InvalidCharacterEpisodes`() {
        try {
            CharacterBuilder.rickSanchez().withEpisode(emptyList()).build()
            fail("Should have thrown InvalidCharacterEpisodes exception")
        } catch (e: CharacterException.InvalidCharacterEpisodes) {
            assertEquals(0, e.episodeCount)
        }
    }

    @Test
    fun `given all valid fields when creating character then returns character with correct values`() {
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

        assertEquals(1, character.id)
        assertEquals("Rick Sanchez", character.name)
    }
}