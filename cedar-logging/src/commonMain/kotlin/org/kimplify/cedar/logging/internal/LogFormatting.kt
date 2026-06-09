package org.kimplify.cedar.logging.internal

import org.kimplify.cedar.logging.LogPriority

/** Fallback tag the built-in trees display when a log call provides no explicit tag. */
internal const val DEFAULT_TAG: String = "Cedar"

/**
 * Maps a [LogPriority] to the single symbol the built-in trees prefix to a line.
 * Emoji form when [emojis] is true, otherwise the single-letter level code.
 */
internal fun LogPriority.symbol(emojis: Boolean): String = if (emojis) {
    when (this) {
        LogPriority.VERBOSE -> "🔍"
        LogPriority.DEBUG -> "🐞"
        LogPriority.INFO -> "ℹ️"
        LogPriority.WARNING -> "⚠️"
        LogPriority.ERROR -> "❌"
    }
} else {
    when (this) {
        LogPriority.VERBOSE -> "V"
        LogPriority.DEBUG -> "D"
        LogPriority.INFO -> "I"
        LogPriority.WARNING -> "W"
        LogPriority.ERROR -> "E"
    }
}
