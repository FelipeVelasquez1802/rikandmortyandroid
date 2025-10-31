package com.infinitum.labs.domain.character.model

import com.infinitum.labs.domain.character.exception.CharacterException

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
        validate()
    }

    private fun validate() {
        validateId()
        validateName()
        validateSpecies()
        validateImage()
        validateUrl()
        validateEpisodes()
        validateLocations()
    }

    private fun validateId() {
        if (id < 1) {
            throw CharacterException.InvalidCharacterId(characterId = id)
        }
    }

    private fun validateName() {
        if (name.isBlank()) {
            throw CharacterException.InvalidCharacterName(name = name)
        }
    }

    private fun validateSpecies() {
        if (species.isBlank()) {
            throw CharacterException.InvalidCharacterSpecies(species = species)
        }
    }

    private fun validateImage() {
        if (image.isBlank()) {
            throw CharacterException.InvalidCharacterImageUrl(imageUrl = image)
        }
        if (!image.startsWith("http://") && !image.startsWith("https://")) {
            throw CharacterException.InvalidCharacterImageUrl(imageUrl = image)
        }
    }

    private fun validateUrl() {
        if (url.isBlank()) {
            throw CharacterException.InvalidCharacterUrl(url = url)
        }
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            throw CharacterException.InvalidCharacterUrl(url = url)
        }
    }

    private fun validateEpisodes() {
        if (episode.isEmpty()) {
            throw CharacterException.InvalidCharacterEpisodes(episodeCount = episode.size)
        }
    }

    private fun validateLocations() {
        if (origin.name.isBlank()) {
            throw CharacterException.InvalidCharacterLocation(
                locationType = "origin",
                reason = "Origin name cannot be blank"
            )
        }

        if (location.name.isBlank()) {
            throw CharacterException.InvalidCharacterLocation(
                locationType = "location",
                reason = "Location name cannot be blank"
            )
        }
    }
}