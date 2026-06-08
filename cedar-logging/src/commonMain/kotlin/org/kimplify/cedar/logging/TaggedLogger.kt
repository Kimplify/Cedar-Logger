package org.kimplify.cedar.logging

import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import org.kimplify.cedar.logging.Cedar.Forest.logToAllTrees

public class TaggedLogger internal constructor(@PublishedApi internal val logTag: String?) {
    // --- Eager, message-first (primary) ---
    public fun v(message: String, throwable: Throwable? = null): Unit =
        logToAllTrees(LogPriority.VERBOSE, logTag, message, throwable)
    public fun d(message: String, throwable: Throwable? = null): Unit =
        logToAllTrees(LogPriority.DEBUG, logTag, message, throwable)
    public fun i(message: String, throwable: Throwable? = null): Unit =
        logToAllTrees(LogPriority.INFO, logTag, message, throwable)
    public fun w(message: String, throwable: Throwable? = null): Unit =
        logToAllTrees(LogPriority.WARNING, logTag, message, throwable)
    public fun e(message: String, throwable: Throwable? = null): Unit =
        logToAllTrees(LogPriority.ERROR, logTag, message, throwable)

    // --- Eager, throwable-first (non-null throwable -> unambiguous) ---
    public fun v(throwable: Throwable, message: String = ""): Unit =
        logToAllTrees(LogPriority.VERBOSE, logTag, message, throwable)
    public fun d(throwable: Throwable, message: String = ""): Unit =
        logToAllTrees(LogPriority.DEBUG, logTag, message, throwable)
    public fun i(throwable: Throwable, message: String = ""): Unit =
        logToAllTrees(LogPriority.INFO, logTag, message, throwable)
    public fun w(throwable: Throwable, message: String = ""): Unit =
        logToAllTrees(LogPriority.WARNING, logTag, message, throwable)
    public fun e(throwable: Throwable, message: String = ""): Unit =
        logToAllTrees(LogPriority.ERROR, logTag, message, throwable)

    // --- Lazy (message built only when a tree is planted) ---
    public inline fun v(throwable: Throwable? = null, message: () -> String) {
        if (Cedar.treeCount != 0) logToAllTrees(LogPriority.VERBOSE, logTag, message(), throwable)
    }

    public inline fun d(throwable: Throwable? = null, message: () -> String) {
        if (Cedar.treeCount != 0) logToAllTrees(LogPriority.DEBUG, logTag, message(), throwable)
    }

    public inline fun i(throwable: Throwable? = null, message: () -> String) {
        if (Cedar.treeCount != 0) logToAllTrees(LogPriority.INFO, logTag, message(), throwable)
    }

    public inline fun w(throwable: Throwable? = null, message: () -> String) {
        if (Cedar.treeCount != 0) logToAllTrees(LogPriority.WARNING, logTag, message(), throwable)
    }

    public inline fun e(throwable: Throwable? = null, message: () -> String) {
        if (Cedar.treeCount != 0) logToAllTrees(LogPriority.ERROR, logTag, message(), throwable)
    }

    public fun log(priority: LogPriority, message: String, throwable: Throwable? = null): Unit =
        logToAllTrees(priority, logTag, message, throwable)

    @OptIn(ExperimentalTime::class)
    public fun scope(priority: LogPriority = LogPriority.DEBUG, message: String): LogScope {
        val startTime = Clock.System.now().toEpochMilliseconds()
        log(priority, "⟹ $message")
        return LogScope(this, priority, message, startTime)
    }
}
