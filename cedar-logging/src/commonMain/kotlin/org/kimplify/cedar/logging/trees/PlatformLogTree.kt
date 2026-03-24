package org.kimplify.cedar.logging.trees

import org.kimplify.cedar.logging.LogPriority
import org.kimplify.cedar.logging.LogTree

/**
 * Platform-specific configuration for logging behavior.
 * Contains configuration options for all platforms, but each platform will only apply relevant settings.
 */
public class PlatformLogConfig {
    public var iosSubsystem: String? = null
    public var iosCategory: String? = null

    public var androidMaxLogLength: Int? = null

    public var jvmLoggerName: String? = null

    public var enableEmojis: Boolean = true
}

/**
 * Debug tree that provides platform-specific optimized logging.
 * Each platform has a specific implementation that takes advantage of native capabilities.
 *
 * Usage:
 * ```
 * // Basic usage (works on all platforms)
 * Cedar.plant(PlatformLogTree())
 *
 * // Platform-specific configuration
 * Cedar.plant(PlatformLogTree().configureForPlatform {
 *     // Platform-specific options will be available here
 * })
 * ```
 */
public expect class PlatformLogTree() : LogTree {
    public override fun isLoggable(tag: String?, priority: LogPriority): Boolean
    public override fun log(priority: LogPriority, tag: String, message: String, throwable: Throwable?)

    /**
     * Configure platform-specific logging options.
     * This method allows platform-specific customization without breaking the common API.
     */
    public fun configureForPlatform(config: PlatformLogConfig.() -> Unit): PlatformLogTree
}
