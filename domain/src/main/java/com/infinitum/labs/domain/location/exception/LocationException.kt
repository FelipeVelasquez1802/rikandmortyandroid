package com.infinitum.labs.domain.location.exception

import com.infinitum.labs.domain.common.exception.DomainException

/**
 * Sealed hierarchy of exceptions for Location domain operations.
 * These exceptions are focused on the Location model and business rules.
 * They speak the ubiquitous language of the Location domain.
 *
 * All location exceptions inherit from DomainException to maintain
 * consistency with the overall domain exception architecture.
 */
sealed class LocationException(
    message: String,
    cause: Throwable? = null,
    errorCode: String? = null
) : DomainException(message, cause, errorCode) {

    // ========== NOT FOUND EXCEPTIONS ==========

    /**
     * Thrown when a location with the specified ID does not exist in the system.
     * Business rule: Location IDs must reference existing locations.
     */
    data class LocationNotFound(
        val locationId: Int
    ) : LocationException(
        message = "Location with ID $locationId does not exist",
        errorCode = "LOCATION_NOT_FOUND"
    )

    /**
     * Thrown when a location search by name yields no results.
     * Business rule: Location searches may return empty results.
     */
    data class LocationsNotFoundByName(
        val searchName: String
    ) : LocationException(
        message = "No locations found matching name '$searchName'",
        errorCode = "LOCATIONS_NOT_FOUND_BY_NAME"
    )

    // ========== VALIDATION EXCEPTIONS (for model invariants) ==========

    /**
     * Thrown when a location ID is invalid.
     * Business rule: Location IDs must be positive integers (>= 1).
     */
    data class InvalidLocationId(
        val locationId: Int
    ) : LocationException(
        message = "Invalid location ID: $locationId. ID must be greater than 0",
        errorCode = "INVALID_LOCATION_ID"
    )

    /**
     * Thrown when a location name is invalid.
     * Business rule: Location names cannot be blank or empty.
     */
    data class InvalidLocationName(
        val name: String
    ) : LocationException(
        message = "Invalid location name: name cannot be blank",
        errorCode = "INVALID_LOCATION_NAME"
    )

    /**
     * Thrown when a location type is invalid.
     * Business rule: Location types cannot be blank.
     */
    data class InvalidLocationType(
        val type: String
    ) : LocationException(
        message = "Invalid location type: type cannot be blank",
        errorCode = "INVALID_LOCATION_TYPE"
    )

    /**
     * Thrown when a location dimension is invalid.
     * Business rule: Location dimensions cannot be blank.
     */
    data class InvalidLocationDimension(
        val dimension: String
    ) : LocationException(
        message = "Invalid location dimension: dimension cannot be blank",
        errorCode = "INVALID_LOCATION_DIMENSION"
    )

    /**
     * Thrown when a location URL is invalid.
     * Business rule: Location URLs cannot be blank and must be valid HTTP/HTTPS URLs.
     */
    data class InvalidLocationUrl(
        val url: String
    ) : LocationException(
        message = "Invalid location URL: '$url' cannot be blank or must be a valid URL",
        errorCode = "INVALID_LOCATION_URL"
    )

    /**
     * Thrown when a location's created date is invalid.
     * Business rule: Location creation dates must be valid ISO-8601 format.
     */
    data class InvalidLocationCreatedDate(
        val created: String
    ) : LocationException(
        message = "Invalid location created date: '$created' must be a valid ISO-8601 date",
        errorCode = "INVALID_LOCATION_CREATED_DATE"
    )

    // ========== USE CASE / QUERY VALIDATION EXCEPTIONS ==========

    /**
     * Thrown when attempting to retrieve locations with invalid pagination.
     * Business rule: Page numbers must be positive integers (>= 1).
     */
    data class InvalidLocationPage(
        val page: Int
    ) : LocationException(
        message = "Invalid page number: $page. Page must be greater than 0",
        errorCode = "INVALID_LOCATION_PAGE"
    )

    /**
     * Thrown when a location search query is invalid.
     * Business rule: Search queries cannot be empty or blank.
     */
    data class InvalidLocationSearchQuery(
        val query: String
    ) : LocationException(
        message = "Invalid search query: query cannot be blank",
        errorCode = "INVALID_LOCATION_SEARCH_QUERY"
    )

    // ========== REPOSITORY / INFRASTRUCTURE EXCEPTIONS ==========

    /**
     * Thrown when the location repository/catalog is temporarily unavailable.
     * Business rule: The location catalog should be accessible, but may be temporarily unavailable.
     */
    data class LocationRepositoryUnavailable(
        override val message: String = "The location catalog is temporarily unavailable",
        override val cause: Throwable? = null
    ) : LocationException(
        message = message,
        cause = cause,
        errorCode = "LOCATION_REPOSITORY_UNAVAILABLE"
    )

    /**
     * Thrown when location data cannot be loaded due to corruption or invalid format.
     * Business rule: Location data must be in a valid, readable format.
     * This is typically used at the data layer when parsing fails.
     */
    data class InvalidLocationData(
        override val message: String = "Location data is invalid or corrupted",
        override val cause: Throwable? = null
    ) : LocationException(
        message = message,
        cause = cause,
        errorCode = "INVALID_LOCATION_DATA"
    )
}
