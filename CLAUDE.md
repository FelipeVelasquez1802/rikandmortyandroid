# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Rick and Morty Android app - A Kotlin-based Android application built with Jetpack Compose and Material3 design. The project follows **Clean Architecture** with a multi-module structure separating concerns into distinct layers.

**Base Package**: `com.infinitum.labs`

## Build Configuration

- **Min SDK**: 24
- **Target SDK**: 36
- **Compile SDK**: 36
- **Java Version**: 11
- **Kotlin Version**: 2.0.21
- **AGP Version**: 8.13.0

## Common Commands

### Building
```bash
./gradlew build                    # Build all modules
./gradlew assembleDebug            # Build debug APK
./gradlew assembleRelease          # Build release APK
./gradlew clean                    # Clean build artifacts
./gradlew :app:build               # Build only app module
./gradlew :domain:build            # Build only domain module
./gradlew :data:build              # Build only data module
```

### Running
```bash
./gradlew installDebug             # Install debug APK on connected device
```

### Testing
```bash
./gradlew test                     # Run all unit tests (all modules)
./gradlew :app:test                # Run app module unit tests
./gradlew :data:test               # Run data module unit tests
./gradlew connectedAndroidTest     # Run instrumented tests on device/emulator

# Run specific test class
./gradlew test --tests com.infinitum.labs.rickandmorty_android.ExampleUnitTest

# Run specific test method
./gradlew test --tests "*.ExampleUnitTest.addition_isCorrect"
```

### Linting & Code Quality
```bash
./gradlew lint                     # Run Android lint checks
./gradlew lintDebug                # Run lint on debug variant
./gradlew :app:lint                # Lint only app module
./gradlew :data:lint               # Lint only data module
```

## Architecture

### Multi-Module Clean Architecture

The project is structured into three modules following Clean Architecture principles:

1. **`:app`** - Presentation Layer (Android Application Module)
   - Package: `com.infinitum.labs.rickandmorty_android`
   - Type: `com.android.application`
   - Dependencies: `:domain`, `:data`, Jetpack Compose, Material3
   - Responsibilities: UI, ViewModels, Compose screens, theme, navigation

2. **`:domain`** - Business Logic Layer (Pure Kotlin Module)
   - Package: `com.infinitum.labs.domain`
   - Type: `java-library` (no Android dependencies)
   - Dependencies: None (pure Kotlin)
   - Responsibilities: Use cases, business rules, domain models, repository interfaces

3. **`:data`** - Data Layer (Android Library Module)
   - Package: `com.infinitum.labs.data`
   - Type: `com.android.library`
   - Dependencies: `:domain`, AndroidX libraries
   - Responsibilities: Repository implementations, data sources (API, database), DTOs, mappers

### Dependency Flow
```
:app → :domain
:app → :data
:data → :domain
```

The `:domain` module has no dependencies, ensuring business logic remains platform-independent.

### Project Structure
```
├── presentation/                         # Presentation layer (Android app)
│   └── src/
│       ├── main/
│       │   ├── java/com/infinitum/labs/rickandmorty_android/
│       │   │   ├── MainActivity.kt       # Main entry point
│       │   │   ├── RickAndMortyApplication.kt  # Application class
│       │   │   ├── character/            # Character feature
│       │   │   │   └── list/             # Character list screen
│       │   │   │       └── CharacterListViewModel.kt
│       │   │   ├── di/                   # Dependency injection
│       │   │   │   └── AppModule.kt      # Koin modules
│       │   │   └── ui/theme/             # Material3 theme
│       │   │       ├── Color.kt          # Color palette
│       │   │       ├── Theme.kt          # Theme setup
│       │   │       └── Type.kt           # Typography
│       │   └── AndroidManifest.xml
│       ├── test/                         # Unit tests
│       └── androidTest/                  # Instrumented tests
│
├── domain/                               # Business logic layer (pure Kotlin)
│   └── src/
│       ├── main/java/com/infinitum/labs/domain/
│       │   ├── common/                   # Shared domain components
│       │   │   └── exception/            # Base exception hierarchy
│       │   │       └── DomainException.kt
│       │   └── character/                # Character bounded context
│       │       ├── exception/            # Character-specific exceptions
│       │       │   └── CharacterException.kt
│       │       ├── model/                # Domain models
│       │       │   ├── Character.kt      # Self-validating aggregate
│       │       │   ├── CharacterLocation.kt
│       │       │   ├── CharacterStatus.kt
│       │       │   └── CharacterGender.kt
│       │       ├── repository/           # Repository interfaces
│       │       │   └── CharacterRepository.kt
│       │       └── usecase/              # Use cases
│       │           ├── GetCharactersUseCase.kt
│       │           ├── GetCharacterByIdUseCase.kt
│       │           └── SearchCharactersByNameUseCase.kt
│       └── test/                         # Unit tests (80+ tests)
│
├── data/                                 # Data layer (Android library)
│   └── src/
│       ├── main/
│       │   ├── java/com/infinitum/labs/data/
│       │   │   └── character/            # Character data implementation
│       │   │       ├── exception/        # Data layer exceptions
│       │   │       ├── repository/       # Repository implementations
│       │   │       │   └── CharacterRepositoryImpl.kt
│       │   │       ├── remote/           # Remote data sources
│       │   │       └── mapper/           # DTO to Domain mappers
│       │   └── AndroidManifest.xml
│       ├── test/                         # Unit tests
│       └── androidTest/                  # Instrumented tests
│
└── gradle/libs.versions.toml             # Centralized dependency versions
```

### Technology Stack
- **UI Framework**: Jetpack Compose with Material3
- **Build System**: Gradle with Kotlin DSL
- **Dependency Management**: Version catalogs (`gradle/libs.versions.toml`)
- **Architecture**: Clean Architecture with multi-module separation
- **Dependency Injection**: Koin 4.0.0

### Key Dependencies
- **Compose BOM**: 2024.09.00 (version-aligned Compose dependencies)
- **Lifecycle Runtime**: 2.6.1
- **Activity Compose**: 1.11.0
- **Testing**: JUnit 4.13.2, AndroidX Test, Espresso, Compose UI Test
- **Material**: Material3 (Compose), Material Components 1.10.0 (data module)
- **Koin**: 4.0.0 (Dependency Injection)

## Development Notes

### Working with Modules

**Adding dependencies between modules:**
- In `app/build.gradle.kts`: `implementation(project(":domain"))` or `implementation(project(":data"))`
- In `data/build.gradle.kts`: `implementation(project(":domain"))`
- Never add `:app` or `:data` as dependencies in `:domain`

**Module naming in commands:**
- Use `:app`, `:domain`, `:data` to refer to modules
- Example: `./gradlew :domain:test`

### Compose UI (app module)
- Uses `enableEdgeToEdge()` for modern edge-to-edge display
- Theme: `RickAndMortyAndroidTheme` supports dark/light mode (dynamic colors disabled by default)
- **Color scheme**: Rick and Morty themed palette
  - 💚 **Verde Portal** (#00FF80) - Primary color for buttons and actions
  - 🟢 **Verde Oscuro** (#007F4E) - Section backgrounds and containers
  - ⚫ **Gris Oscuro** (#1A1A1A) - General background
  - ⚪ **Blanco Humo** (#F5F5F5) - Primary text
  - 💜 **Morado Cósmico** (#9B5DE5) - Accents and borders
- Preview functions: Annotated with `@Preview` for Compose tooling

### Testing Strategy
- **Unit tests**: Place in `src/test/` (all modules) - no Android dependencies
- **Instrumented tests**: Place in `src/androidTest/` (app and data modules only)
- Test runner: `androidx.test.runner.AndroidJUnitRunner`
- Domain module can only have JUnit tests (no Android instrumentation)

## Presentation Layer Architecture

### Organization by Feature
```
presentation/src/main/java/com/infinitum/labs/rickandmorty_android/
├── MainActivity.kt                    # Main entry point
├── RickAndMortyApplication.kt         # Application class (Koin setup)
├── NavigateApp.kt                     # Centralized navigation handler
├── NavigationRoute.kt                 # Type-safe navigation routes
├── splash/                            # Splash screen feature
│   ├── SplashScreen.kt
│   ├── SplashViewModel.kt
│   ├── SplashWrapper.kt               # UI state wrapper
│   └── SplashRouter.kt
├── main/                              # Main screen with bottom navigation
│   └── MainScreen.kt
├── character/                         # Character feature module
│   ├── state/                        # UI state wrappers
│   │   ├── CharacterListWrapper.kt
│   │   └── CharacterDetailWrapper.kt
│   ├── list/                         # Character list screen
│   │   ├── CharacterListViewModel.kt
│   │   └── CharacterListScreen.kt
│   ├── detail/                       # Character detail screen
│   │   ├── CharacterDetailViewModel.kt
│   │   └── CharacterDetailScreen.kt
│   ├── view/                         # Composable views
│   │   └── CharacterCard.kt
│   └── router/                       # Navigation actions
│       └── CharacterRouter.kt
├── location/                          # Location feature module (Coming Soon)
│   └── view/
│       └── LocationListScreen.kt
├── episode/                           # Episode feature module (Coming Soon)
│   └── view/
│       └── EpisodeListScreen.kt
├── common/                            # Shared presentation components
│   ├── viewmodel/                    # Base ViewModels
│   │   └── BaseViewModel.kt
│   └── router/                       # Base router interface
│       └── BaseRouter.kt
├── di/                                # Dependency injection
│   └── AppModule.kt                  # Koin modules
└── ui/theme/                          # Material3 theme
    ├── Color.kt
    ├── Theme.kt
    └── Type.kt
```

### UI State Wrapper Pattern

Each screen defines a sealed interface wrapper containing its UiState and Events:

**Pattern Structure**:
- Wrapper is a `sealed interface` marked as `internal`
- Contains two nested components:
  - `UiState` data class: holds all screen state (data, loading, errors)
  - `Event` sealed interface: one-time events (navigation, snackbars)

**Example**:
```kotlin
// character/state/CharacterListWrapper.kt
internal sealed interface CharacterListWrapper {
    data class UiState(
        val characters: List<Character> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null,
        val currentPage: Int = 1,
        val canLoadMore: Boolean = true
    )

    sealed interface Event {
        data object NavToDetail : Event
        data class ShowError(val message: String) : Event
    }
}
```

**Benefits**:
- Clear namespace separation per screen
- Type-safe state and events
- Easy to find all state/events for a screen
- Self-documenting code

### BaseViewModel Pattern

All ViewModels extend `BaseViewModel<StateType, EventType>`:

**BaseViewModel Structure**:
```kotlin
internal abstract class BaseViewModel<StateType, EventType>(
    initialState: StateType
) : ViewModel() {
    protected val _state: MutableStateFlow<StateType> = MutableStateFlow(initialState)
    val state: StateFlow<StateType> = _state
        .onStart { onStart() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = initialState,
        )

    protected val channelEvent: Channel<EventType> = Channel()
    val channel: ReceiveChannel<EventType> = channelEvent

    protected abstract fun onStart()
}
```

**Key Features**:
- Generic in both `StateType` and `EventType`
- StateFlow with 5-second subscriber timeout
- Channel for one-time UI events
- `onStart()` lifecycle hook called when StateFlow starts collecting
- `viewModelScope` for coroutine management

**ViewModel Implementation**:
```kotlin
internal class CharacterListViewModel(
    private val characterRepository: CharacterRepository
) : BaseViewModel<CharacterListWrapper.UiState, CharacterListWrapper.Event>(
    initialState = CharacterListWrapper.UiState()
) {
    init {
        loadCharacters()
    }

    fun loadNextPage() {
        if (!_state.value.canLoadMore || _state.value.isLoading) return
        _state.update { it.copy(currentPage = it.currentPage + 1) }
        loadCharacters()
    }

    private fun loadCharacters() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            characterRepository.getCharacters(_state.value.currentPage)
                .onSuccess { characters ->
                    _state.update {
                        it.copy(
                            characters = if (it.currentPage == 1) characters else it.characters + characters,
                            isLoading = false,
                            canLoadMore = characters.isNotEmpty()
                        )
                    }
                }
                .onFailure { exception ->
                    _state.update { it.copy(isLoading = false, error = exception.message) }
                    channelEvent.send(CharacterListWrapper.Event.ShowError(exception.message))
                }
        }
    }

    override fun onStart() = Unit // Called when state flow collection starts
}
```

**Best Practices**:
- Mark ViewModels as `internal`
- Use `_state.update { }` for state mutations
- Use `channelEvent.send()` for one-time events
- Handle errors by mapping domain exceptions to user-friendly messages
- Keep business logic in domain layer (use cases)

### Navigation Architecture

The app uses **Jetpack Navigation with Type-Safe Routes** powered by `kotlinx.serialization`:

**Type-Safe Routes with @Serializable**:
```kotlin
// NavigationRoute.kt
internal sealed interface NavigationRoute {
    @Serializable data object Splash : NavigationRoute
    @Serializable data object Main : NavigationRoute
    @Serializable data object CharacterList : NavigationRoute
    @Serializable data class CharacterDetail(val characterId: Int) : NavigationRoute
    @Serializable data object LocationList : NavigationRoute
    @Serializable data object EpisodeList : NavigationRoute
}
```

**Centralized Navigation** (NavigateApp.kt):
```kotlin
@Composable
internal fun NavigateApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: NavigationRoute = NavigationRoute.Splash
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable<NavigationRoute.Splash> {
            SplashScreen(
                onNavigate = { route ->
                    when (route) {
                        SplashRouter.NavigateToMain -> {
                            navController.navigate(NavigationRoute.Main) {
                                popUpTo(NavigationRoute.Splash) { inclusive = true }
                            }
                        }
                    }
                }
            )
        }

        composable<NavigationRoute.Main> {
            MainScreen(
                onNavigate = { route ->
                    when (route) {
                        is CharacterRouter.NavigateToDetail -> {
                            navController.navigate(NavigationRoute.CharacterDetail(route.characterId))
                        }
                        CharacterRouter.NavigateBack -> navController.popBackStack()
                    }
                }
            )
        }

        composable<NavigationRoute.CharacterDetail> { backStackEntry ->
            val route = backStackEntry.toRoute<NavigationRoute.CharacterDetail>()
            CharacterDetailScreen(
                characterId = route.characterId,
                onNavigate = { navRoute ->
                    when (navRoute) {
                        CharacterRouter.NavigateBack -> navController.popBackStack()
                        else -> {}
                    }
                }
            )
        }
    }
}
```

**Router Pattern for Features**:
```kotlin
// common/router/BaseRouter.kt
internal interface BaseRouter

// character/router/CharacterRouter.kt
internal sealed class CharacterRouter : BaseRouter {
    data class NavigateToDetail(val characterId: Int) : CharacterRouter()
    data object NavigateBack : CharacterRouter()
}
```

**Navigation Flow**:
1. **Splash Screen** (2-second delay) → **Main Screen**
2. **Main Screen** (Bottom Navigation with 3 tabs):
   - Characters Tab → **Character List** → **Character Detail**
   - Locations Tab → **Location List** (Coming Soon)
   - Episodes Tab → **Episode List** (Coming Soon)

**Benefits**:
- Type-safe navigation with compile-time checks
- Automatic argument serialization/deserialization
- Centralized navigation logic in NavigateApp
- Feature-specific routers for domain actions
- Clear separation between navigation routes and feature actions

### Composable Screens

**Screen Pattern**:
```kotlin
@Composable
internal fun CharacterListScreen(
    viewModel: CharacterListViewModel = koinViewModel(),
    onNavigate: (CharacterRouter) -> Unit = {}
) {
    val state by viewModel.state.collectAsState()

    // Collect one-time events
    LaunchedEffect(Unit) {
        viewModel.channel.receiveAsFlow().collect { event ->
            when (event) {
                is CharacterListWrapper.Event.NavToDetail -> {
                    onNavigate(CharacterRouter.Detail(event.characterId))
                }
                is CharacterListWrapper.Event.ShowError -> {
                    // Show snackbar or toast
                }
            }
        }
    }

    CharacterListContent(
        state = state,
        onLoadMore = viewModel::loadNextPage,
        onRetry = viewModel::retry
    )
}

@Composable
private fun CharacterListContent(
    state: CharacterListWrapper.UiState,
    onLoadMore: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    // UI implementation
}
```

**Best Practices**:
- Main screen function is `internal`
- Inject ViewModel with `koinViewModel()`
- Collect state with `collectAsState()`
- Separate content into private `*Content` function for testability
- Collect events in `LaunchedEffect`
- Pass callbacks as method references (`viewModel::method`)

**Infinite Scroll Pagination Pattern**:

For implementing infinite scroll in LazyColumn/LazyRow, use `snapshotFlow` to observe scroll state:

```kotlin
@Composable
internal fun CharacterListScreen(
    viewModel: CharacterListViewModel = koinViewModel(),
    onEvent: (CharacterListWrapper.Event) -> Unit = {},
    onGoTo: (CharacterRouter) -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
    val listState = rememberLazyListState()

    // Pagination trigger using snapshotFlow
    LaunchedEffect(listState, state.canLoadMore, state.isLoading) {
        snapshotFlow {
            val layoutInfo = listState.layoutInfo
            val totalItemsCount = layoutInfo.totalItemsCount
            val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0

            // Trigger when user is 3 items from the end
            lastVisibleItemIndex >= totalItemsCount - 3
        }
        .collect { shouldLoadMore ->
            if (shouldLoadMore && state.canLoadMore && !state.isLoading) {
                onEvent(CharacterListWrapper.Event.LoadNextPage)
            }
        }
    }

    LazyColumn(state = listState) {
        items(state.characters) { character ->
            CharacterCard(character = character)
        }

        if (state.isLoading) {
            item {
                CircularProgressIndicator()
            }
        }
    }
}
```

**Key Points**:
- Use `snapshotFlow` to observe `LazyListState.layoutInfo`
- Check `lastVisibleItemIndex >= totalItemsCount - 3` to load next page
- Add guards: `canLoadMore && !isLoading` to prevent duplicate requests
- Show loading indicator at the end of the list during pagination
- DO NOT use `derivedStateOf` for scroll observation (doesn't trigger properly)

### Use Cases

**Structure**:
- One use case class per operation
- Constructor injection for dependencies
- All methods are suspend functions
- Return `Result<T>` for error handling

**Example**:
```kotlin
// domain/character/usecase/GetCharactersUseCase.kt
class GetCharactersUseCase(
    private val characterRepository: CharacterRepository
) {
    suspend operator fun invoke(page: Int): Result<List<Character>> {
        if (page < 1) {
            return Result.failure(InvalidCharacterPage(page))
        }
        return characterRepository.getCharacters(page)
    }
}
```

**Naming Convention**:
- Pattern: `[Action][Entity]UseCase` (e.g., `GetCharactersUseCase`, `SearchCharactersByNameUseCase`)
- Use `invoke()` operator for single-operation use cases
- Place in `domain/[context]/usecase/`

### Dependency Injection (Koin)

**Module Structure**:
```kotlin
// presentation/di/AppModule.kt
val appModule = module {
    // ViewModels
    viewModelOf(::CharacterListViewModel)

    // Use Cases
    singleOf(::GetCharactersUseCase)
    singleOf(::GetCharacterByIdUseCase)
    singleOf(::SearchCharactersByNameUseCase)

    // Repositories
    singleOf(::CharacterRepositoryImpl) bind CharacterRepository::class
}
```

**Initialization** (in Application class):
```kotlin
class RickAndMortyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@RickAndMortyApplication)
            modules(appModule)
        }
    }
}
```

**Best Practices**:
- Use `viewModelOf` for ViewModels
- Use `singleOf` with `bind` for repository implementations
- Group dependencies by feature/bounded context
- Use constructor injection in ViewModels and use cases

### Data Layer Structure

**Organization by Bounded Context**:
```
data/src/main/java/com/infinitum/labs/data/
└── character/
    ├── exception/                    # Data layer exceptions
    │   └── DataException.kt
    ├── remote/
    │   ├── datasource/              # Remote data sources
    │   │   └── CharacterRemoteDataSource.kt
    │   └── dto/                     # Data Transfer Objects
    │       ├── CharacterDto.kt
    │       └── CharacterResponseDto.kt
    ├── mapper/                       # DTO ↔ Domain converters
    │   └── CharacterMapper.kt
    └── repository/                   # Repository implementations
        └── CharacterRepositoryImpl.kt
```

**DTOs (Data Transfer Objects)**:
- Mark as `internal data class`
- Use `@Serializable` for JSON serialization
- Named with `Dto` suffix
- Simple data classes without business logic

**Mappers**:
- Mark as `internal object` for stateless mappers
- Methods: `toDto()`, `toDomain()`
- Keep conversion logic simple
- Example: `CharacterMapper.toDomain(dto: CharacterDto): Character`

**Repository Implementation**:
```kotlin
internal class CharacterRepositoryImpl(
    private val remoteDataSource: CharacterRemoteDataSource
) : CharacterRepository {
    override suspend fun getCharacters(page: Int): Result<List<Character>> {
        return try {
            val response = remoteDataSource.getCharacters(page)
            val characters = response.results.map { CharacterMapper.toDomain(it) }
            Result.success(characters)
        } catch (e: Exception) {
            Result.failure(CharacterRepositoryUnavailable(e.message))
        }
    }
}
```

### Visibility Modifiers

**Guidelines**:
- **Presentation layer**: All classes, composables, and wrappers should be `internal`
- **Data layer**: All classes, objects, and DTOs should be `internal`
- **Domain layer**: `public` (accessible across modules)
- Only expose what's necessary - prefer `internal` or `private` by default

**Rationale**:
- Prevents accidental exposure of implementation details
- Clear module boundaries
- Better encapsulation

### Domain Exception Architecture

The project implements a robust exception hierarchy following DDD principles:

**Base Exceptions** (`domain/common/exception/`):
- `DomainException`: Root of all domain exceptions, includes error codes
- `ValidationException`: For entity/value object validation failures
- `NotFoundException`: For missing resources (404 scenarios)
- `RepositoryException`: For infrastructure/data access failures

**Character Exceptions** (`domain/character/exception/`):
All character exceptions inherit from `DomainException` and include unique error codes:

**Validation Exceptions** (Model Invariants):
- `InvalidCharacterId`: ID must be >= 1
- `InvalidCharacterName`: Name cannot be blank
- `InvalidCharacterSpecies`: Species cannot be blank
- `InvalidCharacterImageUrl`: Image URL must be valid HTTP/HTTPS
- `InvalidCharacterUrl`: URL must be valid HTTP/HTTPS
- `InvalidCharacterEpisodes`: Must have at least 1 episode
- `InvalidCharacterLocation`: Location name/URL validation
- `InvalidCharacterCreatedDate`: Date must be valid ISO-8601

**Use Case Exceptions**:
- `InvalidCharacterPage`: Page number must be >= 1
- `InvalidCharacterSearchQuery`: Search query cannot be blank

**Repository Exceptions**:
- `CharacterNotFound`: Character doesn't exist by ID
- `CharactersNotFoundByName`: No characters found by name
- `CharacterRepositoryUnavailable`: Catalog temporarily unavailable
- `InvalidCharacterData`: Data corrupted or invalid format

**Self-Validating Models**:
All domain models validate themselves on construction:
- `Character`: Validates ID, name, species, URLs, episodes, locations
- `CharacterLocation`: Validates name and URL format

### Testing Philosophy

**Test Naming Convention**: `given [context] when [action] then [expected result]`

Example:
```kotlin
@Test
fun `given valid ID when creating character then returns character successfully`() {
    val validId = 1

    val character = CharacterBuilder.rickSanchez().withId(validId).build()

    assertEquals(validId, character.id)
}
```

**Test Principles**:
- No comments (`// Given`, `// When`, `// Then`)
- Test name describes the complete scenario
- Clean, direct code
- 80+ tests covering all domain logic
- Each model has comprehensive validation tests

**Test Data Builders**:
- Use the Builder pattern for creating test data
- Create builders in a `databuilder/` directory within the test package
- Builders should have sensible default values
- Use companion object factory methods with clear names (e.g., `buildCharacter()`)
- Use inline return expressions for `build()` method

Example:
```kotlin
// domain/src/test/.../character/model/databuilder/CharacterBuilder.kt
class CharacterBuilder {
    private var id: Int = 1
    private var name: String = "Rick Sanchez"
    private var status: CharacterStatus = CharacterStatus.ALIVE
    private var species: String = "Human"
    private var image: String = "https://example.com/rick.png"

    fun withId(id: Int) = apply { this.id = id }
    fun withName(name: String) = apply { this.name = name }
    fun withStatus(status: CharacterStatus) = apply { this.status = status }

    fun build() = Character(
        id = id,
        name = name,
        status = status,
        species = species,
        image = image,
        // ... other properties
    )

    companion object {
        fun buildCharacter() = CharacterBuilder()
        fun rickSanchez() = CharacterBuilder()
        fun mortySmith() = CharacterBuilder()
            .withId(2)
            .withName("Morty Smith")
    }
}

// Usage in tests:
@Test
fun `given valid character when saving then returns success`() {
    val character = buildCharacter()
        .withName("Summer Smith")
        .withId(3)
        .build()

    assertEquals("Summer Smith", character.name)
}
```

**Benefits**:
- Readable test setup
- Reusable across tests
- Easy to modify for specific test cases
- Reduces test data duplication
- Clear intent with named factory methods

### Gradle Configuration
- Version catalog: Edit `gradle/libs.versions.toml` to update dependency versions
- All modules use Java 11 compatibility
- ProGuard is disabled in debug builds, enabled (but not minified) in release builds

## Domain-Driven Design (DDD)

### Bounded Contexts
- **Character**: Manages character entities, their properties, and related operations
- **Common**: Shared kernel with base exceptions and utilities

### Ubiquitous Language
The code speaks the domain language:
- Exceptions use business-oriented messages
- Models enforce business rules through validation
- Use cases encapsulate business operations

### Aggregates
- `Character`: Self-validating aggregate root that ensures all invariants are met on construction