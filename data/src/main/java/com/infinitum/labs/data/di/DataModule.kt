package com.infinitum.labs.data.di

import com.infinitum.labs.data.character.local.datasource.CharacterLocalDataSource
import com.infinitum.labs.data.character.local.datasource.CharacterLocalDataSourceImpl
import com.infinitum.labs.data.character.local.model.RealmCharacter
import com.infinitum.labs.data.character.remote.datasource.CharacterRemoteDataSource
import com.infinitum.labs.data.character.remote.datasource.CharacterRemoteDataSourceImpl
import com.infinitum.labs.data.character.repository.CharacterRepositoryImpl
import com.infinitum.labs.data.episode.local.datasource.EpisodeLocalDataSource
import com.infinitum.labs.data.episode.local.datasource.EpisodeLocalDataSourceImpl
import com.infinitum.labs.data.episode.local.model.RealmEpisode
import com.infinitum.labs.data.episode.remote.datasource.EpisodeRemoteDataSource
import com.infinitum.labs.data.episode.remote.datasource.EpisodeRemoteDataSourceImpl
import com.infinitum.labs.data.episode.repository.EpisodeRepositoryImpl
import com.infinitum.labs.data.location.local.datasource.LocationLocalDataSource
import com.infinitum.labs.data.location.local.datasource.LocationLocalDataSourceImpl
import com.infinitum.labs.data.location.local.model.RealmLocation
import com.infinitum.labs.data.location.remote.datasource.LocationRemoteDataSource
import com.infinitum.labs.data.location.remote.datasource.LocationRemoteDataSourceImpl
import com.infinitum.labs.data.location.repository.LocationRepositoryImpl
import com.infinitum.labs.domain.character.repository.CharacterRepository
import com.infinitum.labs.domain.episode.repository.EpisodeRepository
import com.infinitum.labs.domain.location.repository.LocationRepository
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule = module {
    single {
        val config = RealmConfiguration.Builder(
            schema = setOf(
                RealmCharacter::class,
                RealmLocation::class,
                RealmEpisode::class
            )
        )
            .name("rickandmorty.realm")
            .schemaVersion(1)
            .build()
        Realm.open(config)
    }

    singleOf(::CharacterRemoteDataSourceImpl) bind CharacterRemoteDataSource::class
    singleOf(::CharacterLocalDataSourceImpl) bind CharacterLocalDataSource::class
    singleOf(::CharacterRepositoryImpl) bind CharacterRepository::class

    singleOf(::LocationRemoteDataSourceImpl) bind LocationRemoteDataSource::class
    singleOf(::LocationLocalDataSourceImpl) bind LocationLocalDataSource::class
    singleOf(::LocationRepositoryImpl) bind LocationRepository::class

    singleOf(::EpisodeRemoteDataSourceImpl) bind EpisodeRemoteDataSource::class
    singleOf(::EpisodeLocalDataSourceImpl) bind EpisodeLocalDataSource::class
    singleOf(::EpisodeRepositoryImpl) bind EpisodeRepository::class
}