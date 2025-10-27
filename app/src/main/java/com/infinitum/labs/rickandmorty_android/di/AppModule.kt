package com.infinitum.labs.rickandmorty_android.di

import com.infinitum.labs.rickandmorty_android.list.CharacterListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // ViewModels
    viewModel { CharacterListViewModel(get()) }
}