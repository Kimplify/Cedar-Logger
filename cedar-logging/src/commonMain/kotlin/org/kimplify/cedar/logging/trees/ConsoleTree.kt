package org.kimplify.cedar.logging.trees

import org.kimplify.cedar.logging.LogPriority
import org.kimplify.cedar.logging.LogTree

public class ConsoleTree : LogTree {
    private var minPriority = LogPriority.VERBOSE

    public fun withMinPriority(priority: LogPriority): ConsoleTree {
        minPriority = priority
        return this
    }

    public override fun isLoggable(tag: String?, priority: LogPriority): Boolean = priority >= minPriority

    public override fun log(priority: LogPriority, tag: String, message: String, throwable: Throwable?) {
        if (!isLoggable(tag, priority)) {
            return
        }

        val priorityIcon = when (priority) {
            LogPriority.VERBOSE -> "🔍"
            LogPriority.DEBUG -> "🐛"
            LogPriority.INFO -> "ℹ️"
            LogPriority.WARNING -> "⚠️"
            LogPriority.ERROR -> "❌"
        }

        val priorityName = priority.name.padEnd(7)
        val formattedMessage = "$priorityIcon $priorityName [$tag] $message"

        println(formattedMessage)

        throwable?.let {
            println("Exception: ${it.message}")
            it.printStackTrace()
        }
    }
}
