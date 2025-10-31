package com.infinitum.labs.data.episode.local.datasource

import com.infinitum.labs.data.episode.local.model.RealmEpisode
import com.infinitum.labs.data.episode.remote.dto.EpisodeDto
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal interface EpisodeLocalDataSource {
    suspend fun saveEpisodes(episodes: List<EpisodeDto>, page: Int)
    suspend fun getEpisodes(page: Int): List<EpisodeDto>
    suspend fun getAllEpisodes(): List<EpisodeDto>
    suspend fun getEpisode(id: Int): EpisodeDto?
    suspend fun clearAllEpisodes()
}

internal class EpisodeLocalDataSourceImpl(
    private val realm: Realm
) : EpisodeLocalDataSource {

    override suspend fun saveEpisodes(episodes: List<EpisodeDto>, page: Int) {
        realm.write {
            episodes.forEach { episode ->
                val realmEpisode = RealmEpisode().apply {
                    id = episode.id
                    name = episode.name
                    airDate = episode.airDate
                    this.episode = episode.episode
                    charactersJson = Json.encodeToString(episode.characters)
                    url = episode.url
                    created = episode.created
                    this.page = page
                    timestamp = System.currentTimeMillis()
                }
                copyToRealm(realmEpisode, updatePolicy = io.realm.kotlin.UpdatePolicy.ALL)
            }
        }
    }

    override suspend fun getEpisodes(page: Int): List<EpisodeDto> {
        return realm.query<RealmEpisode>("page == $0", page)
            .find()
            .map { it.toDto() }
    }

    override suspend fun getAllEpisodes(): List<EpisodeDto> {
        return realm.query<RealmEpisode>()
            .find()
            .map { it.toDto() }
    }

    override suspend fun getEpisode(id: Int): EpisodeDto? {
        return realm.query<RealmEpisode>("id == $0", id)
            .first()
            .find()
            ?.toDto()
    }

    override suspend fun clearAllEpisodes() {
        realm.write {
            val episodes = query<RealmEpisode>().find()
            delete(episodes)
        }
    }

    private fun RealmEpisode.toDto(): EpisodeDto {
        return EpisodeDto(
            id = id,
            name = name,
            airDate = airDate,
            episode = episode,
            characters = Json.decodeFromString(charactersJson),
            url = url,
            created = created
        )
    }
}