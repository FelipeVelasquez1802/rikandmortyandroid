package com.infinitum.labs.domain.location.model.builder

import com.infinitum.labs.domain.location.model.Location

/**
 * Builder for creating Location test instances.
 * Provides sensible defaults and convenient factory methods for common locations.
 *
 * Usage:
 * ```
 * val location = LocationBuilder.aLocation().withName("Earth").build()
 * val earth = LocationBuilder.earthC137()
 * ```
 */
class LocationBuilder {
    private var id: Int = 1
    private var name: String = "Earth (C-137)"
    private var type: String = "Planet"
    private var dimension: String = "Dimension C-137"
    private var residents: List<String> = listOf(
        "https://rickandmortyapi.com/api/character/1",
        "https://rickandmortyapi.com/api/character/2"
    )
    private var url: String = "https://rickandmortyapi.com/api/location/1"
    private var created: String = "2017-11-10T12:42:04.162Z"

    fun withId(id: Int) = apply { this.id = id }

    fun withName(name: String) = apply { this.name = name }

    fun withType(type: String) = apply { this.type = type }

    fun withDimension(dimension: String) = apply { this.dimension = dimension }

    fun withResidents(residents: List<String>) = apply { this.residents = residents }

    fun withUrl(url: String) = apply { this.url = url }

    fun withCreated(created: String) = apply { this.created = created }

    fun build(): Location {
        return Location(
            id = id,
            name = name,
            type = type,
            dimension = dimension,
            residents = residents,
            url = url,
            created = created
        )
    }

    companion object {
        /**
         * Creates a basic location builder with default values.
         */
        fun aLocation() = LocationBuilder()

        /**
         * Creates Earth (C-137) - The home dimension of the original Rick and Morty.
         */
        fun earthC137() = LocationBuilder()
            .withId(1)
            .withName("Earth (C-137)")
            .withType("Planet")
            .withDimension("Dimension C-137")
            .withResidents(
                listOf(
                    "https://rickandmortyapi.com/api/character/1",
                    "https://rickandmortyapi.com/api/character/2"
                )
            )
            .withUrl("https://rickandmortyapi.com/api/location/1")
            .withCreated("2017-11-10T12:42:04.162Z")

        /**
         * Creates Citadel of Ricks - The secret headquarters for the Council of Ricks.
         */
        fun citadelOfRicks() = LocationBuilder()
            .withId(3)
            .withName("Citadel of Ricks")
            .withType("Space station")
            .withDimension("unknown")
            .withResidents(emptyList())
            .withUrl("https://rickandmortyapi.com/api/location/3")
            .withCreated("2017-11-10T13:08:13.191Z")

        /**
         * Creates Planet Squanch - Squanchy's home planet.
         */
        fun planetSquanch() = LocationBuilder()
            .withId(35)
            .withName("Planet Squanch")
            .withType("Planet")
            .withDimension("Replacement Dimension")
            .withResidents(emptyList())
            .withUrl("https://rickandmortyapi.com/api/location/35")
            .withCreated("2017-11-18T11:40:26.310Z")

        /**
         * Creates an unknown location with minimal data.
         */
        fun unknownLocation() = LocationBuilder()
            .withId(99)
            .withName("unknown")
            .withType("unknown")
            .withDimension("unknown")
            .withResidents(emptyList())
            .withUrl("https://rickandmortyapi.com/api/location/99")
    }
}