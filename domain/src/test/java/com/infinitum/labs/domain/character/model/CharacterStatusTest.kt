package com.infinitum.labs.domain.character.model

import org.junit.Assert.assertEquals
import org.junit.Test

class CharacterStatusTest {

    @Test
    fun `CharacterStatus should have ALIVE value`() {
        // When
        val status = CharacterStatus.ALIVE

        // Then
        assertEquals("ALIVE", status.name)
    }

    @Test
    fun `CharacterStatus should have DEAD value`() {
        // When
        val status = CharacterStatus.DEAD

        // Then
        assertEquals("DEAD", status.name)
    }

    @Test
    fun `CharacterStatus should have UNKNOWN value`() {
        // When
        val status = CharacterStatus.UNKNOWN

        // Then
        assertEquals("UNKNOWN", status.name)
    }

    @Test
    fun `CharacterStatus should have exactly 3 values`() {
        // When
        val values = CharacterStatus.values()

        // Then
        assertEquals(3, values.size)
    }

    @Test
    fun `CharacterStatus valueOf should return correct enum`() {
        // When
        val alive = CharacterStatus.valueOf("ALIVE")
        val dead = CharacterStatus.valueOf("DEAD")
        val unknown = CharacterStatus.valueOf("UNKNOWN")

        // Then
        assertEquals(CharacterStatus.ALIVE, alive)
        assertEquals(CharacterStatus.DEAD, dead)
        assertEquals(CharacterStatus.UNKNOWN, unknown)
    }
}