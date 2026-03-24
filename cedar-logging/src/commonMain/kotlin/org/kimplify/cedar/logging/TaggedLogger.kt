package org.kimplify.cedar.logging

import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import org.kimplify.cedar.logging.Cedar.Forest.logToAllTrees

public class TaggedLogger internal constructor(private val logTag: String) {
    public fun v(message: String, throwable: Throwable? = null) {
        logToAllTrees(LogPriority.VERBOSE, logTag, message, throwable)
    }

    public fun d(throwable: Throwable? = null, message: String) {
        logToAllTrees(LogPriority.DEBUG, logTag, message, throwable)
    }

    public fun d(message: String, throwable: Throwable? = null) {
        logToAllTrees(LogPriority.DEBUG, logTag, message, throwable)
    }

    public fun i(message: String, throwable: Throwable? = null) {
        logToAllTrees(LogPriority.INFO, logTag, message, throwable)
    }

    public fun w(throwable: Throwable?, message: String = "") {
        logToAllTrees(LogPriority.WARNING, logTag, message, throwable)
    }

    public fun w(message: String, throwable: Throwable? = null) {
        logToAllTrees(LogPriority.WARNING, logTag, message, throwable)
    }

    public fun e(throwable: Throwable? = null, message: String = "") {
        logToAllTrees(LogPriority.ERROR, logTag, message, throwable)
    }

    public fun e(message: String = "", throwable: Throwable? = null) {
        logToAllTrees(LogPriority.ERROR, logTag, message, throwable)
    }

    public fun log(priority: LogPriority, message: String, throwable: Throwable? = null) {
        logToAllTrees(priority, logTag, message, throwable)
    }

    @OptIn(ExperimentalTime::class)
    public fun scope(priority: LogPriority = LogPriority.DEBUG, message: String): LogScope {
        val startTime = Clock.System.now().toEpochMilliseconds()
        log(priority, "⟹ $message")
        return LogScope(this, priority, message, startTime)
    }
}
