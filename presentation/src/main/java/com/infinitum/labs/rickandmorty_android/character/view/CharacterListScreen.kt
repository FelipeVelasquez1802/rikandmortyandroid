package com.infinitum.labs.rickandmorty_android.character.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.infinitum.labs.rickandmorty_android.character.components.CharacterCard
import com.infinitum.labs.rickandmorty_android.character.list.CharacterListViewModel
import com.infinitum.labs.rickandmorty_android.character.router.CharacterRouter
import com.infinitum.labs.rickandmorty_android.character.state.CharacterListWrapper
import kotlinx.coroutines.flow.receiveAsFlow
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CharacterListScreen(
    onGoTo: (CharacterRouter) -> Unit = {},
) {
    val viewModel: CharacterListViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    // Collect one-time events (navigation, snackbars, etc.)
    LaunchedEffect(Unit) {
        viewModel.channel.receiveAsFlow().collect { event ->
            when (event) {
                is CharacterListWrapper.Event.NavigateToDetail -> {
                    onGoTo(CharacterRouter.NavigateToDetail(event.characterId))
                }
                is CharacterListWrapper.Event.ShowError -> {
                    // Could show a snackbar here
                }
                // User interaction events are handled in ViewModel
                CharacterListWrapper.Event.Retry,
                CharacterListWrapper.Event.LoadNextPage,
                is CharacterListWrapper.Event.OnCharacterClick -> { /* Handled in ViewModel */ }
            }
        }
    }

    CharacterListContent(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CharacterListContent(
    state: CharacterListWrapper.UiState,
    onEvent: (CharacterListWrapper.Event) -> Unit
) {
    val listState = rememberLazyListState()

    // Paginación arreglada: observa el scroll y carga más items
    LaunchedEffect(listState, state.canLoadMore, state.isLoading) {
        snapshotFlow {
            val layoutInfo = listState.layoutInfo
            val totalItemsCount = layoutInfo.totalItemsCount
            val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0

            // Carga cuando estamos a 3 items del final
            lastVisibleItemIndex >= totalItemsCount - 3
        }
            .collect { shouldLoadMore ->
                if (shouldLoadMore && state.canLoadMore && !state.isLoading) {
                    onEvent(CharacterListWrapper.Event.LoadNextPage)
                }
            }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Rick and Morty") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                state.error != null && state.characters.isEmpty() -> {
                    ErrorState(
                        message = state.error,
                        onRetry = { onEvent(CharacterListWrapper.Event.Retry) },
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                state.isLoading && state.characters.isEmpty() -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                else -> {
                    LazyColumn(
                        state = listState,
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        items(
                            items = state.characters,
                            key = { it.id }
                        ) { character ->
                            CharacterCard(
                                character = character,
                                onClick = { characterId ->
                                    onEvent(CharacterListWrapper.Event.OnCharacterClick(characterId))
                                }
                            )
                        }

                        // Loading indicator at bottom for pagination
                        if (state.isLoading && state.characters.isNotEmpty()) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ErrorState(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.error
            )
            Button(onClick = onRetry) {
                Text("Retry")
            }
        }
    }
}

@Preview
@Composable
internal fun CharacterListPreview() {
    CharacterListScreen(
        onGoTo = {}
    )
}
