package org.kimplify.cedar.logging

/**
 * A tree-like logging system that allows planting different log handlers.
 */
public interface LogTree {
    /**
     * Log a message with the specified priority and optional throwable.
     * [tag] is null when the originating call did not provide an explicit tag.
     */
    public fun log(priority: LogPriority, tag: String?, message: String, throwable: Throwable? = null)

    /** For handling initialization. */
    public fun setup() {}

    /** For handling cleanup. */
    public fun tearDown() {}

    /** Called when determining if a certain priority is loggable. */
    public fun isLoggable(tag: String?, priority: LogPriority): Boolean = true
}
