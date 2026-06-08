package org.kimplify.cedar.logging.trees

import java.util.logging.Level
import java.util.logging.Logger
import org.kimplify.cedar.logging.LogPriority
import org.kimplify.cedar.logging.LogTree
import org.kimplify.cedar.logging.internal.DEFAULT_TAG
import org.kimplify.cedar.logging.internal.symbol

public actual class PlatformLogTree actual constructor() : LogTree {
    private var logger: Logger = Logger.getLogger(PlatformLogTree::class.java.name)
    private var enableEmojis: Boolean = true

    public actual fun configureForPlatform(config: PlatformLogConfig.() -> Unit): PlatformLogTree {
        val configuration = PlatformLogConfig().apply(config)

        configuration.jvmLoggerName?.let {
            logger = Logger.getLogger(it)
        }
        enableEmojis = configuration.enableEmojis

        return this
    }

    private fun LogPriority.toLevel(): Level = when (this) {
        LogPriority.VERBOSE, LogPriority.DEBUG -> Level.FINEST
        LogPriority.INFO -> Level.INFO
        LogPriority.WARNING -> Level.WARNING
        LogPriority.ERROR -> Level.SEVERE
    }

    public actual override fun isLoggable(tag: String?, priority: LogPriority): Boolean =
        logger.isLoggable(priority.toLevel())

    public actual override fun log(priority: LogPriority, tag: String?, message: String, throwable: Throwable?) {
        val header = "[${priority.symbol(enableEmojis)} ${tag ?: DEFAULT_TAG}]"
        val fullMessage = buildString {
            append(header).append(" ").append(message)
            throwable?.let {
                append("\n").append(it.stackTraceToString())
            }
        }

        val level = priority.toLevel()
        if (throwable != null) {
            logger.log(level, fullMessage, throwable)
        } else {
            logger.log(level, fullMessage)
        }
    }
}
