package com.infinitum.labs.domain.character.model

import org.junit.Assert.assertEquals
import org.junit.Test

class CharacterGenderTest {

    @Test
    fun `CharacterGender should have FEMALE value`() {
        // When
        val gender = CharacterGender.FEMALE

        // Then
        assertEquals("FEMALE", gender.name)
    }

    @Test
    fun `CharacterGender should have MALE value`() {
        // When
        val gender = CharacterGender.MALE

        // Then
        assertEquals("MALE", gender.name)
    }

    @Test
    fun `CharacterGender should have GENDERLESS value`() {
        // When
        val gender = CharacterGender.GENDERLESS

        // Then
        assertEquals("GENDERLESS", gender.name)
    }

    @Test
    fun `CharacterGender should have UNKNOWN value`() {
        // When
        val gender = CharacterGender.UNKNOWN

        // Then
        assertEquals("UNKNOWN", gender.name)
    }

    @Test
    fun `CharacterGender should have exactly 4 values`() {
        // When
        val values = CharacterGender.values()

        // Then
        assertEquals(4, values.size)
    }

    @Test
    fun `CharacterGender valueOf should return correct enum`() {
        // When
        val female = CharacterGender.valueOf("FEMALE")
        val male = CharacterGender.valueOf("MALE")
        val genderless = CharacterGender.valueOf("GENDERLESS")
        val unknown = CharacterGender.valueOf("UNKNOWN")

        // Then
        assertEquals(CharacterGender.FEMALE, female)
        assertEquals(CharacterGender.MALE, male)
        assertEquals(CharacterGender.GENDERLESS, genderless)
        assertEquals(CharacterGender.UNKNOWN, unknown)
    }
}