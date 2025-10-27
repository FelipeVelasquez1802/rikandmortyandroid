package com.infinitum.labs.domain.character.model

import com.infinitum.labs.domain.character.model.builder.CharacterBuilder
import com.infinitum.labs.domain.character.model.builder.CharacterLocationBuilder
import org.junit.Assert.assertEquals
import org.junit.Test

class CharacterTest {

    @Test
    fun `create character with all properties should succeed`() {
        // Given
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

        // When
        val character = CharacterBuilder.rickSanchez()
            .withOrigin(origin)
            .withLocation(location)
            .withEpisode(episodes)
            .build()

        // Then
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
    fun `character with DEAD status should be created correctly`() {
        // When
        val character = CharacterBuilder.mortySmith()
            .withStatus(CharacterStatus.DEAD)
            .withEpisode(emptyList())
            .build()

        // Then
        assertEquals(CharacterStatus.DEAD, character.status)
        assertEquals("Morty Smith", character.name)
    }

    @Test
    fun `character with UNKNOWN status should be created correctly`() {
        // When
        val character = CharacterBuilder.summerSmith()
            .withStatus(CharacterStatus.UNKNOWN)
            .withOrigin(CharacterLocationBuilder.anUnknownLocation().build())
            .withLocation(CharacterLocationBuilder.anUnknownLocation().build())
            .withEpisode(emptyList())
            .build()

        // Then
        assertEquals(CharacterStatus.UNKNOWN, character.status)
        assertEquals(CharacterGender.FEMALE, character.gender)
    }

    @Test
    fun `character copy with different name should preserve other properties`() {
        // Given
        val character = CharacterBuilder.aCharacter()
            .withName("Rick")
            .withImage("image.jpg")
            .withUrl("url")
            .withEpisode(emptyList())
            .build()

        // When
        val copiedCharacter = character.copy(name = "Rick Sanchez")

        // Then
        assertEquals("Rick Sanchez", copiedCharacter.name)
        assertEquals(character.id, copiedCharacter.id)
        assertEquals(character.status, copiedCharacter.status)
        assertEquals(character.species, copiedCharacter.species)
    }

    @Test
    fun `character equality should work correctly`() {
        // Given
        val origin = CharacterLocationBuilder.aCharacterLocation()
            .withName("Earth")
            .withUrl("url")
            .build()

        val character1 = CharacterBuilder.aCharacter()
            .withName("Rick")
            .withOrigin(origin)
            .withLocation(origin)
            .withImage("image.jpg")
            .withUrl("url")
            .withEpisode(emptyList())
            .build()

        val character2 = CharacterBuilder.aCharacter()
            .withName("Rick")
            .withOrigin(origin)
            .withLocation(origin)
            .withImage("image.jpg")
            .withUrl("url")
            .withEpisode(emptyList())
            .build()

        // Then
        assertEquals(character1, character2)
    }
}