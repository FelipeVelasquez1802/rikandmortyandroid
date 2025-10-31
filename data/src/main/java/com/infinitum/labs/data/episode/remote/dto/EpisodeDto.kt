package com.infinitum.labs.data.episode.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data Transfer Object for Episode from the Rick and Morty API.
 * This DTO represents the raw data structure from the API.
 *
 * Marked as internal to prevent leaking data layer details to other modules.
 */
@Serializable
internal data class EpisodeDto(
    val id: Int,
    val name: String,
    @SerialName("air_date") val airDate: String,
    val episode: String,
    val characters: List<String>,
    val url: String,
    val created: String
)