package com.infinitum.labs.domain.character.model.builder

import com.infinitum.labs.domain.character.model.CharacterLocation

class CharacterLocationBuilder {
    private var name: String = "Earth (C-137)"
    private var url: String = "https://rickandmortyapi.com/api/location/1"

    fun withName(name: String) = apply { this.name = name }

    fun withUrl(url: String) = apply { this.url = url }

    fun withUnknown() = apply {
        this.name = "unknown"
        this.url = ""
    }

    fun build(): CharacterLocation {
        return CharacterLocation(
            name = name,
            url = url
        )
    }

    companion object {
        fun aCharacterLocation() = CharacterLocationBuilder()

        fun anUnknownLocation() = CharacterLocationBuilder().withUnknown()
    }
}