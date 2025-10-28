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
        validateSpecies()
        validateImage()
        validateUrl()
        validateEpisodes()
        validateLocations()
    }

    /**
     * Validates that the character ID is valid.
     * Business rule: Character IDs must be positive integers (>= 1).
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
            throw CharacterException.InvalidCharacterName(name = name)
        }
    }

    /**
     * Validates that the character species is valid.
     * Business rule: Character species cannot be blank.
     */
    private fun validateSpecies() {
        if (species.isBlank()) {
            throw CharacterException.InvalidCharacterSpecies(species = species)
        }
    }

    /**
     * Validates that the character image URL is valid.
     * Business rule: Character image URLs cannot be blank and should be valid URLs.
     */
    private fun validateImage() {
        if (image.isBlank()) {
            throw CharacterException.InvalidCharacterImageUrl(imageUrl = image)
        }
        // Optional: Add URL format validation
        if (!image.startsWith("http://") && !image.startsWith("https://")) {
            throw CharacterException.InvalidCharacterImageUrl(imageUrl = image)
        }
    }

    /**
     * Validates that the character URL is valid.
     * Business rule: Character URLs cannot be blank and should be valid URLs.
     */
    private fun validateUrl() {
        if (url.isBlank()) {
            throw CharacterException.InvalidCharacterUrl(url = url)
        }
        // Optional: Add URL format validation
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            throw CharacterException.InvalidCharacterUrl(url = url)
        }
    }

    /**
     * Validates that the character has episodes.
     * Business rule: Characters must have appeared in at least one episode.
     */
    private fun validateEpisodes() {
        if (episode.isEmpty()) {
            throw CharacterException.InvalidCharacterEpisodes(episodeCount = episode.size)
        }
    }

    /**
     * Validates that the character's origin and location are valid.
     * Business rule: Location names cannot be blank.
     */
    private fun validateLocations() {
        // Validate origin
        if (origin.name.isBlank()) {
            throw CharacterException.InvalidCharacterLocation(
                locationType = "origin",
                reason = "Origin name cannot be blank"
            )
        }

        // Validate current location
        if (location.name.isBlank()) {
            throw CharacterException.InvalidCharacterLocation(
                locationType = "location",
                reason = "Location name cannot be blank"
            )
        }
    }
}