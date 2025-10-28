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

### Key Dependencies
- **Compose BOM**: 2024.09.00 (version-aligned Compose dependencies)
- **Lifecycle Runtime**: 2.6.1
- **Activity Compose**: 1.11.0
- **Testing**: JUnit 4.13.2, AndroidX Test, Espresso, Compose UI Test
- **Material**: Material3 (Compose), Material Components 1.10.0 (data module)

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
- Theme: `RickAndMortyAndroidTheme` supports dark/light mode and dynamic colors (Android 12+)
- Color scheme: Purple-based palette with tertiary pink accents
- Preview functions: Annotated with `@Preview` for Compose tooling

### Testing Strategy
- **Unit tests**: Place in `src/test/` (all modules) - no Android dependencies
- **Instrumented tests**: Place in `src/androidTest/` (app and data modules only)
- Test runner: `androidx.test.runner.AndroidJUnitRunner`
- Domain module can only have JUnit tests (no Android instrumentation)

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