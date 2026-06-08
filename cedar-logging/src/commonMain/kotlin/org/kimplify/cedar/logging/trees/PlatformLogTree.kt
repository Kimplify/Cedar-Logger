@file:Suppress("MatchingDeclarationName")

package org.kimplify.cedar.logging.trees

import org.kimplify.cedar.logging.LogTree

/**
 * Platform-specific configuration for logging behavior.
 * Each platform applies only the settings relevant to it.
 */
public class PlatformLogConfig {
    public var iosSubsystem: String? = null
    public var iosCategory: String? = null

    public var androidMaxLogLength: Int? = null

    public var jvmLoggerName: String? = null

    public var enableEmojis: Boolean = true
}

/**
 * Creates a [LogTree] that logs through the current platform's native facility
 * (Logcat on Android, os_log on iOS, java.util.logging on JVM, console on JS/Wasm).
 *
 * Usage:
 * ```
 * Cedar.plant(platformLogTree())
 *
 * Cedar.plant(platformLogTree {
 *     iosSubsystem = "com.example.app"
 *     enableEmojis = true
 * })
 * ```
 */
public expect fun platformLogTree(configure: PlatformLogConfig.() -> Unit = {}): LogTree
