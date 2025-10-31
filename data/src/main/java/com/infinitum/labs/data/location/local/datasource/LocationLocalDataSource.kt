package com.infinitum.labs.data.location.local.datasource

import com.infinitum.labs.data.location.local.model.RealmLocation
import com.infinitum.labs.data.location.remote.dto.LocationDto
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal interface LocationLocalDataSource {
    suspend fun saveLocations(locations: List<LocationDto>, page: Int)
    suspend fun getLocations(page: Int): List<LocationDto>
    suspend fun getAllLocations(): List<LocationDto>
    suspend fun getLocation(id: Int): LocationDto?
    suspend fun clearAllLocations()
}

internal class LocationLocalDataSourceImpl(
    private val realm: Realm
) : LocationLocalDataSource {

    override suspend fun saveLocations(locations: List<LocationDto>, page: Int) {
        realm.write {
            locations.forEach { location ->
                val realmLocation = RealmLocation().apply {
                    id = location.id
                    name = location.name
                    type = location.type
                    dimension = location.dimension
                    residentsJson = Json.encodeToString(location.residents)
                    url = location.url
                    created = location.created
                    this.page = page
                    timestamp = System.currentTimeMillis()
                }
                copyToRealm(realmLocation, updatePolicy = io.realm.kotlin.UpdatePolicy.ALL)
            }
        }
    }

    override suspend fun getLocations(page: Int): List<LocationDto> {
        return realm.query<RealmLocation>("page == $0", page)
            .find()
            .map { it.toDto() }
    }

    override suspend fun getAllLocations(): List<LocationDto> {
        return realm.query<RealmLocation>()
            .find()
            .map { it.toDto() }
    }

    override suspend fun getLocation(id: Int): LocationDto? {
        return realm.query<RealmLocation>("id == $0", id)
            .first()
            .find()
            ?.toDto()
    }

    override suspend fun clearAllLocations() {
        realm.write {
            val locations = query<RealmLocation>().find()
            delete(locations)
        }
    }

    private fun RealmLocation.toDto(): LocationDto {
        return LocationDto(
            id = id,
            name = name,
            type = type,
            dimension = dimension,
            residents = Json.decodeFromString(residentsJson),
            url = url,
            created = created
        )
    }
}