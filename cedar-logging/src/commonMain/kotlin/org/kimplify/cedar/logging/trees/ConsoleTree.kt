package org.kimplify.cedar.logging.trees

import org.kimplify.cedar.logging.LogPriority
import org.kimplify.cedar.logging.LogTree
import org.kimplify.cedar.logging.internal.DEFAULT_TAG
import org.kimplify.cedar.logging.internal.symbol

public class ConsoleTree : LogTree {
    private var minPriority = LogPriority.VERBOSE

    public fun withMinPriority(priority: LogPriority): ConsoleTree {
        minPriority = priority
        return this
    }

    public override fun isLoggable(tag: String?, priority: LogPriority): Boolean = priority >= minPriority

    public override fun log(priority: LogPriority, tag: String?, message: String, throwable: Throwable?) {
        val priorityName = priority.name.padEnd(7)
        println("${priority.symbol(emojis = true)} $priorityName [${tag ?: DEFAULT_TAG}] $message")

        throwable?.let {
            println("Exception: ${it.message}")
            it.printStackTrace()
        }
    }
}
