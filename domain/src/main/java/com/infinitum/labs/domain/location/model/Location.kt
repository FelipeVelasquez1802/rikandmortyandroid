package com.infinitum.labs.domain.location.model

import com.infinitum.labs.domain.location.exception.LocationException

/**
 * Location domain model representing a location from Rick and Morty.
 * This model is rich with business logic and enforces its own invariants.
 *
 * The model validates itself on construction to ensure it always represents
 * a valid location according to business rules.
 */
data class Location(
    val id: Int,
    val name: String,
    val type: String,
    val dimension: String,
    val residents: List<String>,
    val url: String,
    val created: String
) {
    init {
        // Validate on construction - ensures invariants
        validate()
    }

    /**
     * Validates all location fields according to business rules.
     * This method is private and called automatically on construction.
     * Throws LocationException if any validation fails.
     */
    private fun validate() {
        validateId()
        validateName()
        validateType()
        validateDimension()
        validateUrl()
        validateCreated()
    }

    /**
     * Validates that the location ID is valid.
     * Business rule: Location IDs must be positive integers (>= 1).
     */
    private fun validateId() {
        if (id < 1) {
            throw LocationException.InvalidLocationId(locationId = id)
        }
    }

    /**
     * Validates that the location name is valid.
     * Business rule: Location names cannot be blank.
     */
    private fun validateName() {
        if (name.isBlank()) {
            throw LocationException.InvalidLocationName(name = name)
        }
    }

    /**
     * Validates that the location type is valid.
     * Business rule: Location types cannot be blank.
     */
    private fun validateType() {
        if (type.isBlank()) {
            throw LocationException.InvalidLocationType(type = type)
        }
    }

    /**
     * Validates that the location dimension is valid.
     * Business rule: Location dimensions cannot be blank.
     */
    private fun validateDimension() {
        if (dimension.isBlank()) {
            throw LocationException.InvalidLocationDimension(dimension = dimension)
        }
    }

    /**
     * Validates that the location URL is valid.
     * Business rule: Location URLs cannot be blank and should be valid HTTP/HTTPS URLs.
     */
    private fun validateUrl() {
        if (url.isBlank()) {
            throw LocationException.InvalidLocationUrl(url = url)
        }
        // URL format validation
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            throw LocationException.InvalidLocationUrl(url = url)
        }
    }

    /**
     * Validates that the created date is valid.
     * Business rule: Created date cannot be blank (should be valid ISO-8601 format).
     */
    private fun validateCreated() {
        if (created.isBlank()) {
            throw LocationException.InvalidLocationCreatedDate(created = created)
        }
    }
}