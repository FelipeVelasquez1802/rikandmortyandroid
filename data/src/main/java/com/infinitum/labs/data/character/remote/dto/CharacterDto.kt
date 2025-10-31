package com.infinitum.labs.data.character.remote.dto

import kotlinx.serialization.Serializable

@Serializable
internal data class CharacterDto(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val origin: CharacterLocationDto,
    val location: CharacterLocationDto,
    val image: String,
    val episode: List<String>,
    val url: String,
    val created: String
)