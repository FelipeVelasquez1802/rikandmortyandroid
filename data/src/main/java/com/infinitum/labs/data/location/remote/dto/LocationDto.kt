package com.infinitum.labs.data.location.remote.dto

import kotlinx.serialization.Serializable

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