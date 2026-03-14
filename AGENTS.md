# Muzyakich Project

Muzyakich is a modern, native Android application built with Kotlin. It provides a seamless,
reactive user experience for managing custom playlists and exploring personal libraries of songs,
albums, and artists.

## Architecture

This project is a modern Android application that follows the official architecture guidance from
Google. It is a reactive, single-activity app that uses the following:

- **UI:** Built entirely with Jetpack Compose, including Material 3 Expressive components and
  adaptive layouts for different screen sizes.
- **State Management:** Unidirectional Data Flow (UDF) is implemented using Kotlin Coroutines and
  `Flow`s. `ViewModel`s act as state holders, exposing UI state as streams of data.
- **Dependency Injection:** Hilt is used for dependency injection throughout the app, simplifying
  the management of dependencies and improving testability.
- **Navigation:** Navigation is handled by Jetpack Navigation 3 for Compose, allowing for a
  declarative and type-safe way to navigate between screens.
- **Data:** The data layer is implemented using the repository pattern.
    - **Local Data:** MediaStore and DataStore are used for local data persistence.

## Modules

The main Android app with features lives in the `mobile/` folder and core and shared modules in
`core/`.

## Localization

All `strings.xml` files with translations are located in the `core/locales/` folder. The strings
within them are stored strictly in alphabetical order.

## Commands to Build & Test

The `mobile/` folder have two product flavors: `demo` and `prod`, and two build types:
`debug` and `release`.

- Build: `./gradlew assembleDemoDebug`.