package com.infinitum.labs.domain.episode.model

import com.infinitum.labs.domain.episode.exception.EpisodeException

/**
 * Episode domain model representing an episode from Rick and Morty.
 * This model is rich with business logic and enforces its own invariants.
 *
 * The model validates itself on construction to ensure it always represents
 * a valid episode according to business rules.
 */
data class Episode(
    val id: Int,
    val name: String,
    val airDate: String,
    val episode: String,
    val characters: List<String>,
    val url: String,
    val created: String
) {
    init {
        // Validate on construction - ensures invariants
        validate()
    }

    /**
     * Validates all episode fields according to business rules.
     * This method is private and called automatically on construction.
     * Throws EpisodeException if any validation fails.
     */
    private fun validate() {
        validateId()
        validateName()
        validateAirDate()
        validateEpisodeCode()
        validateUrl()
        validateCreated()
    }

    /**
     * Validates that the episode ID is valid.
     * Business rule: Episode IDs must be positive integers (>= 1).
     */
    private fun validateId() {
        if (id < 1) {
            throw EpisodeException.InvalidEpisodeId(episodeId = id)
        }
    }

    /**
     * Validates that the episode name is valid.
     * Business rule: Episode names cannot be blank.
     */
    private fun validateName() {
        if (name.isBlank()) {
            throw EpisodeException.InvalidEpisodeName(name = name)
        }
    }

    /**
     * Validates that the episode air date is valid.
     * Business rule: Air dates cannot be blank.
     */
    private fun validateAirDate() {
        if (airDate.isBlank()) {
            throw EpisodeException.InvalidEpisodeAirDate(airDate = airDate)
        }
    }

    /**
     * Validates that the episode code is valid.
     * Business rule: Episode codes cannot be blank and should follow format (e.g., "S01E01").
     */
    private fun validateEpisodeCode() {
        if (episode.isBlank()) {
            throw EpisodeException.InvalidEpisodeCode(episodeCode = episode)
        }
    }

    /**
     * Validates that the episode URL is valid.
     * Business rule: Episode URLs cannot be blank and should be valid HTTP/HTTPS URLs.
     */
    private fun validateUrl() {
        if (url.isBlank()) {
            throw EpisodeException.InvalidEpisodeUrl(url = url)
        }
        // URL format validation
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            throw EpisodeException.InvalidEpisodeUrl(url = url)
        }
    }

    /**
     * Validates that the created date is valid.
     * Business rule: Created date cannot be blank (should be valid ISO-8601 format).
     */
    private fun validateCreated() {
        if (created.isBlank()) {
            throw EpisodeException.InvalidEpisodeCreatedDate(created = created)
        }
    }
}
