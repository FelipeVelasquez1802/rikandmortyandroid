package com.infinitum.labs.domain.character.model

import com.infinitum.labs.domain.character.exception.CharacterException

/**
 * Character domain model representing a character from Rick and Morty.
 * This model is rich with business logic and enforces its own invariants.
 *
 * The model validates itself on construction to ensure it always represents
 * a valid character according to business rules.
 */
data class Character(
    val id: Int,
    val name: String,
    val status: CharacterStatus,
    val species: String,
    val type: String,
    val gender: CharacterGender,
    val origin: CharacterLocation,
    val location: CharacterLocation,
    val image: String,
    val episode: List<String>,
    val url: String,
    val created: String
) {
    init {
        // Validate on construction - ensures invariants
        validate()
    }

    /**
     * Validates all character fields according to business rules.
     * This method is private and called automatically on construction.
     * Throws CharacterException if any validation fails.
     */
    private fun validate() {
        validateId()
        validateName()
        validateImage()
        validateUrl()
        validateEpisodes()
    }

    /**
     * Validates that the character ID is valid.
     * Business rule: Character IDs must be positive integers.
     */
    private fun validateId() {
        if (id < 1) {
            throw CharacterException.InvalidCharacterId(characterId = id)
        }
    }

    /**
     * Validates that the character name is valid.
     * Business rule: Character names cannot be blank.
     */
    private fun validateName() {
        if (name.isBlank()) {
            throw CharacterException.InvalidCharacterData(
                message = "Character name cannot be blank"
            )
        }
    }

    /**
     * Validates that the character image URL is valid.
     * Business rule: Character image URLs cannot be blank.
     */
    private fun validateImage() {
        if (image.isBlank()) {
            throw CharacterException.InvalidCharacterData(
                message = "Character image URL cannot be blank"
            )
        }
    }

    /**
     * Validates that the character URL is valid.
     * Business rule: Character URLs cannot be blank.
     */
    private fun validateUrl() {
        if (url.isBlank()) {
            throw CharacterException.InvalidCharacterData(
                message = "Character URL cannot be blank"
            )
        }
    }

    /**
     * Validates that the character has episodes.
     * Business rule: Characters must have appeared in at least one episode.
     */
    private fun validateEpisodes() {
        if (episode.isEmpty()) {
            throw CharacterException.InvalidCharacterData(
                message = "Character must have appeared in at least one episode"
            )
        }
    }
}