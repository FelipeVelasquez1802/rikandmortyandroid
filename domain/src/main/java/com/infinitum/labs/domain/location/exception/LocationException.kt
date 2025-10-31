package com.infinitum.labs.domain.location.exception

import com.infinitum.labs.domain.common.exception.DomainException

sealed class LocationException(
    message: String,
    cause: Throwable? = null,
    errorCode: String? = null
) : DomainException(message, cause, errorCode) {

    data class LocationNotFound(
        val locationId: Int
    ) : LocationException(
        message = "Location with ID $locationId does not exist",
        errorCode = "LOCATION_NOT_FOUND"
    )

    data class LocationsNotFoundByName(
        val searchName: String
    ) : LocationException(
        message = "No locations found matching name '$searchName'",
        errorCode = "LOCATIONS_NOT_FOUND_BY_NAME"
    )

    data class InvalidLocationId(
        val locationId: Int
    ) : LocationException(
        message = "Invalid location ID: $locationId. ID must be greater than 0",
        errorCode = "INVALID_LOCATION_ID"
    )

    data class InvalidLocationName(
        val name: String
    ) : LocationException(
        message = "Invalid location name: name cannot be blank",
        errorCode = "INVALID_LOCATION_NAME"
    )

    data class InvalidLocationType(
        val type: String
    ) : LocationException(
        message = "Invalid location type: type cannot be blank",
        errorCode = "INVALID_LOCATION_TYPE"
    )

    data class InvalidLocationDimension(
        val dimension: String
    ) : LocationException(
        message = "Invalid location dimension: dimension cannot be blank",
        errorCode = "INVALID_LOCATION_DIMENSION"
    )

    data class InvalidLocationUrl(
        val url: String
    ) : LocationException(
        message = "Invalid location URL: '$url' cannot be blank or must be a valid URL",
        errorCode = "INVALID_LOCATION_URL"
    )

    data class InvalidLocationCreatedDate(
        val created: String
    ) : LocationException(
        message = "Invalid location created date: '$created' must be a valid ISO-8601 date",
        errorCode = "INVALID_LOCATION_CREATED_DATE"
    )

    data class InvalidLocationPage(
        val page: Int
    ) : LocationException(
        message = "Invalid page number: $page. Page must be greater than 0",
        errorCode = "INVALID_LOCATION_PAGE"
    )

    data class InvalidLocationSearchQuery(
        val query: String
    ) : LocationException(
        message = "Invalid search query: query cannot be blank",
        errorCode = "INVALID_LOCATION_SEARCH_QUERY"
    )

    data class LocationRepositoryUnavailable(
        override val message: String = "The location catalog is temporarily unavailable",
        override val cause: Throwable? = null
    ) : LocationException(
        message = message,
        cause = cause,
        errorCode = "LOCATION_REPOSITORY_UNAVAILABLE"
    )

    data class InvalidLocationData(
        override val message: String = "Location data is invalid or corrupted",
        override val cause: Throwable? = null
    ) : LocationException(
        message = message,
        cause = cause,
        errorCode = "INVALID_LOCATION_DATA"
    )
}
