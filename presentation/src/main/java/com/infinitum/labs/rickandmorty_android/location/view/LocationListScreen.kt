package com.infinitum.labs.rickandmorty_android.location.view

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.infinitum.labs.rickandmorty_android.location.components.LocationCard
import com.infinitum.labs.rickandmorty_android.location.list.LocationListViewModel
import com.infinitum.labs.rickandmorty_android.location.router.LocationRouter
import com.infinitum.labs.rickandmorty_android.location.state.LocationListWrapper
import kotlinx.coroutines.flow.receiveAsFlow
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LocationListScreen(
    onGoTo: (LocationRouter) -> Unit = {},
) {
    val viewModel: LocationListViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    // Collect one-time events (navigation, snackbars, etc.)
    LaunchedEffect(Unit) {
        viewModel.channel.receiveAsFlow().collect { event ->
            when (event) {
                is LocationListWrapper.Event.NavigateToDetail -> {
                    onGoTo(LocationRouter.NavigateToDetail(event.locationId))
                }
                is LocationListWrapper.Event.ShowError -> {
                    // Could show a snackbar here
                }
                // User interaction events are handled in ViewModel
                LocationListWrapper.Event.Retry,
                LocationListWrapper.Event.LoadNextPage,
                is LocationListWrapper.Event.OnLocationClick -> { /* Handled in ViewModel */ }
            }
        }
    }

    LocationListContent(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LocationListContent(
    state: LocationListWrapper.UiState,
    onEvent: (LocationListWrapper.Event) -> Unit
) {
    val listState = rememberLazyListState()

    // Pagination: observe scroll and load more items
    LaunchedEffect(listState, state.canLoadMore, state.isLoading) {
        snapshotFlow {
            val layoutInfo = listState.layoutInfo
            val totalItemsCount = layoutInfo.totalItemsCount
            val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0

            // Load when we are 3 items from the end
            lastVisibleItemIndex >= totalItemsCount - 3
        }
            .collect { shouldLoadMore ->
                if (shouldLoadMore && state.canLoadMore && !state.isLoading) {
                    onEvent(LocationListWrapper.Event.LoadNextPage)
                }
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Locations") },
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
                state.error != null && state.locations.isEmpty() -> {
                    ErrorState(
                        message = state.error,
                        onRetry = { onEvent(LocationListWrapper.Event.Retry) },
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                state.isLoading && state.locations.isEmpty() -> {
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
                            items = state.locations,
                            key = { it.id }
                        ) { location ->
                            LocationCard(
                                location = location,
                                onClick = { locationId ->
                                    onEvent(LocationListWrapper.Event.OnLocationClick(locationId))
                                }
                            )
                        }

                        // Loading indicator at bottom for pagination
                        if (state.isLoading && state.locations.isNotEmpty()) {
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
internal fun LocationListPreview() {
    LocationListScreen(
        onGoTo = {}
    )
}