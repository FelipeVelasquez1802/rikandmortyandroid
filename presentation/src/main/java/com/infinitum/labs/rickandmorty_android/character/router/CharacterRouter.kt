package com.infinitum.labs.rickandmorty_android.character.router

import com.infinitum.labs.rickandmorty_android.common.router.BaseRouter

internal sealed class CharacterRouter : BaseRouter {
    data class NavigateToDetail(val characterId: Int) : CharacterRouter()
    data object NavigateBack : CharacterRouter()
}
