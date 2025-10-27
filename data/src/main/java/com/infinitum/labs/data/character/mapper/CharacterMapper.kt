package com.infinitum.labs.data.character.mapper

import com.infinitum.labs.data.character.remote.dto.CharacterDto
import com.infinitum.labs.data.character.remote.dto.CharacterLocationDto
import com.infinitum.labs.domain.character.model.Character
import com.infinitum.labs.domain.character.model.CharacterGender
import com.infinitum.labs.domain.character.model.CharacterLocation
import com.infinitum.labs.domain.character.model.CharacterStatus
import java.util.Locale

fun CharacterDto.toDomain(): Character {
    return Character(
        id = id,
        name = name,
        status = status.toCharacterStatus(),
        species = species,
        type = type,
        gender = gender.toCharacterGender(),
        origin = origin.toDomain(),
        location = location.toDomain(),
        image = image,
        episode = episode,
        url = url,
        created = created
    )
}

fun CharacterLocationDto.toDomain(): CharacterLocation {
    return CharacterLocation(
        name = name,
        url = url
    )
}

fun String.toCharacterStatus(): CharacterStatus {
    return when (this.uppercase(Locale.ROOT)) {
        "ALIVE" -> CharacterStatus.ALIVE
        "DEAD" -> CharacterStatus.DEAD
        else -> CharacterStatus.UNKNOWN
    }
}

fun String.toCharacterGender(): CharacterGender {
    return when (this.uppercase(Locale.ROOT)) {
        "FEMALE" -> CharacterGender.FEMALE
        "MALE" -> CharacterGender.MALE
        "GENDERLESS" -> CharacterGender.GENDERLESS
        else -> CharacterGender.UNKNOWN
    }
}