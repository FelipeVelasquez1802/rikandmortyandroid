package com.infinitum.labs.rickandmorty_android.di

import com.infinitum.labs.domain.character.usecase.GetCharacterByIdUseCase
import com.infinitum.labs.domain.character.usecase.GetCharactersUseCase
import com.infinitum.labs.domain.character.usecase.SearchCharactersByNameUseCase
import com.infinitum.labs.domain.episode.usecase.GetEpisodeByIdUseCase
import com.infinitum.labs.domain.episode.usecase.GetEpisodesUseCase
import com.infinitum.labs.domain.episode.usecase.SearchEpisodesByNameUseCase
import com.infinitum.labs.domain.location.usecase.GetLocationByIdUseCase
import com.infinitum.labs.domain.location.usecase.GetLocationsUseCase
import com.infinitum.labs.domain.location.usecase.SearchLocationsByNameUseCase
import com.infinitum.labs.rickandmorty_android.character.detail.CharacterDetailViewModel
import com.infinitum.labs.rickandmorty_android.character.list.CharacterListViewModel
import com.infinitum.labs.rickandmorty_android.episode.detail.EpisodeDetailViewModel
import com.infinitum.labs.rickandmorty_android.episode.list.EpisodeListViewModel
import com.infinitum.labs.rickandmorty_android.location.detail.LocationDetailViewModel
import com.infinitum.labs.rickandmorty_android.location.list.LocationListViewModel
import com.infinitum.labs.rickandmorty_android.splash.SplashViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    // Character Use Cases
    singleOf(::GetCharactersUseCase)
    singleOf(::GetCharacterByIdUseCase)
    singleOf(::SearchCharactersByNameUseCase)

    // Location Use Cases
    singleOf(::GetLocationsUseCase)
    singleOf(::GetLocationByIdUseCase)
    singleOf(::SearchLocationsByNameUseCase)

    // Episode Use Cases
    singleOf(::GetEpisodesUseCase)
    singleOf(::GetEpisodeByIdUseCase)
    singleOf(::SearchEpisodesByNameUseCase)

    // ViewModels
    viewModelOf(::SplashViewModel)
    viewModelOf(::CharacterListViewModel)
    viewModelOf(::LocationListViewModel)
    viewModelOf(::EpisodeListViewModel)

    // CharacterDetailViewModel with parameters
    viewModel { (characterId: Int) ->
        CharacterDetailViewModel(
            characterId = characterId,
            getCharacterByIdUseCase = get()
        )
    }

    // LocationDetailViewModel with parameters
    viewModel { (locationId: Int) ->
        LocationDetailViewModel(
            locationId = locationId,
            getLocationByIdUseCase = get()
        )
    }

    // EpisodeDetailViewModel with parameters
    viewModel { (episodeId: Int) ->
        EpisodeDetailViewModel(
            episodeId = episodeId,
            getEpisodeByIdUseCase = get()
        )
    }
}