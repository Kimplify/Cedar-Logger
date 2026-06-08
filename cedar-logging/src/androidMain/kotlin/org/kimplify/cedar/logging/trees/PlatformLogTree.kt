package org.kimplify.cedar.logging.trees

import android.util.Log
import org.kimplify.cedar.logging.LogPriority
import org.kimplify.cedar.logging.LogTree
import org.kimplify.cedar.logging.internal.DEFAULT_TAG
import org.kimplify.cedar.logging.internal.symbol

public actual class PlatformLogTree : LogTree {

    private var maxLogLength = 4_000
    private var enableEmojis: Boolean = true

    public actual fun configureForPlatform(config: PlatformLogConfig.() -> Unit): PlatformLogTree {
        val configuration = PlatformLogConfig().apply(config)

        configuration.androidMaxLogLength?.let { maxLogLength = it }
        enableEmojis = configuration.enableEmojis

        return this
    }

    private fun String.logChunks(prio: Int, tag: String) = chunked(maxLogLength).forEach { Log.println(prio, tag, it) }

    private fun LogPriority.toAndroid(): Int = when (this) {
        LogPriority.VERBOSE -> Log.VERBOSE
        LogPriority.DEBUG -> Log.DEBUG
        LogPriority.INFO -> Log.INFO
        LogPriority.WARNING -> Log.WARN
        LogPriority.ERROR -> Log.ERROR
    }

    public actual override fun isLoggable(tag: String?, priority: LogPriority): Boolean = true

    public actual override fun log(priority: LogPriority, tag: String?, message: String, throwable: Throwable?) {
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
