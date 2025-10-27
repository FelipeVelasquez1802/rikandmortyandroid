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
├── app/                                  # Presentation layer (Android app)
│   └── src/
│       ├── main/
│       │   ├── java/com/infinitum/labs/rickandmorty_android/
│       │   │   ├── MainActivity.kt       # Main entry point
│       │   │   └── ui/theme/             # Material3 theme
│       │   │       ├── Color.kt          # Color palette (Purple/Pink scheme)
│       │   │       ├── Theme.kt          # Theme setup with dynamic colors
│       │   │       └── Type.kt           # Typography configuration
│       │   └── AndroidManifest.xml
│       ├── test/                         # App unit tests
│       └── androidTest/                  # App instrumented tests
│
├── domain/                               # Business logic layer (pure Kotlin)
│   └── src/main/java/com/infinitum/labs/domain/
│       └── MyClass.kt                    # Placeholder for domain classes
│
├── data/                                 # Data layer (Android library)
│   └── src/
│       ├── main/
│       │   ├── AndroidManifest.xml
│       │   └── (empty - ready for repositories, data sources)
│       ├── test/                         # Data unit tests
│       └── androidTest/                  # Data instrumented tests
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

### Gradle Configuration
- Version catalog: Edit `gradle/libs.versions.toml` to update dependency versions
- All modules use Java 11 compatibility
- ProGuard is disabled in debug builds, enabled (but not minified) in release builds