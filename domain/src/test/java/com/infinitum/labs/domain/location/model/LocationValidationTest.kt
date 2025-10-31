package com.infinitum.labs.domain.location.model

import com.infinitum.labs.domain.location.exception.LocationException
import com.infinitum.labs.domain.location.model.builder.LocationBuilder
import org.junit.Assert.assertEquals
import org.junit.Test

class LocationValidationTest {

    // ========== ID VALIDATION TESTS ==========

    @Test
    fun `given valid positive ID when creating location then creates location successfully`() {
        val location = LocationBuilder.aLocation().withId(1).build()

        assertEquals(1, location.id)
    }

    @Test(expected = LocationException.InvalidLocationId::class)
    fun `given zero ID when creating location then throws InvalidLocationId exception`() {
        LocationBuilder.aLocation().withId(0).build()
    }

    @Test(expected = LocationException.InvalidLocationId::class)
    fun `given negative ID when creating location then throws InvalidLocationId exception`() {
        LocationBuilder.aLocation().withId(-1).build()
    }

    @Test
    fun `given large positive ID when creating location then creates location successfully`() {
        val location = LocationBuilder.aLocation().withId(999999).build()

        assertEquals(999999, location.id)
    }

    // ========== NAME VALIDATION TESTS ==========

    @Test
    fun `given valid name when creating location then creates location successfully`() {
        val location = LocationBuilder.aLocation().withName("Earth (C-137)").build()

        assertEquals("Earth (C-137)", location.name)
    }

    @Test(expected = LocationException.InvalidLocationName::class)
    fun `given blank name when creating location then throws InvalidLocationName exception`() {
        LocationBuilder.aLocation().withName("").build()
    }

    @Test(expected = LocationException.InvalidLocationName::class)
    fun `given whitespace name when creating location then throws InvalidLocationName exception`() {
        LocationBuilder.aLocation().withName("   ").build()
    }

    @Test
    fun `given long name when creating location then creates location successfully`() {
        val longName = "A".repeat(100)
        val location = LocationBuilder.aLocation().withName(longName).build()

        assertEquals(longName, location.name)
    }

    @Test
    fun `given name with special characters when creating location then creates location successfully`() {
        val location = LocationBuilder.aLocation().withName("Earth (C-137) !@#$%").build()

        assertEquals("Earth (C-137) !@#$%", location.name)
    }

    // ========== TYPE VALIDATION TESTS ==========

    @Test
    fun `given valid type when creating location then creates location successfully`() {
        val location = LocationBuilder.aLocation().withType("Planet").build()

        assertEquals("Planet", location.type)
    }

    @Test(expected = LocationException.InvalidLocationType::class)
    fun `given blank type when creating location then throws InvalidLocationType exception`() {
        LocationBuilder.aLocation().withType("").build()
    }

    @Test(expected = LocationException.InvalidLocationType::class)
    fun `given whitespace type when creating location then throws InvalidLocationType exception`() {
        LocationBuilder.aLocation().withType("   ").build()
    }

    @Test
    fun `given different types when creating locations then creates locations successfully`() {
        val planet = LocationBuilder.aLocation().withType("Planet").build()
        val spaceStation = LocationBuilder.aLocation().withType("Space station").build()
        val cluster = LocationBuilder.aLocation().withType("Cluster").build()
        val microverse = LocationBuilder.aLocation().withType("Microverse").build()

        assertEquals("Planet", planet.type)
        assertEquals("Space station", spaceStation.type)
        assertEquals("Cluster", cluster.type)
        assertEquals("Microverse", microverse.type)
    }

    // ========== DIMENSION VALIDATION TESTS ==========

    @Test
    fun `given valid dimension when creating location then creates location successfully`() {
        val location = LocationBuilder.aLocation().withDimension("Dimension C-137").build()

        assertEquals("Dimension C-137", location.dimension)
    }

    @Test(expected = LocationException.InvalidLocationDimension::class)
    fun `given blank dimension when creating location then throws InvalidLocationDimension exception`() {
        LocationBuilder.aLocation().withDimension("").build()
    }

    @Test(expected = LocationException.InvalidLocationDimension::class)
    fun `given whitespace dimension when creating location then throws InvalidLocationDimension exception`() {
        LocationBuilder.aLocation().withDimension("   ").build()
    }

    @Test
    fun `given unknown dimension when creating location then creates location successfully`() {
        val location = LocationBuilder.aLocation().withDimension("unknown").build()

        assertEquals("unknown", location.dimension)
    }

    @Test
    fun `given replacement dimension when creating location then creates location successfully`() {
        val location = LocationBuilder.aLocation().withDimension("Replacement Dimension").build()

        assertEquals("Replacement Dimension", location.dimension)
    }

    // ========== URL VALIDATION TESTS ==========

    @Test
    fun `given valid HTTP URL when creating location then creates location successfully`() {
        val location = LocationBuilder.aLocation()
            .withUrl("http://rickandmortyapi.com/api/location/1")
            .build()

        assertEquals("http://rickandmortyapi.com/api/location/1", location.url)
    }

    @Test
    fun `given valid HTTPS URL when creating location then creates location successfully`() {
        val location = LocationBuilder.aLocation()
            .withUrl("https://rickandmortyapi.com/api/location/1")
            .build()

        assertEquals("https://rickandmortyapi.com/api/location/1", location.url)
    }

    @Test(expected = LocationException.InvalidLocationUrl::class)
    fun `given blank URL when creating location then throws InvalidLocationUrl exception`() {
        LocationBuilder.aLocation().withUrl("").build()
    }

    @Test(expected = LocationException.InvalidLocationUrl::class)
    fun `given whitespace URL when creating location then throws InvalidLocationUrl exception`() {
        LocationBuilder.aLocation().withUrl("   ").build()
    }

    @Test(expected = LocationException.InvalidLocationUrl::class)
    fun `given URL without protocol when creating location then throws InvalidLocationUrl exception`() {
        LocationBuilder.aLocation().withUrl("rickandmortyapi.com/api/location/1").build()
    }

    @Test(expected = LocationException.InvalidLocationUrl::class)
    fun `given invalid URL protocol when creating location then throws InvalidLocationUrl exception`() {
        LocationBuilder.aLocation().withUrl("ftp://rickandmortyapi.com/api/location/1").build()
    }

    // ========== CREATED DATE VALIDATION TESTS ==========

    @Test
    fun `given valid ISO-8601 date when creating location then creates location successfully`() {
        val location = LocationBuilder.aLocation()
            .withCreated("2017-11-10T12:42:04.162Z")
            .build()

        assertEquals("2017-11-10T12:42:04.162Z", location.created)
    }

    @Test(expected = LocationException.InvalidLocationCreatedDate::class)
    fun `given blank created date when creating location then throws InvalidLocationCreatedDate exception`() {
        LocationBuilder.aLocation().withCreated("").build()
    }

    @Test(expected = LocationException.InvalidLocationCreatedDate::class)
    fun `given whitespace created date when creating location then throws InvalidLocationCreatedDate exception`() {
        LocationBuilder.aLocation().withCreated("   ").build()
    }

    @Test
    fun `given different date format when creating location then creates location successfully`() {
        val location = LocationBuilder.aLocation()
            .withCreated("2023-12-25T00:00:00.000Z")
            .build()

        assertEquals("2023-12-25T00:00:00.000Z", location.created)
    }

    // ========== RESIDENTS VALIDATION TESTS ==========

    @Test
    fun `given empty residents list when creating location then creates location successfully`() {
        val location = LocationBuilder.aLocation()
            .withResidents(emptyList())
            .build()

        assertEquals(0, location.residents.size)
    }

    @Test
    fun `given single resident when creating location then creates location successfully`() {
        val residents = listOf("https://rickandmortyapi.com/api/character/1")
        val location = LocationBuilder.aLocation()
            .withResidents(residents)
            .build()

        assertEquals(1, location.residents.size)
        assertEquals(residents, location.residents)
    }

    @Test
    fun `given multiple residents when creating location then creates location successfully`() {
        val residents = listOf(
            "https://rickandmortyapi.com/api/character/1",
            "https://rickandmortyapi.com/api/character/2",
            "https://rickandmortyapi.com/api/character/3"
        )
        val location = LocationBuilder.aLocation()
            .withResidents(residents)
            .build()

        assertEquals(3, location.residents.size)
        assertEquals(residents, location.residents)
    }
}