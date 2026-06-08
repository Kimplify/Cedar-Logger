package org.kimplify.cedar.logging.trees

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.autoreleasepool
import kotlinx.cinterop.ptr
import org.kimplify.cedar.logging.LogPriority
import org.kimplify.cedar.logging.LogTree
import org.kimplify.cedar.logging.internal.DEFAULT_TAG
import org.kimplify.cedar.logging.internal.symbol
import platform.darwin.OS_LOG_DEFAULT
import platform.darwin.OS_LOG_TYPE_DEBUG
import platform.darwin.OS_LOG_TYPE_DEFAULT
import platform.darwin.OS_LOG_TYPE_ERROR
import platform.darwin.OS_LOG_TYPE_FAULT
import platform.darwin.OS_LOG_TYPE_INFO
import platform.darwin.__dso_handle
import platform.darwin._os_log_internal
import platform.darwin.os_log_create

@OptIn(ExperimentalForeignApi::class)
public actual fun platformLogTree(configure: PlatformLogConfig.() -> Unit): LogTree =
    IosLogTree(PlatformLogConfig().apply(configure))

/**
 * iOS-specific debug tree implementation.
 * Uses Apple's os_log for better integration with Xcode console and Console.app.
 * Supports custom subsystems and categories for organized logging.
 */
@OptIn(ExperimentalForeignApi::class)
private class IosLogTree(private val config: PlatformLogConfig) : LogTree {

    private val customLogObject =
        if (config.iosSubsystem != null) {
            os_log_create(config.iosSubsystem, config.iosCategory ?: "General")
        } else {
            null
        }

    override fun isLoggable(tag: String?, priority: LogPriority): Boolean = true

    @OptIn(BetaInteropApi::class)
    override fun log(priority: LogPriority, tag: String?, message: String, throwable: Throwable?) {
        val header = "${priority.symbol(config.enableEmojis)} [${tag ?: DEFAULT_TAG}]"
        val errorDump = throwable?.stackTraceToString()
        val allText = buildList {
            add(header)
            add(message)
            if (errorDump != null) add(errorDump)
        }.joinToString(" ")

        val logObject = customLogObject ?: OS_LOG_DEFAULT

        autoreleasepool {
            allText.chunked(1000).forEach { chunk ->
                _os_log_internal(
                    __dso_handle.ptr,
                    logObject,
                    mapToOsLogType(priority),
                    "%{public}s",
                    chunk
                )
            }
        }
    }

    private fun mapToOsLogType(priority: LogPriority): UByte = when (priority) {
        LogPriority.VERBOSE -> OS_LOG_TYPE_DEFAULT
        LogPriority.DEBUG -> OS_LOG_TYPE_DEBUG
        LogPriority.INFO -> OS_LOG_TYPE_INFO
        LogPriority.WARNING -> OS_LOG_TYPE_ERROR
        LogPriority.ERROR -> OS_LOG_TYPE_FAULT
    }
}
