package com.infinitum.labs.domain.character.model

import com.infinitum.labs.domain.character.model.builder.CharacterBuilder
import com.infinitum.labs.domain.character.model.builder.CharacterLocationBuilder
import org.junit.Assert.assertEquals
import org.junit.Test

class CharacterTest {

    @Test
    fun `given all valid properties when creating character then returns character with correct values`() {
        val origin = CharacterLocationBuilder.aCharacterLocation()
            .withName("Earth (C-137)")
            .withUrl("https://rickandmortyapi.com/api/location/1")
            .build()
        val location = CharacterLocationBuilder.aCharacterLocation()
            .withName("Citadel of Ricks")
            .withUrl("https://rickandmortyapi.com/api/location/3")
            .build()
        val episodes = listOf(
            "https://rickandmortyapi.com/api/episode/1",
            "https://rickandmortyapi.com/api/episode/2"
        )

        val character = CharacterBuilder.rickSanchez()
            .withOrigin(origin)
            .withLocation(location)
            .withEpisode(episodes)
            .build()

        assertEquals(1, character.id)
        assertEquals("Rick Sanchez", character.name)
        assertEquals(CharacterStatus.ALIVE, character.status)
        assertEquals("Human", character.species)
        assertEquals("", character.type)
        assertEquals(CharacterGender.MALE, character.gender)
        assertEquals(origin, character.origin)
        assertEquals(location, character.location)
        assertEquals("https://rickandmortyapi.com/api/character/avatar/1.jpeg", character.image)
        assertEquals(episodes, character.episode)
        assertEquals("https://rickandmortyapi.com/api/character/1", character.url)
        assertEquals("2017-11-04T18:48:46.250Z", character.created)
    }

    @Test
    fun `given DEAD status when creating character then returns character with DEAD status`() {
        val character = CharacterBuilder.mortySmith()
            .withStatus(CharacterStatus.DEAD)
            .build()

        assertEquals(CharacterStatus.DEAD, character.status)
        assertEquals("Morty Smith", character.name)
    }

    @Test
    fun `given UNKNOWN status when creating character then returns character with UNKNOWN status`() {
        val character = CharacterBuilder.summerSmith()
            .withStatus(CharacterStatus.UNKNOWN)
            .withOrigin(CharacterLocationBuilder.anUnknownLocation().build())
            .withLocation(CharacterLocationBuilder.anUnknownLocation().build())
            .build()

        assertEquals(CharacterStatus.UNKNOWN, character.status)
        assertEquals(CharacterGender.FEMALE, character.gender)
    }

    @Test
    fun `given character when copying with different name then preserves other properties`() {
        val character = CharacterBuilder.aCharacter()
            .withName("Rick")
            .withImage("https://example.com/image.jpg")
            .withUrl("https://example.com/character/1")
            .build()

        val copiedCharacter = character.copy(name = "Rick Sanchez")

        assertEquals("Rick Sanchez", copiedCharacter.name)
        assertEquals(character.id, copiedCharacter.id)
        assertEquals(character.status, copiedCharacter.status)
        assertEquals(character.species, copiedCharacter.species)
    }

    @Test
    fun `given two characters with same properties when comparing then are equal`() {
        val origin = CharacterLocationBuilder.aCharacterLocation()
            .withName("Earth")
            .withUrl("https://example.com/location/1")
            .build()

        val character1 = CharacterBuilder.aCharacter()
            .withName("Rick")
            .withOrigin(origin)
            .withLocation(origin)
            .withImage("https://example.com/image.jpg")
            .withUrl("https://example.com/character/1")
            .build()

        val character2 = CharacterBuilder.aCharacter()
            .withName("Rick")
            .withOrigin(origin)
            .withLocation(origin)
            .withImage("https://example.com/image.jpg")
            .withUrl("https://example.com/character/1")
            .build()

        assertEquals(character1, character2)
    }
}