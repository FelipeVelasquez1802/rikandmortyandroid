package com.infinitum.labs.domain.location.repository

import com.infinitum.labs.domain.location.model.Location

/**
 * Repository interface for Location domain operations.
 * Defines the contract for accessing location data.
 *
 * All methods return Result<T> to handle success and failure cases explicitly.
 * This follows the Railway Oriented Programming pattern for error handling.
 */
interface LocationRepository {
    /**
     * Retrieves a paginated list of locations.
     *
     * @param page The page number to retrieve (1-indexed). Defaults to 1.
     * @return Result containing a list of locations, or a failure with LocationException.
     */
    suspend fun getLocations(page: Int = 1): Result<List<Location>>

    /**
     * Retrieves a single location by its ID.
     *
     * @param id The unique identifier of the location.
     * @return Result containing the location, or a failure with LocationException.
     */
    suspend fun getLocation(id: Int): Result<Location>

    /**
     * Searches for locations by name with pagination support.
     *
     * @param name The name to search for (partial matching).
     * @param page The page number to retrieve (1-indexed). Defaults to 1.
     * @return Result containing a list of matching locations, or a failure with LocationException.
     */
    suspend fun getLocationsByName(name: String, page: Int = 1): Result<List<Location>>
}