package com.infinitum.labs.domain.location.model

import com.infinitum.labs.domain.location.model.builder.LocationBuilder
import org.junit.Assert.assertEquals
import org.junit.Test

class LocationTest {

    @Test
    fun `given all valid properties when creating location then returns location with correct values`() {
        val residents = listOf(
            "https://rickandmortyapi.com/api/character/1",
            "https://rickandmortyapi.com/api/character/2"
        )

        val location = LocationBuilder.earthC137()
            .withResidents(residents)
            .build()

        assertEquals(1, location.id)
        assertEquals("Earth (C-137)", location.name)
        assertEquals("Planet", location.type)
        assertEquals("Dimension C-137", location.dimension)
        assertEquals(residents, location.residents)
        assertEquals("https://rickandmortyapi.com/api/location/1", location.url)
        assertEquals("2017-11-10T12:42:04.162Z", location.created)
    }

    @Test
    fun `given citadel of ricks when creating location then returns location with space station type`() {
        val location = LocationBuilder.citadelOfRicks().build()

        assertEquals(3, location.id)
        assertEquals("Citadel of Ricks", location.name)
        assertEquals("Space station", location.type)
        assertEquals("unknown", location.dimension)
    }

    @Test
    fun `given planet squanch when creating location then returns location with planet type`() {
        val location = LocationBuilder.planetSquanch().build()

        assertEquals(35, location.id)
        assertEquals("Planet Squanch", location.name)
        assertEquals("Planet", location.type)
        assertEquals("Replacement Dimension", location.dimension)
    }

    @Test
    fun `given location with empty residents when creating location then returns location with empty list`() {
        val location = LocationBuilder.aLocation()
            .withResidents(emptyList())
            .build()

        assertEquals(emptyList<String>(), location.residents)
    }

    @Test
    fun `given location with multiple residents when creating location then returns location with all residents`() {
        val residents = listOf(
            "https://rickandmortyapi.com/api/character/1",
            "https://rickandmortyapi.com/api/character/2",
            "https://rickandmortyapi.com/api/character/3",
            "https://rickandmortyapi.com/api/character/4"
        )

        val location = LocationBuilder.aLocation()
            .withResidents(residents)
            .build()

        assertEquals(4, location.residents.size)
        assertEquals(residents, location.residents)
    }

    @Test
    fun `given different location types when creating locations then returns locations with correct types`() {
        val planet = LocationBuilder.aLocation().withType("Planet").build()
        val spaceStation = LocationBuilder.aLocation().withType("Space station").build()
        val cluster = LocationBuilder.aLocation().withType("Cluster").build()
        val microverse = LocationBuilder.aLocation().withType("Microverse").build()

        assertEquals("Planet", planet.type)
        assertEquals("Space station", spaceStation.type)
        assertEquals("Cluster", cluster.type)
        assertEquals("Microverse", microverse.type)
    }

    @Test
    fun `given different dimensions when creating locations then returns locations with correct dimensions`() {
        val c137 = LocationBuilder.aLocation().withDimension("Dimension C-137").build()
        val replacement = LocationBuilder.aLocation().withDimension("Replacement Dimension").build()
        val unknown = LocationBuilder.aLocation().withDimension("unknown").build()

        assertEquals("Dimension C-137", c137.dimension)
        assertEquals("Replacement Dimension", replacement.dimension)
        assertEquals("unknown", unknown.dimension)
    }
}