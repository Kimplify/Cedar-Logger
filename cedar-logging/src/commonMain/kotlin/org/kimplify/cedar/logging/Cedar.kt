package org.kimplify.cedar.logging

import kotlin.concurrent.atomics.AtomicReference
import kotlin.concurrent.atomics.ExperimentalAtomicApi

/**
 * A lightweight, extensible logging system for Kotlin Multiplatform projects.
 *
 * The set of planted [LogTree]s is stored in a lock-free copy-on-write array: reads
 * (the hot logging path) never take a lock, and writes (plant/uproot/clear) publish a
 * fresh array via compare-and-set so concurrent readers always see a consistent snapshot.
 */
@OptIn(ExperimentalAtomicApi::class)
public class Cedar private constructor() {

    public companion object Forest {
        private val treesRef = AtomicReference<Array<LogTree>>(emptyArray())

        /** Cached logger used for untagged top-level calls, avoiding per-call allocation. */
        private val defaultLogger = TaggedLogger("AppLogger")

        public fun tag(tag: String): TaggedLogger = TaggedLogger(tag)

        public fun getLogger(tag: String? = null): TaggedLogger = if (tag == null) defaultLogger else TaggedLogger(tag)

        public fun plant(tree: LogTree) {
            tree.setup()
            while (true) {
                val old = treesRef.load()
                if (treesRef.compareAndSet(old, old + tree)) return
            }
        }

        public fun plant(vararg trees: LogTree) {
            for (tree in trees) {
                tree.setup()
            }
            while (true) {
                val old = treesRef.load()
                if (treesRef.compareAndSet(old, old + trees)) return
            }
        }

        /** Remove a previously planted tree (the first matching instance). */
        public fun uproot(tree: LogTree) {
            while (true) {
                val old = treesRef.load()
                val index = old.indexOf(tree)
                if (index < 0) return
                val updated = ArrayList<LogTree>(old.size - 1)
                old.forEachIndexed { i, t -> if (i != index) updated.add(t) }
                if (treesRef.compareAndSet(old, updated.toTypedArray())) {
                    tree.tearDown()
                    return
                }
            }
        }

        public fun clearForest() {
            val old = treesRef.exchange(emptyArray())
            old.forEach { it.tearDown() }
        }

        public fun forest(): List<LogTree> = treesRef.load().toList()

        public val treeCount: Int
            get() = treesRef.load().size

        public fun v(message: String, throwable: Throwable? = null) {
            defaultLogger.v(message, throwable)
        }

        public fun d(message: String, throwable: Throwable? = null) {
            defaultLogger.d(message, throwable)
        }

        public fun i(message: String, throwable: Throwable? = null) {
            defaultLogger.i(message, throwable)
        }

        public fun w(message: String, throwable: Throwable? = null) {
            defaultLogger.w(message, throwable)
        }

        public fun w(throwable: Throwable? = null, message: String = "") {
            defaultLogger.w(throwable, message)
        }

        public fun e(throwable: Throwable? = null, message: String = "") {
            defaultLogger.e(throwable, message)
        }

        public fun e(message: String, throwable: Throwable? = null) {
            defaultLogger.e(message, throwable)
        }

        @PublishedApi
        internal fun logToAllTrees(priority: LogPriority, tag: String, message: String, throwable: Throwable? = null) {
            val trees = treesRef.load()
            for (tree in trees) {
                if (tree.isLoggable(tag, priority)) {
                    tree.log(priority, tag, message, throwable)
                }
            }
        }
    }
}
