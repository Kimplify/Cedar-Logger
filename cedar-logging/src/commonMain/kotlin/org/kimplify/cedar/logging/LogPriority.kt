package org.kimplify.cedar.logging

/**
 * Log priority levels
 */
public enum class LogPriority {
    VERBOSE,
    DEBUG,
    INFO,
    WARNING,
    ERROR;

    public operator fun compareTo(other: Int): Int = ordinal - other

    public fun isAtLeast(other: LogPriority): Boolean = ordinal >= other.ordinal
}
