package com.infinitum.labs.domain.character.model

import com.infinitum.labs.domain.character.model.builder.CharacterLocationBuilder
import org.junit.Assert.assertEquals
import org.junit.Test

class CharacterLocationTest {

    @Test
    fun `create location with name and url should succeed`() {
        // Given
        val name = "Earth (C-137)"
        val url = "https://rickandmortyapi.com/api/location/1"

        // When
        val location = CharacterLocationBuilder.aCharacterLocation()
            .withName(name)
            .withUrl(url)
            .build()

        // Then
        assertEquals(name, location.name)
        assertEquals(url, location.url)
    }

    @Test
    fun `create location with empty url should succeed`() {
        // When
        val location = CharacterLocationBuilder.anUnknownLocation().build()

        // Then
        assertEquals("unknown", location.name)
        assertEquals("", location.url)
    }

    @Test
    fun `location copy with different name should preserve url`() {
        // Given
        val location = CharacterLocationBuilder.aCharacterLocation()
            .withName("Earth")
            .build()

        // When
        val copiedLocation = location.copy(name = "Earth (C-137)")

        // Then
        assertEquals("Earth (C-137)", copiedLocation.name)
        assertEquals(location.url, copiedLocation.url)
    }

    @Test
    fun `location equality should work correctly`() {
        // Given
        val location1 = CharacterLocationBuilder.aCharacterLocation()
            .withName("Earth (C-137)")
            .withUrl("https://rickandmortyapi.com/api/location/1")
            .build()
        val location2 = CharacterLocationBuilder.aCharacterLocation()
            .withName("Earth (C-137)")
            .withUrl("https://rickandmortyapi.com/api/location/1")
            .build()

        // Then
        assertEquals(location1, location2)
    }
}