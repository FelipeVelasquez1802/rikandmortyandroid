package com.infinitum.labs.data.location.repository

import com.infinitum.labs.data.location.exception.LocationDataException
import com.infinitum.labs.data.location.mapper.LocationMapper
import com.infinitum.labs.data.location.remote.datasource.LocationRemoteDataSource
import com.infinitum.labs.domain.location.exception.LocationException
import com.infinitum.labs.domain.location.model.Location
import com.infinitum.labs.domain.location.repository.LocationRepository
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.serialization.JsonConvertException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

internal class LocationRepositoryImpl(
    private val remoteDataSource: LocationRemoteDataSource
) : LocationRepository {

    override suspend fun getLocations(page: Int): Result<List<Location>> {
        return try {
            val response = remoteDataSource.getLocations(page)
            val locations = response.results.map { LocationMapper.toDomain(it) }
            Result.success(locations)
        } catch (e: Exception) {
            Result.failure(e.toLocationDomainException())
        }
    }

    override suspend fun getLocation(id: Int): Result<Location> {
        return try {
            val locationDto = remoteDataSource.getLocation(id)
            val location = LocationMapper.toDomain(locationDto)
            Result.success(location)
        } catch (e: Exception) {
            Result.failure(e.toLocationDomainException(locationId = id))
        }
    }

    override suspend fun getLocationsByName(name: String, page: Int): Result<List<Location>> {
        return try {
            val response = remoteDataSource.getLocationsByName(name, page)
            val locations = response.results.map { LocationMapper.toDomain(it) }
            Result.success(locations)
        } catch (e: Exception) {
            Result.failure(e.toLocationDomainException(searchName = name))
        }
    }

    private fun Exception.toLocationDomainException(
        locationId: Int? = null,
        searchName: String? = null
    ): LocationException {
        val dataException = when (this) {
            is LocationException -> return this
            is LocationDataException -> this

            is ClientRequestException -> {
                when (response.status.value) {
                    404 -> {
                        if (locationId != null) {
                            LocationDataException.LocationNotFoundInApi(locationId)
                        } else if (searchName != null) {
                            LocationDataException.LocationsNotFoundInApiByName(searchName)
                        } else {
                            LocationDataException.LocationApiClientError(404)
                        }
                    }

                    else -> LocationDataException.LocationApiClientError(
                        statusCode = response.status.value,
                        message = "Location API request failed: ${response.status.description}"
                    )
                }
            }

            is ServerResponseException -> LocationDataException.LocationApiServerError(
                statusCode = response.status.value,
                message = "Location API server error: ${response.status.description}"
            )

            is UnknownHostException -> LocationDataException.LocationApiNetworkError(
                message = "Cannot reach location API server",
                cause = this
            )

            is SocketTimeoutException -> LocationDataException.LocationApiNetworkError(
                message = "Connection to location API timed out",
                cause = this
            )

            is ConnectException -> LocationDataException.LocationApiNetworkError(
                message = "Failed to connect to location API",
                cause = this
            )

            is JsonConvertException -> LocationDataException.LocationDataParseError(
                message = "Failed to parse location data from API",
                cause = this
            )

            else -> LocationDataException.LocationApiUnexpectedError(
                message = message ?: "Unexpected error when accessing location data",
                cause = this
            )
        }

        return dataException.toLocationDomainException()
    }

    private fun LocationDataException.toLocationDomainException(): LocationException {
        return when (this) {
            is LocationDataException.LocationNotFoundInApi ->
                LocationException.LocationNotFound(locationId = locationId)

            is LocationDataException.LocationsNotFoundInApiByName ->
                LocationException.LocationsNotFoundByName(searchName = searchName)

            is LocationDataException.LocationApiNetworkError,
            is LocationDataException.LocationApiServerError,
            is LocationDataException.LocationApiClientError ->
                LocationException.LocationRepositoryUnavailable(
                    message = "The location catalog is temporarily unavailable",
                    cause = this
                )

            is LocationDataException.LocationDataParseError ->
                LocationException.InvalidLocationData(
                    message = "Location data is invalid or corrupted",
                    cause = this
                )

            is LocationDataException.LocationApiUnexpectedError ->
                LocationException.LocationRepositoryUnavailable(
                    message = "The location catalog encountered an unexpected error",
                    cause = this
                )
        }
    }

}