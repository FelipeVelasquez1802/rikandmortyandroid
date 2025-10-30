package com.infinitum.labs.data.di

import com.infinitum.labs.data.character.remote.datasource.CharacterRemoteDataSource
import com.infinitum.labs.data.character.remote.datasource.CharacterRemoteDataSourceImpl
import com.infinitum.labs.data.character.repository.CharacterRepositoryImpl
import com.infinitum.labs.data.episode.remote.datasource.EpisodeRemoteDataSource
import com.infinitum.labs.data.episode.remote.datasource.EpisodeRemoteDataSourceImpl
import com.infinitum.labs.data.episode.repository.EpisodeRepositoryImpl
import com.infinitum.labs.data.location.remote.datasource.LocationRemoteDataSource
import com.infinitum.labs.data.location.remote.datasource.LocationRemoteDataSourceImpl
import com.infinitum.labs.data.location.repository.LocationRepositoryImpl
import com.infinitum.labs.domain.character.repository.CharacterRepository
import com.infinitum.labs.domain.episode.repository.EpisodeRepository
import com.infinitum.labs.domain.location.repository.LocationRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule = module {
    // Character
    singleOf(::CharacterRemoteDataSourceImpl) bind CharacterRemoteDataSource::class
    singleOf(::CharacterRepositoryImpl) bind CharacterRepository::class

    // Location
    singleOf(::LocationRemoteDataSourceImpl) bind LocationRemoteDataSource::class
    singleOf(::LocationRepositoryImpl) bind LocationRepository::class

    // Episode
    singleOf(::EpisodeRemoteDataSourceImpl) bind EpisodeRemoteDataSource::class
    singleOf(::EpisodeRepositoryImpl) bind EpisodeRepository::class
}