# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added

- CI workflow for PR quality checks (Spotless, Detekt, API compatibility) and tests (JVM, iOS)
- Quality tooling plugins: Binary Compatibility Validator, Spotless, Detekt, Dokka, Kover
- Initial API dump for binary compatibility tracking
- `.editorconfig` for consistent code style
- `explicitApi()` mode for stricter API visibility
- `androidApp` module as the Android entry point (replacing embedded Android source sets)

### Changed

- Migrated `cedar-logging` from `com.android.library` to `com.android.kotlin.multiplatform.library` (AGP 9)
- Migrated `sample` module to `android-multiplatform-library` plugin
- Updated AGP to 9.0.1, Gradle to 9.1, Compose to 1.10.3, Kotlin to 2.2.20
- Updated Gradle wrapper
- Applied Spotless formatting across the codebase

### Fixed

- Removed broken CI tasks (`testReleaseUnitTest`, `koverHtmlReport`) that no longer exist after AGP 9 migration

## [0.2.2] - 2025-03-11

### Changed

- Updated app version and dependencies

## [0.2.1] - 2025-02-25

### Fixed

- Updated log format specifier for public visibility in `PlatformLogTree`

## [0.2.0] - 2025-02-18

### Added

- WebAssembly and JavaScript support with logging enhancements
- HTML entry point for browser targets
- Signing for all Maven publications
- `workflow_dispatch` trigger for manual publishing

### Fixed

- Downgraded `maven-publish` version to 0.33.0
- Ensured newline at end of file in `libs.versions.toml`

## [0.1.0] - 2025-01-28

### Added

- Initial release of Cedar Logger
- Kotlin Multiplatform logging library with support for Android, iOS, JVM, JS, and wasmJs
- `Cedar` API with tree-based logging architecture
- `PlatformLogTree` with configurable logging options and emoji support
- `ConsoleTree` for cross-platform console output
- `LogScope` for structured logging blocks
- `TaggedLogger` for tag-based log filtering
- iOS framework integration with UI for logging display
- CocoaPods support
- Maven Central publishing

[Unreleased]: https://github.com/Kimplify/Cedar-Logger/compare/0.2.2...HEAD
[0.2.2]: https://github.com/Kimplify/Cedar-Logger/compare/0.2.1...0.2.2
[0.2.1]: https://github.com/Kimplify/Cedar-Logger/compare/0.2.0...0.2.1
[0.2.0]: https://github.com/Kimplify/Cedar-Logger/compare/0.1.0...0.2.0
[0.1.0]: https://github.com/Kimplify/Cedar-Logger/releases/tag/0.1.0
