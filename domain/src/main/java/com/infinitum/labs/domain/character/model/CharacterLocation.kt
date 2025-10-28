package com.infinitum.labs.domain.character.model

import com.infinitum.labs.domain.character.exception.CharacterException

data class CharacterLocation(
    val name: String,
    val url: String
) {
    init {
        validate()
    }

    private fun validate() {
        validateName()
        validateUrl()
    }

    private fun validateName() {
        if (name.isBlank()) {
            throw CharacterException.InvalidCharacterLocation(
                locationType = "location",
                reason = "Location name cannot be blank"
            )
        }
    }

    private fun validateUrl() {
        if (url.isNotBlank() && !url.startsWith("http://") && !url.startsWith("https://")) {
            throw CharacterException.InvalidCharacterLocation(
                locationType = "location",
                reason = "Location URL must be a valid HTTP/HTTPS URL or empty"
            )
        }
    }
}