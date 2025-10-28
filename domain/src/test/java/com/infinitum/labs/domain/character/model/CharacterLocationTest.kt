package com.infinitum.labs.domain.character.model

import com.infinitum.labs.domain.character.exception.CharacterException
import com.infinitum.labs.domain.character.model.builder.CharacterLocationBuilder
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test

class CharacterLocationTest {

    @Test
    fun `given valid name and url when creating location then returns location with correct properties`() {
        val name = "Earth (C-137)"
        val url = "https://rickandmortyapi.com/api/location/1"

        val location = CharacterLocationBuilder.aCharacterLocation()
            .withName(name)
            .withUrl(url)
            .build()

        assertEquals(name, location.name)
        assertEquals(url, location.url)
    }

    @Test
    fun `given valid name and empty url when creating location then returns location successfully`() {
        val location = CharacterLocationBuilder.anUnknownLocation().build()

        assertEquals("unknown", location.name)
        assertEquals("", location.url)
    }

    @Test
    fun `given blank name when creating location then throws InvalidCharacterLocation`() {
        try {
            CharacterLocationBuilder.aCharacterLocation()
                .withName("   ")
                .build()
            fail("Should have thrown InvalidCharacterLocation")
        } catch (e: CharacterException.InvalidCharacterLocation) {
            assertEquals("location", e.locationType)
        }
    }

    @Test
    fun `given empty name when creating location then throws InvalidCharacterLocation`() {
        try {
            CharacterLocationBuilder.aCharacterLocation()
                .withName("")
                .build()
            fail("Should have thrown InvalidCharacterLocation")
        } catch (e: CharacterException.InvalidCharacterLocation) {
            assertEquals("location", e.locationType)
        }
    }

    @Test
    fun `given invalid url without protocol when creating location then throws InvalidCharacterLocation`() {
        try {
            CharacterLocationBuilder.aCharacterLocation()
                .withName("Earth")
                .withUrl("invalid-url")
                .build()
            fail("Should have thrown InvalidCharacterLocation")
        } catch (e: CharacterException.InvalidCharacterLocation) {
            assertEquals("location", e.locationType)
        }
    }

    @Test
    fun `given location when copying with different name then preserves url`() {
        val location = CharacterLocationBuilder.aCharacterLocation()
            .withName("Earth")
            .build()

        val copiedLocation = location.copy(name = "Earth (C-137)")

        assertEquals("Earth (C-137)", copiedLocation.name)
        assertEquals(location.url, copiedLocation.url)
    }

    @Test
    fun `given two locations with same properties when comparing then are equal`() {
        val location1 = CharacterLocationBuilder.aCharacterLocation()
            .withName("Earth (C-137)")
            .withUrl("https://rickandmortyapi.com/api/location/1")
            .build()
        val location2 = CharacterLocationBuilder.aCharacterLocation()
            .withName("Earth (C-137)")
            .withUrl("https://rickandmortyapi.com/api/location/1")
            .build()

        assertEquals(location1, location2)
    }
}