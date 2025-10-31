package com.infinitum.labs.rickandmorty_android.common.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

internal abstract class BaseViewModel<StateType, EventType>(
    initialState: StateType
) : ViewModel() {
    protected val _state: MutableStateFlow<StateType> =
        MutableStateFlow(initialState)
    val state: StateFlow<StateType> = _state
        .onStart {
            onStart()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = initialState,
        )

    protected val channelEvent: Channel<EventType> = Channel()
    val channel: ReceiveChannel<EventType> = channelEvent

    protected abstract fun onStart()
}