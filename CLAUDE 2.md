# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**Trakero** is a Kotlin Multiplatform project using Compose Multiplatform, targeting Android and iOS platforms. The project uses Gradle with Kotlin DSL for build configuration.

**Package name:** `com.infinite.solution.trakero`

## Build System

This project uses Gradle with version catalog (gradle/libs.versions.toml) for dependency management.

### Key Build Commands

**Android:**
```shell
# Build debug APK
./gradlew :composeApp:assembleDebug

# Build release APK
./gradlew :composeApp:assembleRelease

# Clean build
./gradlew clean
```

**iOS:**
- Open `iosApp/iosApp.xcodeproj` in Xcode and build from there
- The Kotlin framework is built automatically when building the iOS app
- Framework name: `ComposeApp` (static framework)
- Supported targets: `iosArm64` (device), `iosSimulatorArm64` (simulator)

**Testing:**
```shell
# Run common tests
./gradlew :composeApp:test

# Run Android instrumented tests
./gradlew :composeApp:connectedAndroidTest
```

## Architecture

### Source Set Structure

The codebase follows Kotlin Multiplatform conventions with platform-specific and shared code:

- **composeApp/src/commonMain/kotlin**: Shared business logic and UI code for all platforms
  - Follows **Clean Architecture** principles with three layers:
    - `domain/`: Business logic (models, repository interfaces, use cases)
    - `data/`: Data layer (repository implementations, data sources, mappers)
    - `presentation/`: UI layer (screens, components, viewmodels, navigation)
  - `di/`: Dependency injection setup
  - Platform abstractions (Platform.kt)

- **composeApp/src/androidMain/kotlin**: Android-specific implementations
  - Entry point: `MainActivity.kt`
  - Platform-specific implementations in `Platform.android.kt`

- **composeApp/src/iosMain/kotlin**: iOS-specific implementations
  - Entry point: `MainViewController.kt`
  - Platform-specific implementations in `Platform.ios.kt`

- **composeApp/src/commonMain/composeResources**: Shared resources (images, strings, etc.)
  - Accessed via generated `Res` class in `trakero.composeapp.generated.resources`

- **composeApp/src/commonTest/kotlin**: Common unit tests

- **iosApp/iosApp**: Swift/SwiftUI entry point for iOS application
  - Xcode project: `iosApp/iosApp.xcodeproj`
  - This directory contains the iOS app wrapper that loads the Kotlin framework

### Clean Architecture

The project follows Clean Architecture with clear separation of concerns:

**Dependency Rule**: `Presentation → Domain ← Data`
- Domain layer has no dependencies on other layers
- Data and Presentation layers depend on Domain
- Domain defines repository interfaces; Data implements them

**Layer Responsibilities**:
- **Domain**: Pure business logic organized by bounded contexts (e.g., `auth/`, `tracking/`)
  - Each context contains: models, repository interfaces, and use cases
  - Example: `domain/auth/` contains authentication-related business logic
  - **Use Cases**: Group related operations in a single use case class (e.g., `AuthUseCase` with login/logout methods)
  - Repository interfaces define data contracts; implementations go in data layer
- **Data**: Repository implementations, data sources (local/remote), data mappers, DTOs
  - Organized by bounded contexts matching domain structure (e.g., `data/auth/`)
  - **DTOs**: Data Transfer Objects for API/database communication
  - **Mappers**: Convert between DTOs and domain models (use object for stateless mappers)
  - **Repository Implementations**: Named `[Repository]Impl` (e.g., `AuthRepositoryImpl`)
  - Use `@Serializable` annotation for DTOs (kotlinx.serialization)
- **Presentation**: Composables, ViewModels, navigation, UI components
  - Organized by bounded contexts matching domain structure (e.g., `presentation/auth/`)
  - **ViewModels**: State management with StateFlow, handle UI logic
  - **Views**: Composable screens, use koinViewModel() for DI integration

### Platform Abstraction Pattern

The project uses the expect/actual pattern for platform-specific code:
- Define `expect` declarations in `commonMain`
- Provide `actual` implementations in `androidMain` and `iosMain`
- Example: `Platform.kt` (expect) with platform-specific implementations

### Key Technologies

- **Kotlin:** 2.2.20
- **Compose Multiplatform:** 1.9.0
- **Koin:** 4.0.0 (Dependency Injection)
- **Android:**
  - Min SDK: 24
  - Target SDK: 36
  - Compile SDK: 36
  - Java Version: 17
- **Lifecycle:** AndroidX Lifecycle 2.9.4 with ViewModel and Compose integration
- **AGP:** 8.11.2

## Development Notes

### Gradle Configuration

- Version catalog is enabled and defined in `gradle/libs.versions.toml`
- Type-safe project accessors are enabled
- Configuration cache and build cache are enabled by default
- JVM args are pre-configured for Kotlin daemon (3GB) and Gradle (4GB)

### iOS Development

- The iOS framework (`ComposeApp`) is configured as a static framework
- Framework is automatically rebuilt when you build the iOS app in Xcode
- iOS project lives in `iosApp/` directory as a separate Xcode project

### Adding Dependencies

Add dependencies to `gradle/libs.versions.toml`, then reference them in `composeApp/build.gradle.kts` within the appropriate source set:
- `commonMain.dependencies` for shared code
- `androidMain.dependencies` for Android-specific
- `iosMain.dependencies` for iOS-specific (if needed)

### Resource Management

- Place shared resources in `composeApp/src/commonMain/composeResources/`
- Resources are accessed via the generated `Res` object
- Example: `painterResource(Res.drawable.compose_multiplatform)`

### Use Cases

**Structure**:
- Group related operations in a single use case class per bounded context
- Example: `AuthUseCase` contains `login()`, `logout()`, `isAuthenticated()`
- Use constructor injection for repository dependencies
- All methods should be suspend functions for async operations
- Return `Result<T>` for operations that can fail

**Naming**:
- Pattern: `[Context]UseCase` (e.g., `AuthUseCase`, `TrackingUseCase`)
- Methods should be descriptive verbs (e.g., `login()`, `logout()`, `track()`)

Example:
```kotlin
class AuthUseCase(
    private val authRepository: AuthRepository
) {
    suspend fun login(login: Login): Result<String>
    suspend fun logout(): Result<Unit>
    suspend fun isAuthenticated(): Boolean
}
```

### Dependency Injection (Koin)

**Setup**:
- Koin 4.0.0 for dependency injection
- Initialized in platform-specific entry points (Android Application, iOS MainViewController)
- Modules organized by bounded contexts

**Module Structure**:
```kotlin
di/
├── AppModule.kt           # Main module that includes all others
├── AuthModule.kt          # Auth-specific dependencies
└── KoinInitializer.kt     # Common initialization function
```

**Module Definition**:
- Use `singleOf` for single instances with constructor injection
- Use `bind` to link implementations to interfaces
- Group dependencies by bounded context

Example:
```kotlin
val authModule = module {
    // Repository
    singleOf(::AuthRepositoryImpl) bind AuthRepository::class

    // Use Case
    singleOf(::AuthUseCase)
}
```

**Initialization**:
- Android: In `TrakeroApplication.onCreate()` with `androidContext()` and `androidLogger()`
- iOS: In `MainViewController()` before creating the UI
- Common: Use `initKoin()` function from `KoinInitializer.kt`

**ViewModel Registration**:
- Use `viewModelOf` from `org.koin.core.module.dsl` package
- Example: `viewModelOf(::LoginViewModel)`

### Presentation Layer

**Organization by Bounded Context**:
```
presentation/
├── common/                            # Shared presentation components
│   ├── App.kt                        # Main app entry point
│   ├── components/                   # Reusable UI components
│   │   ├── button/                  # Button components
│   │   │   └── ButtonComponent.kt
│   │   ├── textfield/               # Text field components
│   │   │   ├── TextFieldComponent.kt
│   │   │   ├── EmailTextFieldComponent.kt
│   │   │   └── PasswordTextFieldComponent.kt
│   │   └── scaffold/                # Scaffold/layout components
│   │       └── ScaffoldScreenComponent.kt
│   ├── viewmodel/                    # Base ViewModels and UI events
│   │   ├── BaseViewModel.kt
│   │   └── UiEvent.kt
│   ├── screens/                      # Common screens
│   └── navigation/                   # App-wide navigation
│       ├── NavigateApp.kt
│       └── NavigationRoute.kt
└── auth/                              # Auth-specific bounded context
    ├── uistate/
    │   └── LoginWrapper.kt
    ├── viewmodel/
    │   └── LoginViewModel.kt
    └── view/
        └── LoginScreen.kt
```

**UI State Wrapper Pattern**:
- Create a `[Screen]Wrapper` sealed interface in `uistate/` directory (marked as `internal`)
- Wrapper is just a container - UiState and UiEvent do NOT extend from it
- Contains two nested interfaces/classes:
  - `UiState` data class: holds all screen state (data, loading, errors)
  - `UiEvent` sealed interface: ALL events (both user interactions AND one-time events)
- Use `internal` visibility for wrapper and all presentation layer classes
- UiEvent combines both user interactions (clicks, text changes) and one-time events (navigation, snackbars)

Example:
```kotlin
internal sealed interface LoginWrapper {
    data class UiState(
        val email: String = "",
        val password: String = "",
        val isLoading: Boolean = false,
        val error: String? = null
    )

    sealed interface UiEvent {
        // User interactions
        data class EmailChanged(val email: String) : UiEvent
        data class PasswordChanged(val password: String) : UiEvent
        data object LoginClicked : UiEvent

        // One-time events
        data class ShowSnackbar(val message: String) : UiEvent
        data object NavigateToHome : UiEvent
    }
}
```

**BaseViewModel Pattern**:
- All ViewModels should extend `BaseViewModel<UiState, UiEvent>` from `presentation/common/viewmodel/`
- BaseViewModel is now generic in both UiState AND UiEvent
- BaseViewModel provides common functionality:
  - `_uiState` and `uiState` StateFlow for UI state management
  - `_uiEvent` Channel for one-time UI events (navigation, snackbars, etc.)
  - `suspend fun sendUiEvent(event: UiEvent)` - send one-time events (must be called from coroutine context)
  - `updateState(update: (UiState) -> UiState)` method to update state
  - `launch(block: suspend () -> Unit)` protected helper to launch coroutines in viewModelScope
  - `onStart()` lifecycle hook called when state flow starts
- StateFlow configuration with 5-second subscriber timeout:
  ```kotlin
  SharingStarted.WhileSubscribed(5000L)
  ```
- **Important**: `sendUiEvent` is a suspend function and must be called from within a coroutine (e.g., inside a `launch {}` block)

**ViewModels**:
- Mark as `internal` class
- Extend `BaseViewModel<[Screen]Wrapper.UiState, [Screen]Wrapper.UiEvent>` instead of ViewModel directly
- Pass initial state to BaseViewModel constructor
- Single `internal` `onEvent()` method to handle ALL UI events (from `[Screen]Wrapper.UiEvent`)
- Use `when` expression to handle different event types
- Private methods for each event handler (e.g., `handleEmailChanged()`)
- Use `updateState {}` to modify UI state
- Use `sendUiEvent()` to send one-time UI events (navigation, snackbars)
- Inject use cases via constructor
- Handle one-time events in when expression but don't process them (they're handled in the UI layer)

Example:
```kotlin
internal class LoginViewModel(
    private val authUseCase: AuthUseCase
) : BaseViewModel<LoginWrapper.UiState, LoginWrapper.UiEvent>(LoginWrapper.UiState()) {

    internal fun onEvent(event: LoginWrapper.UiEvent) {
        when (event) {
            // User interactions - handle in ViewModel
            is LoginWrapper.UiEvent.EmailChanged -> handleEmailChanged(event.email)
            is LoginWrapper.UiEvent.PasswordChanged -> handlePasswordChanged(event.password)
            is LoginWrapper.UiEvent.LoginClicked -> handleLoginClicked()

            // One-time events - handled in UI layer
            is LoginWrapper.UiEvent.ShowSnackbar -> { /* Handled in UI */ }
            is LoginWrapper.UiEvent.NavigateToHome -> { /* Handled in UI */ }
        }
    }

    private fun handleEmailChanged(email: String) {
        updateState { it.copy(email = email) }
    }

    private fun handlePasswordChanged(password: String) {
        updateState { it.copy(password = password) }
    }

    private fun handleLoginClicked() {
        launch {
            updateState { it.copy(isLoading = true) }
            authUseCase.login(/* ... */)
                .onSuccess {
                    updateState { it.copy(isLoading = false) }
                    sendUiEvent(LoginWrapper.UiEvent.NavigateToHome)
                }
                .onFailure { error ->
                    updateState { it.copy(isLoading = false, error = error.message) }
                    sendUiEvent(LoginWrapper.UiEvent.ShowSnackbar(error.message ?: "Error"))
                }
        }
    }
}
```

**UI Events (Generic per Screen)**:
- Each screen defines its own `UiEvent` sealed interface inside the `[Screen]Wrapper`
- UiEvent is for one-time actions (navigation, snackbars, toasts)
- Define events specific to each screen's needs
- Common UiEvent types:
  - `ShowSnackbar(message: String)` - Display snackbar message
  - `ShowToast(message: String)` - Display toast message
  - `NavigateTo[Screen]` - Navigate to specific screen
- Collect `viewModel.uiEvent` in screens using `LaunchedEffect`
- **Note**: The old global `UiEvent.kt` in `common/viewmodel/` is deprecated - each screen now defines its own UiEvent

**Composable Screens**:
- Main screen function should be `internal` with no parameters (for better encapsulation)
- Inject ViewModel inside the function using `koinViewModel()`
- Collect state with `collectAsState()`
- Separate presentation logic into a private `*Content` function for testability
- Pass callbacks to ViewModel methods (use method references: `viewModel::method`)
- Use Material 3 components (Scaffold, OutlinedTextField, Button, etc.)
- Handle loading states, errors, and validation in UI state

Example:
```kotlin
@Composable
internal fun LoginScreen() {
    val viewModel: LoginViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()

    LoginScreenContent(
        uiState = uiState,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun LoginScreenContent(
    uiState: LoginWrapper.UiState,
    onEvent: (LoginWrapper.UiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(modifier = modifier.fillMaxSize()) { paddingValues ->
        OutlinedTextField(
            value = uiState.email,
            onValueChange = { onEvent(LoginWrapper.UiEvent.EmailChanged(it)) },
            // ...
        )
        Button(
            onClick = { onEvent(LoginWrapper.UiEvent.LoginClicked) }
        ) {
            Text("Login")
        }
    }
}
```

**Collecting UI Events in Screens**:
- UI events from ViewModels must be collected in the main screen function (not in *Content)
- Use `LaunchedEffect` with `viewModel.uiEvent` to collect and handle events
- Handle navigation, snackbars, and toasts based on event type
- Use the specific UiEvent type from the Wrapper (e.g., `LoginWrapper.UiEvent`)

Example with SnackbarHost and Navigation:
```kotlin
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LoginScreen(
    navController: NavHostController = rememberNavController()
) {
    val viewModel: LoginViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Collect UI events
    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is LoginWrapper.UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message)
                }
                is LoginWrapper.UiEvent.NavigateToHome -> {
                    navController.navigate(NavigationRoute.Home) {
                        popUpTo(NavigationRoute.Login) { inclusive = true }
                    }
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        LoginScreenContent(
            uiState = uiState,
            onEvent = viewModel::onEvent,
            modifier = Modifier.padding(paddingValues)
        )
    }
}
```

### Reusable Components

**Component Organization**:
- Components organized by type in `presentation/common/components/`
- Each component type has its own package (button/, textfield/, scaffold/, etc.)
- All components are marked as `internal`
- Use `@Preview` for visual testing (available in commonMain with Compose Multiplatform 1.9.0+)

**Button Components** (`components/button/`):
- `ButtonComponent.kt`: Reusable button with variants and states
- Supports three variants via `ButtonVariant` enum:
  - `PRIMARY`: Filled button (default)
  - `SECONDARY`: Outlined button
  - `TEXT`: Text button (no background)
- Features:
  - Loading state with `CircularProgressIndicator`
  - Leading and trailing icons
  - `fillMaxWidth` parameter (default: true)
  - Enabled/disabled states

Example:
```kotlin
ButtonComponent(
    text = "Sign In",
    onClick = { /* action */ },
    leadingIcon = Icons.Default.Login,
    isLoading = isLoading,
    enabled = !isLoading,
    variant = ButtonVariant.PRIMARY
)
```

**TextField Components** (`components/textfield/`):
- `TextFieldComponent.kt`: Generic text field wrapper
- `EmailTextFieldComponent.kt`: Email-specific text field
  - Pre-configured with email icon
  - Email keyboard type
  - ImeAction.Next by default
- `PasswordTextFieldComponent.kt`: Password-specific text field
  - Lock icon
  - Toggle visibility button
  - Password masking
  - Password keyboard type

Example:
```kotlin
EmailTextFieldComponent(
    value = email,
    onValueChange = { email = it },
    isError = emailError != null,
    errorMessage = emailError,
    enabled = true,
    imeAction = ImeAction.Next
)

PasswordTextFieldComponent(
    value = password,
    onValueChange = { password = it },
    isError = passwordError != null,
    errorMessage = passwordError,
    enabled = true,
    imeAction = ImeAction.Done,
    keyboardActions = KeyboardActions(onDone = { /* submit */ })
)
```

**Scaffold Components** (`components/scaffold/`):
- `ScaffoldScreenComponent.kt`: Scaffold wrapper with common functionality
- Features:
  - Optional `topBar` parameter for TopAppBar
  - Scrollable content with `enableScroll` parameter (default: true)
  - Uses `rememberScrollState()` and `verticalScroll()`
  - Column-based content layout with padding

Example:
```kotlin
ScaffoldScreenComponent(
    topBar = {
        TopAppBar(title = { Text("Screen Title") })
    },
    enableScroll = true
) {
    // Column content here
    Text("Your content")
}
```

**Previews in commonMain**:
- `@Preview` annotations ARE available in commonMain with Compose Multiplatform 1.9.0+
- Create multiple previews for different states (default, loading, error, with data, etc.)
- Wrap preview content in `MaterialTheme` for proper theming
- Use `ScaffoldScreenComponent` in previews to show realistic layouts
- Preview functions should be marked as `private`

Example:
```kotlin
@Preview
@Composable
private fun LoginScreenPreview() {
    MaterialTheme {
        LoginScreenContent(
            uiState = LoginWrapper.UiState(),
            onEvent = {}
        )
    }
}

@Preview
@Composable
private fun LoginScreenLoadingPreview() {
    MaterialTheme {
        LoginScreenContent(
            uiState = LoginWrapper.UiState(isLoading = true),
            onEvent = {}
        )
    }
}
```

### Navigation

**Type-Safe Navigation with Router Pattern**:
- Use Jetpack Compose Navigation with kotlinx.serialization for type-safe routes
- Router-based navigation for decoupled, testable architecture
- Define routes in `presentation/common/navigation/NavigationRoute.kt`
- Each screen has its own Router sealed interface in `[screen]/router/`
- Mark all navigation components as `internal`

**Router Pattern Structure**:
```
presentation/
├── common/
│   └── navigation/
│       ├── NavigationRoute.kt          # App-wide route definitions
│       ├── NavigateApp.kt              # Centralized navigation handler
│       └── router/
│           └── NavigationRouter.kt     # Base router interface
├── auth/
│   └── router/
│       └── LoginRouter.kt              # Login screen navigation actions
├── dashboard/
│   └── router/
│       └── DashboardRouter.kt          # Dashboard screen navigation actions
└── splash/
    └── router/
        └── SplashRouter.kt             # Splash screen navigation actions
```

**Base Router Interface**:
```kotlin
// presentation/common/navigation/router/NavigationRouter.kt
internal interface NavigationRouter
```

**Screen Router Definition**:
Each screen defines its own navigation actions:
```kotlin
// presentation/auth/router/LoginRouter.kt
internal sealed interface LoginRouter : NavigationRouter {
    data object NavigateToDashboard : LoginRouter
    data object NavigateToForgotPassword : LoginRouter
    data object NavigateToSignUp : LoginRouter
}

// presentation/dashboard/router/DashboardRouter.kt
internal sealed interface DashboardRouter : NavigationRouter {
    data object NavigateToLogin : DashboardRouter
    data object NavigateToProfile : DashboardRouter
    data object NavigateToSettings : DashboardRouter
}
```

**Route Definition**:
```kotlin
// presentation/common/navigation/NavigationRoute.kt
internal sealed interface NavigationRoute {
    @Serializable
    data object Splash : NavigationRoute

    @Serializable
    data object Login : NavigationRoute

    @Serializable
    data object Dashboard : NavigationRoute

    // For routes with parameters
    @Serializable
    data class Detail(val id: String) : NavigationRoute
}
```

**Screen Implementation with Router**:
Screens receive a callback with their specific Router type:
```kotlin
@Composable
internal fun LoginScreen(
    onNavigate: (LoginRouter) -> Unit = {}
) {
    val viewModel: LoginViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                LoginWrapper.UiEvent.NavigateToDashboard -> {
                    onNavigate(LoginRouter.NavigateToDashboard)
                }
            }
        }
    }

    LoginScreenContent(uiState, onEvent = viewModel::onEvent)
}
```

**Centralized Navigation Handler in NavigateApp**:
All navigation logic is centralized in a single handler:
```kotlin
@Composable
internal fun NavigateApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: NavigationRoute = NavigationRoute.Splash
) {
    // Centralized navigation handler
    val handleNavigation: (NavigationRouter) -> Unit = { router ->
        when (router) {
            // Splash navigation
            is SplashRouter.NavigateToLogin -> {
                navController.navigate(NavigationRoute.Login) {
                    popUpTo(NavigationRoute.Splash) { inclusive = true }
                }
            }
            is SplashRouter.NavigateToDashboard -> {
                navController.navigate(NavigationRoute.Dashboard) {
                    popUpTo(NavigationRoute.Splash) { inclusive = true }
                }
            }

            // Login navigation
            is LoginRouter.NavigateToDashboard -> {
                navController.navigate(NavigationRoute.Dashboard) {
                    popUpTo(NavigationRoute.Login) { inclusive = true }
                }
            }

            // Dashboard navigation
            is DashboardRouter.NavigateToLogin -> {
                navController.navigate(NavigationRoute.Login) {
                    popUpTo(0) { inclusive = true }
                }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable<NavigationRoute.Splash> {
            SplashScreen(onNavigate = handleNavigation)
        }

        composable<NavigationRoute.Login> {
            LoginScreen(onNavigate = handleNavigation)
        }

        composable<NavigationRoute.Dashboard> {
            DashboardScreen(onNavigate = handleNavigation)
        }
    }
}
```

**Splash Screen for Authentication Check**:
- The app starts with a Splash screen that validates the user's session
- Uses `AuthUseCase.validateSession()` to check token validity
- Automatically navigates to Dashboard if authenticated, or Login if not
- Handles token expiration and automatic cleanup

**SplashViewModel Example**:
```kotlin
internal class SplashViewModel(
    private val authUseCase: AuthUseCase
) : BaseViewModel<SplashWrapper.UiState, SplashWrapper.UiEvent>(SplashWrapper.UiState()) {

    override fun onStart() {
        super.onStart()
        checkAuthenticationStatus()
    }

    private fun checkAuthenticationStatus() {
        launch {
            authUseCase.validateSession()
                .onSuccess { isValid ->
                    if (isValid) {
                        sendUiEvent(SplashWrapper.UiEvent.NavigateToDashboard)
                    } else {
                        sendUiEvent(SplashWrapper.UiEvent.NavigateToLogin)
                    }
                }
                .onFailure {
                    sendUiEvent(SplashWrapper.UiEvent.NavigateToLogin)
                }
        }
    }
}
```

**Session Persistence**:
- Tokens are stored in DataStore (Android) for persistent authentication
- Session validation happens on app startup via Splash screen
- Invalid/expired tokens are automatically cleared
- Methods available in AuthRepository:
  - `getToken()`: Retrieve stored token
  - `validateToken()`: Validate token with backend
  - `isAuthenticated()`: Check if user has valid token

**Navigation Flow**:
```
App Start → Splash (validates session)
    ├── Token valid → Dashboard
    └── Token invalid/missing → Login → Dashboard
```

**Advantages of Router Pattern**:
1. **Type Safety**: Compiler ensures only valid routes are used
2. **Extensibility**: Add new routes without modifying screens
3. **Testability**: Easy to mock routers in tests
4. **Separation of Concerns**: Screens don't know about NavController
5. **Centralized Logic**: All navigation in one place (NavigateApp)
6. **Documentation**: Routers serve as navigation documentation

**Dependencies**:
- Navigation Compose: `androidx-navigation = "2.8.0-alpha10"`
- Kotlinx Serialization: `kotlinx-serialization = "1.7.3"`
- Add `kotlinSerialization` plugin to `build.gradle.kts`

### Data Layer Structure

**Organization by Bounded Context**:
```
data/
└── auth/                      # Same context as domain
    ├── dto/                   # Data Transfer Objects
    │   ├── LoginDto.kt
    │   └── AuthResponseDto.kt
    ├── mapper/                # Domain ↔ DTO converters
    │   └── LoginMapper.kt
    └── repository/            # Repository implementations
        └── AuthRepositoryImpl.kt
```

**Visibility Modifiers**:
- **Data layer**: All classes, objects, and DTOs should be `internal`
- **Presentation layer**: All classes, composables, and wrappers should be `internal`
- **Domain layer**: Public (accessible across modules)
- Only expose what's necessary - prefer `internal` or `private` by default

**DTOs (Data Transfer Objects)**:
- Mark as `internal data class`
- Use `@Serializable` for JSON serialization
- Simple data classes without business logic
- Named with `Dto` suffix (e.g., `LoginDto`, `AuthResponseDto`)
- Only contain data needed for API/database communication

**Mappers**:
- Mark as `internal object` for stateless mappers
- Methods: `toDto()`, `toDomain()`, `toEntity()`
- Keep conversion logic simple and focused
- Example: `LoginMapper.toDto(login: Login): LoginDto`

**Repository Implementation**:
- Mark as `internal class`
- Named `[Repository]Impl` (e.g., `AuthRepositoryImpl`)
- Implements domain repository interface
- Uses mappers to convert between DTOs and domain models
- Handles data source coordination (remote + local)
- Returns `Result<T>` for error handling

Example:
```kotlin
internal class AuthRepositoryImpl(
    private val remoteDataSource: AuthRemoteDataSource,
    private val localDataSource: AuthLocalDataSource
) : AuthRepository {
    override suspend fun authenticate(login: Login): Result<String> {
        return try {
            val loginDto = LoginMapper.toDto(login)
            val response = remoteDataSource.login(loginDto)
            localDataSource.saveToken(response.token)
            Result.success(response.token)
        } catch (e: Exception) {
            Result.failure(AuthenticationException(e.message))
        }
    }
}
```

### Testing Best Practices

**Test Naming Convention**:
- Use Given-When-Then format for test names (BDD style)
- Pattern: `given [context] when [action] then [expected outcome]`
- Examples:
  - `given valid email and password when building login then returns login instance`
  - `given empty email when building login then throws InvalidEmailException`
  - `given invalid credentials when authenticating then returns error`

**Test Data Builders**:
- Use the Builder pattern for creating test data
- Create builders in a `databuilder/` directory within the test package
- Builders should have sensible default values
- Use companion object factory methods with clear names (e.g., `buildLogin()`)
- Use inline return expressions for `build()` method
- Example structure: `commonTest/.../domain/auth/model/databuilder/LoginBuilder.kt`

Example usage:
```kotlin
@Test
fun `given valid credentials when building login then returns login instance`() {
    val login = buildLogin()
        .withEmail("test@example.com")
        .withPassword("password123")
        .build()

    assertEquals("test@example.com", login.email)
}
```