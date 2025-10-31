package com.infinitum.labs.rickandmorty_android.splash

import androidx.lifecycle.viewModelScope
import com.infinitum.labs.rickandmorty_android.common.viewmodel.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

internal class SplashViewModel : BaseViewModel<SplashWrapper.UiState, SplashWrapper.Event>(
    initialState = SplashWrapper.UiState()
) {

    init {
        navigateToMain()
    }

    private fun navigateToMain() {
        viewModelScope.launch {
            // Espera 2 segundos antes de navegar
            delay(2000)
            channelEvent.send(SplashWrapper.Event.NavigateToMain)
        }
    }

    override fun onStart() = Unit
}