package com.infinitum.labs.data.character.local.datasource

import com.infinitum.labs.data.character.local.model.RealmCharacter
import com.infinitum.labs.data.character.remote.dto.CharacterDto
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal interface CharacterLocalDataSource {
    suspend fun saveCharacters(characters: List<CharacterDto>, page: Int)
    suspend fun getCharacters(page: Int): List<CharacterDto>
    suspend fun getAllCharacters(): List<CharacterDto>
    suspend fun getCharacter(id: Int): CharacterDto?
    suspend fun clearAllCharacters()
}

internal class CharacterLocalDataSourceImpl(
    private val realm: Realm
) : CharacterLocalDataSource {

    override suspend fun saveCharacters(characters: List<CharacterDto>, page: Int) {
        realm.write {
            characters.forEach { character ->
                val realmCharacter = RealmCharacter().apply {
                    id = character.id
                    name = character.name
                    status = character.status
                    species = character.species
                    type = character.type
                    gender = character.gender
                    originName = character.origin.name
                    originUrl = character.origin.url
                    locationName = character.location.name
                    locationUrl = character.location.url
                    image = character.image
                    episodesJson = Json.encodeToString(character.episode)
                    url = character.url
                    created = character.created
                    this.page = page
                    timestamp = System.currentTimeMillis()
                }
                copyToRealm(realmCharacter, updatePolicy = io.realm.kotlin.UpdatePolicy.ALL)
            }
        }
    }

    override suspend fun getCharacters(page: Int): List<CharacterDto> {
        return realm.query<RealmCharacter>("page == $0", page)
            .find()
            .map { it.toDto() }
    }

    override suspend fun getAllCharacters(): List<CharacterDto> {
        return realm.query<RealmCharacter>()
            .find()
            .map { it.toDto() }
    }

    override suspend fun getCharacter(id: Int): CharacterDto? {
        return realm.query<RealmCharacter>("id == $0", id)
            .first()
            .find()
            ?.toDto()
    }

    override suspend fun clearAllCharacters() {
        realm.write {
            val characters = query<RealmCharacter>().find()
            delete(characters)
        }
    }

    private fun RealmCharacter.toDto(): CharacterDto {
        return CharacterDto(
            id = id,
            name = name,
            status = status,
            species = species,
            type = type,
            gender = gender,
            origin = com.infinitum.labs.data.character.remote.dto.CharacterLocationDto(
                name = originName,
                url = originUrl
            ),
            location = com.infinitum.labs.data.character.remote.dto.CharacterLocationDto(
                name = locationName,
                url = locationUrl
            ),
            image = image,
            episode = Json.decodeFromString(episodesJson),
            url = url,
            created = created
        )
    }
}