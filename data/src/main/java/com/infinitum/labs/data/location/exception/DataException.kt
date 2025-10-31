package com.infinitum.labs.data.location.exception

internal sealed class LocationDataException(
    message: String,
    cause: Throwable? = null
) : Exception(message, cause) {

    data class LocationApiNetworkError(
        override val message: String = "Cannot connect to location API",
        override val cause: Throwable? = null
    ) : LocationDataException(message, cause)

    data class LocationApiServerError(
        val statusCode: Int,
        override val message: String = "Location API server error (HTTP $statusCode)"
    ) : LocationDataException(message)

    data class LocationApiClientError(
        val statusCode: Int,
        override val message: String = "Location API request failed (HTTP $statusCode)"
    ) : LocationDataException(message)

    data class LocationNotFoundInApi(
        val locationId: Int
    ) : LocationDataException("Location with ID $locationId not found in API")

    data class LocationsNotFoundInApiByName(
        val searchName: String
    ) : LocationDataException("No locations found in API for name '$searchName'")

    data class LocationDataParseError(
        override val message: String = "Failed to parse location data from API",
        override val cause: Throwable? = null
    ) : LocationDataException(message, cause)

    data class LocationApiUnexpectedError(
        override val message: String = "Unexpected error when accessing location API",
        override val cause: Throwable? = null
    ) : LocationDataException(message, cause)
}