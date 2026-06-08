# 🌲 Cedar Logger

<p align="center">
  <img src="https://img.shields.io/badge/Kotlin-Multiplatform-blue?style=for-the-badge&logo=kotlin"/>
  <img src="https://img.shields.io/badge/Platforms-Android%20·%20iOS%20·%20JVM%20·%20JS%20·%20wasmJs-green?style=for-the-badge"/>
  <img src="https://img.shields.io/badge/License-Apache%202.0-yellow?style=for-the-badge"/>
</p>

A lightweight, Timber-style logging library for Kotlin Multiplatform. **Plant trees** to send logs wherever you want — the platform console, a crash reporter, your own UI — with tagging, scoped timing, and zero-cost lazy messages.

If you've used [Timber](https://github.com/JakeWharton/timber), the API will feel immediately familiar.

## Why Cedar

- **Truly multiplatform** — Android, iOS, JVM, JS, and wasmJs from one API.
- **Zero runtime dependencies** — no coroutines, no datetime; just the Kotlin stdlib.
- **Lock-free and thread-safe** — the tree registry uses a copy-on-write atomic, so logging never blocks.
- **Pluggable trees** — built-in console and platform-native trees, or write your own in a few lines.
- **Zero-cost when disabled** — lazy `{ }` overloads skip string building when nothing is listening.

## Install

```kotlin
dependencies {
    implementation("org.kimplify:cedar-logging:0.3.0")
}
```

Gradle metadata resolves the right variant per target automatically — `cedar-logging` is all you need in `commonMain`.

<details>
<summary><b>iOS integration (framework export / CocoaPods)</b></summary>

**Framework export (recommended)** — use Cedar directly from Swift:

```kotlin
// iOS app build.gradle.kts
listOf(iosX64(), iosArm64(), iosSimulatorArm64()).forEach { target ->
    target.binaries.framework {
        export("org.kimplify:cedar-logging:0.3.0")
        baseName = "YourApp"
        isStatic = true
    }
}

// commonMain
api("org.kimplify:cedar-logging:0.3.0")
```

**CocoaPods:**

```ruby
pod 'CedarLogger', :git => 'https://github.com/Kimplify/Cedar-Logger.git', :tag => 'v0.3.0'
```

Framework: `CedarLogger` · static · min iOS 12.0 · arm64 + x64 + simulator.
</details>

## Quick start

```kotlin
// Plant a tree once, at startup
Cedar.plant(platformLogTree())   // logs to Logcat / os_log / java.util.logging / console

Cedar.d("Debug message")
Cedar.i("App started")
Cedar.e(throwable, "Something failed")
```

## Logging

```kotlin
// Levels
Cedar.v("Verbose")
Cedar.d("Debug")
Cedar.i("Info")
Cedar.w("Warning")
Cedar.e("Error")

// With a throwable (throwable-first or message-first both work)
Cedar.e(exception, "Checkout failed")
Cedar.w("Retrying request", exception)

// Tags — group logs by area
Cedar.tag("Network").i("API call succeeded")
Cedar.tag("Database").d("Query executed")

// Lazy — the lambda runs only if a tree is planted
Cedar.d { "Expensive: ${dumpState()}" }
Cedar.tag("Net").e(throwable) { "Request failed for $url" }

// Scoped timing — logs start, end, and elapsed time
Cedar.tag("Startup").scope(message = "Warm cache").use {
    warmCache()
}
```

> **Lazy logging** is the recommended default for hot paths and expensive messages: when no tree is planted, the `{ }` lambda is never invoked, so the string is never built.

## Built-in trees

### `platformLogTree`

Routes to each platform's native facility (Logcat, `os_log`, `java.util.logging`, `console`) and is configurable:

```kotlin
Cedar.plant(platformLogTree {
    iosSubsystem = "com.myapp.network"   // groups logs in Console.app
    iosCategory = "API"
    androidMaxLogLength = 2000           // chunk long Logcat lines
    jvmLoggerName = "MyApp.Logger"
    enableEmojis = true
})
```

### `ConsoleTree`

Plain `println` output with level icons — handy for tests and simple JVM/JS targets:

```kotlin
Cedar.plant(ConsoleTree().withMinPriority(LogPriority.DEBUG))
// 🐞 DEBUG   [Network] API call completed
```

## Custom trees

A tree is anything that implements `LogTree.log`. Route logs to a crash reporter, a file, an analytics pipeline, or your UI:

```kotlin
import org.kimplify.cedar.logging.LogPriority
import org.kimplify.cedar.logging.LogTree

class CrashReportingTree : LogTree {
    // Only forward warnings and errors to the reporter
    override fun isLoggable(tag: String?, priority: LogPriority): Boolean =
        priority.isAtLeast(LogPriority.WARNING)

    override fun log(priority: LogPriority, tag: String?, message: String, throwable: Throwable?) {
        val label = tag ?: "App"
        Reporter.log(priority.name, label, message)
        if (throwable != null) Reporter.recordException(throwable)
    }
}

Cedar.plant(CrashReportingTree())
```

`setup()` and `tearDown()` hooks are available for trees that need initialization or cleanup.

## Managing trees

```kotlin
Cedar.plant(ConsoleTree(), CrashReportingTree())   // plant several at once
Cedar.treeCount                                    // how many are planted

val tree = ConsoleTree()
Cedar.plant(tree)
Cedar.uproot(tree)                                 // remove one
Cedar.clearForest()                                // remove all
```

All planted trees receive every log; each decides via `isLoggable` what to keep.

## Demo app

A Compose Multiplatform sample (Android / iOS / JVM / wasmJs) shows live logging, tagging, scoped timing, and tree management:

```bash
./gradlew :sample:run          # JVM desktop
```

## Migrating to 0.3.0

- **Nullable tags.** `LogTree.log` / `isLoggable` now take `tag: String?`; untagged logs pass `null` (previously the sentinel `"AppLogger"`).
- **Throwable-first overloads** take a non-null `Throwable` — use the message-first overloads when there is no throwable.
- **`platformLogTree { }`** factory replaces the old `PlatformLogTree()` class.
- **`LogPriority.compareTo(Int)` removed** — compare against other `LogPriority` values (or use `isAtLeast`).

## License

Apache License, Version 2.0. See [LICENSE](LICENSE).

## Acknowledgments

Inspired by [Timber](https://github.com/JakeWharton/timber), built on [Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html).

---

<p align="center">Made with ❤️ for the Kotlin Multiplatform community</p>
