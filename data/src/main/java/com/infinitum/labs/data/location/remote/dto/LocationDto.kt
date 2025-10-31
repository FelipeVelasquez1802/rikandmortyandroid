package com.infinitum.labs.data.location.remote.dto

import kotlinx.serialization.Serializable

/**
 * Data Transfer Object for Location from the Rick and Morty API.
 * This DTO represents the raw data structure from the API.
 *
 * Marked as internal to prevent leaking data layer details to other modules.
 */
@Serializable
internal data class LocationDto(
    val id: Int,
    val name: String,
    val type: String,
    val dimension: String,
    val residents: List<String>,
    val url: String,
    val created: String
)