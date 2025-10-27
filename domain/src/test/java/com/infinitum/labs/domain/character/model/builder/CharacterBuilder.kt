package com.infinitum.labs.domain.character.model.builder

import com.infinitum.labs.domain.character.model.Character
import com.infinitum.labs.domain.character.model.CharacterGender
import com.infinitum.labs.domain.character.model.CharacterLocation
import com.infinitum.labs.domain.character.model.CharacterStatus

class CharacterBuilder {
    private var id: Int = 1
    private var name: String = "Rick Sanchez"
    private var status: CharacterStatus = CharacterStatus.ALIVE
    private var species: String = "Human"
    private var type: String = ""
    private var gender: CharacterGender = CharacterGender.MALE
    private var origin: CharacterLocation = CharacterLocationBuilder.aCharacterLocation().build()
    private var location: CharacterLocation = CharacterLocationBuilder.aCharacterLocation()
        .withName("Citadel of Ricks")
        .withUrl("https://rickandmortyapi.com/api/location/3")
        .build()
    private var image: String = "https://rickandmortyapi.com/api/character/avatar/1.jpeg"
    private var episode: List<String> = listOf(
        "https://rickandmortyapi.com/api/episode/1",
        "https://rickandmortyapi.com/api/episode/2"
    )
    private var url: String = "https://rickandmortyapi.com/api/character/1"
    private var created: String = "2017-11-04T18:48:46.250Z"

    fun withId(id: Int) = apply { this.id = id }

    fun withName(name: String) = apply { this.name = name }

    fun withStatus(status: CharacterStatus) = apply { this.status = status }

    fun withSpecies(species: String) = apply { this.species = species }

    fun withType(type: String) = apply { this.type = type }

    fun withGender(gender: CharacterGender) = apply { this.gender = gender }

    fun withOrigin(origin: CharacterLocation) = apply { this.origin = origin }

    fun withLocation(location: CharacterLocation) = apply { this.location = location }

    fun withImage(image: String) = apply { this.image = image }

    fun withEpisode(episode: List<String>) = apply { this.episode = episode }

    fun withUrl(url: String) = apply { this.url = url }

    fun withCreated(created: String) = apply { this.created = created }

    fun build(): Character {
        return Character(
            id = id,
            name = name,
            status = status,
            species = species,
            type = type,
            gender = gender,
            origin = origin,
            location = location,
            image = image,
            episode = episode,
            url = url,
            created = created
        )
    }

    companion object {
        fun aCharacter() = CharacterBuilder()

        fun rickSanchez() = CharacterBuilder()
            .withId(1)
            .withName("Rick Sanchez")
            .withStatus(CharacterStatus.ALIVE)
            .withGender(CharacterGender.MALE)

        fun mortySmith() = CharacterBuilder()
            .withId(2)
            .withName("Morty Smith")
            .withStatus(CharacterStatus.ALIVE)
            .withGender(CharacterGender.MALE)
            .withImage("https://rickandmortyapi.com/api/character/avatar/2.jpeg")
            .withUrl("https://rickandmortyapi.com/api/character/2")
            .withOrigin(CharacterLocationBuilder.anUnknownLocation().build())
            .withLocation(
                CharacterLocationBuilder.aCharacterLocation()
                    .withName("Earth (Replacement Dimension)")
                    .withUrl("https://rickandmortyapi.com/api/location/20")
                    .build()
            )
            .withCreated("2017-11-04T18:50:21.651Z")

        fun summerSmith() = CharacterBuilder()
            .withId(3)
            .withName("Summer Smith")
            .withStatus(CharacterStatus.ALIVE)
            .withGender(CharacterGender.FEMALE)
            .withImage("https://rickandmortyapi.com/api/character/avatar/3.jpeg")
            .withUrl("https://rickandmortyapi.com/api/character/3")
            .withCreated("2017-11-04T19:09:56.428Z")
    }
}