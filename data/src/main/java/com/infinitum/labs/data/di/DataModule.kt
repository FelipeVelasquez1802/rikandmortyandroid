package com.infinitum.labs.data.di

import com.infinitum.labs.data.character.remote.datasource.CharacterRemoteDataSource
import com.infinitum.labs.data.character.remote.datasource.CharacterRemoteDataSourceImpl
import com.infinitum.labs.data.character.repository.CharacterRepositoryImpl
import com.infinitum.labs.domain.character.repository.CharacterRepository
import org.koin.dsl.module

val dataModule = module {
    // Remote Data Sources
    single<CharacterRemoteDataSource> { CharacterRemoteDataSourceImpl(get()) }

    // Repositories
    single<CharacterRepository> { CharacterRepositoryImpl(get()) }
}