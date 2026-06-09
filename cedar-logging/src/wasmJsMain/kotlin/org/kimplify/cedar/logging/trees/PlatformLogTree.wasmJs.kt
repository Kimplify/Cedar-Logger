@file:Suppress("MatchingDeclarationName")

package org.kimplify.cedar.logging.trees

import org.kimplify.cedar.logging.LogPriority
import org.kimplify.cedar.logging.LogTree
import org.kimplify.cedar.logging.internal.DEFAULT_TAG
import org.kimplify.cedar.logging.internal.symbol

@JsName("console")
internal external object Console {
    fun debug(vararg args: String)
    fun log(vararg args: String)
    fun info(vararg args: String)
    fun warn(vararg args: String)
    fun error(vararg args: String)
}

public actual fun platformLogTree(configure: PlatformLogConfig.() -> Unit): LogTree =
    ConsolePlatformTree(PlatformLogConfig().apply(configure))

private class ConsolePlatformTree(config: PlatformLogConfig) : LogTree {
    private val enableEmojis = config.enableEmojis

    override fun isLoggable(tag: String?, priority: LogPriority): Boolean = true

    override fun log(priority: LogPriority, tag: String?, message: String, throwable: Throwable?) {
        val header = "${priority.symbol(enableEmojis)} [${tag ?: DEFAULT_TAG}]"
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
