package org.kimplify.cedar.logging

/**
 * Log priority levels, ordered from least to most severe.
 */
public enum class LogPriority {
    VERBOSE,
    DEBUG,
    INFO,
    WARNING,
    ERROR;

    /** Returns true if this priority is at least as severe as [other]. */
    public fun isAtLeast(other: LogPriority): Boolean = ordinal >= other.ordinal
}
