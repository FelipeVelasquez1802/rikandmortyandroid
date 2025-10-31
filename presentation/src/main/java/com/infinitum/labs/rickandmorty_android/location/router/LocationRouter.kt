package com.infinitum.labs.rickandmorty_android.location.router

import com.infinitum.labs.rickandmorty_android.common.router.BaseRouter

internal sealed class LocationRouter : BaseRouter {
    data class NavigateToDetail(val locationId: Int) : LocationRouter()
    data object NavigateBack : LocationRouter()
}