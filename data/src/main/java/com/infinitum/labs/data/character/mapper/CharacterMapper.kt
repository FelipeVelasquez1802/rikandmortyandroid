package com.infinitum.labs.data.character.mapper

import com.infinitum.labs.data.character.remote.dto.CharacterDto
import com.infinitum.labs.data.character.remote.dto.CharacterLocationDto
import com.infinitum.labs.domain.character.model.Character
import com.infinitum.labs.domain.character.model.CharacterGender
import com.infinitum.labs.domain.character.model.CharacterLocation
import com.infinitum.labs.domain.character.model.CharacterStatus
import java.util.Locale

internal object CharacterMapper {

    fun toDomain(dto: CharacterDto): Character {
        return Character(
            id = dto.id,
            name = dto.name,
            status = toCharacterStatus(dto.status),
            species = dto.species,
            type = dto.type,
            gender = toCharacterGender(dto.gender),
            origin = toDomain(dto.origin),
            location = toDomain(dto.location),
            image = dto.image,
            episode = dto.episode,
            url = dto.url,
            created = dto.created
        )
    }

    fun toDomain(dto: CharacterLocationDto): CharacterLocation {
        return CharacterLocation(
            name = dto.name,
            url = dto.url
        )
    }

    private fun toCharacterStatus(status: String): CharacterStatus {
        return when (status.uppercase(Locale.ROOT)) {
            "ALIVE" -> CharacterStatus.ALIVE
            "DEAD" -> CharacterStatus.DEAD
            else -> CharacterStatus.UNKNOWN
        }
    }

    private fun toCharacterGender(gender: String): CharacterGender {
        return when (gender.uppercase(Locale.ROOT)) {
            "FEMALE" -> CharacterGender.FEMALE
            "MALE" -> CharacterGender.MALE
            "GENDERLESS" -> CharacterGender.GENDERLESS
            else -> CharacterGender.UNKNOWN
        }
    }
}