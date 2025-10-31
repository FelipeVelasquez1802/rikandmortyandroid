package com.infinitum.labs.data.character.remote.dto

import kotlinx.serialization.Serializable

@Serializable
internal data class CharacterResponseDto(
    val info: InfoDto,
    val results: List<CharacterDto>
)

@Serializable
internal data class InfoDto(
    val count: Int,
    val pages: Int,
    val next: String?,
    val prev: String?
)