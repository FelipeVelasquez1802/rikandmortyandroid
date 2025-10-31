package com.infinitum.labs.domain.location.repository

import com.infinitum.labs.domain.location.model.Location

interface LocationRepository {
    suspend fun getLocations(page: Int = 1): Result<List<Location>>
    suspend fun getLocation(id: Int): Result<Location>
    suspend fun getLocationsByName(name: String, page: Int = 1): Result<List<Location>>
}