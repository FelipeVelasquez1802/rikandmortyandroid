package com.infinitum.labs.data.location.remote.datasource

import com.infinitum.labs.data.location.remote.dto.LocationDto
import com.infinitum.labs.data.location.remote.dto.LocationResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

internal interface LocationRemoteDataSource {
    suspend fun getLocations(page: Int): LocationResponseDto
    suspend fun getLocation(id: Int): LocationDto
    suspend fun getLocationsByName(name: String, page: Int): LocationResponseDto
}

internal class LocationRemoteDataSourceImpl(
    private val httpClient: HttpClient
) : LocationRemoteDataSource {

    companion object {
        private const val BASE_URL = "https://rickandmortyapi.com/api"
        private const val LOCATION_ENDPOINT = "$BASE_URL/location"
    }

    override suspend fun getLocations(page: Int): LocationResponseDto {
        return httpClient.get(LOCATION_ENDPOINT) {
            parameter("page", page)
        }.body()
    }

    override suspend fun getLocation(id: Int): LocationDto {
        return httpClient.get("$LOCATION_ENDPOINT/$id").body()
    }

    override suspend fun getLocationsByName(name: String, page: Int): LocationResponseDto {
        return httpClient.get(LOCATION_ENDPOINT) {
            parameter("name", name)
            parameter("page", page)
        }.body()
    }
}