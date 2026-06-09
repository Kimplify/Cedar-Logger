package org.kimplify.cedar.logging.trees

import android.util.Log
import org.kimplify.cedar.logging.LogPriority
import org.kimplify.cedar.logging.LogTree
import org.kimplify.cedar.logging.internal.DEFAULT_TAG
import org.kimplify.cedar.logging.internal.symbol

public actual fun platformLogTree(configure: PlatformLogConfig.() -> Unit): LogTree =
    AndroidLogTree(PlatformLogConfig().apply(configure))

private class AndroidLogTree(config: PlatformLogConfig) : LogTree {

    private val maxLogLength = config.androidMaxLogLength ?: 4_000
    private val enableEmojis = config.enableEmojis

    private fun String.logChunks(prio: Int, tag: String) = chunked(maxLogLength).forEach { Log.println(prio, tag, it) }

    private fun LogPriority.toAndroid(): Int = when (this) {
        LogPriority.VERBOSE -> Log.VERBOSE
        LogPriority.DEBUG -> Log.DEBUG
        LogPriority.INFO -> Log.INFO
        LogPriority.WARNING -> Log.WARN
        LogPriority.ERROR -> Log.ERROR
    }

    override fun isLoggable(tag: String?, priority: LogPriority): Boolean = true

    override fun log(priority: LogPriority, tag: String?, message: String, throwable: Throwable?) {
        val prio = priority.toAndroid()
        val safeTag = (tag ?: DEFAULT_TAG).take(23)

        val full = buildString {
            append("${priority.symbol(enableEmojis)} $message")
            throwable?.let {
                appendLine()
                append(Log.getStackTraceString(it))
            }
        }

        full.logChunks(prio, safeTag)
    }
}
