package org.kimplify.cedar.logging.trees

import org.kimplify.cedar.logging.LogPriority
import org.kimplify.cedar.logging.LogTree

@JsName("console")
internal external object Console {
    fun debug(vararg args: String)
    fun log(vararg args: String)
    fun info(vararg args: String)
    fun warn(vararg args: String)
    fun error(vararg args: String)
}

public actual class PlatformLogTree actual constructor() : LogTree {
    private var enableEmojis: Boolean = true

    public actual fun configureForPlatform(config: PlatformLogConfig.() -> Unit): PlatformLogTree {
        val configuration = PlatformLogConfig().apply(config)

        enableEmojis = configuration.enableEmojis

        return this
    }

    public actual override fun isLoggable(tag: String?, priority: LogPriority): Boolean = true

    public actual override fun log(priority: LogPriority, tag: String, message: String, throwable: Throwable?) {
        val symbol = if (enableEmojis) {
            when (priority) {
                LogPriority.VERBOSE -> "🔍"
                LogPriority.DEBUG -> "🐞"
                LogPriority.INFO -> "ℹ️"
                LogPriority.WARNING -> "⚠️"
                LogPriority.ERROR -> "❌"
            }
        } else {
            when (priority) {
                LogPriority.VERBOSE -> "V"
                LogPriority.DEBUG -> "D"
                LogPriority.INFO -> "I"
                LogPriority.WARNING -> "W"
                LogPriority.ERROR -> "E"
            }
        }

        val header = "$symbol [$tag]"
        val errorDump = throwable?.stackTraceToString()
        val fullMessage = buildList {
            add(header)
            add(message)
            errorDump?.let { add(it) }
        }.toTypedArray()

        when (priority) {
            LogPriority.VERBOSE -> Console.log(*fullMessage)
            LogPriority.DEBUG -> Console.debug(*fullMessage)
            LogPriority.INFO -> Console.info(*fullMessage)
            LogPriority.WARNING -> Console.warn(*fullMessage)
            LogPriority.ERROR -> Console.error(*fullMessage)
        }
    }
}
