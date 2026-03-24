package org.kimplify.cedar.logging

import kotlin.properties.Delegates
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.SynchronizedObject
import kotlinx.coroutines.internal.synchronized

/**
 * A lightweight, extensible logging system for Kotlin Multiplatform projects.
 */
public class Cedar private constructor() {

    public companion object Forest {
        @OptIn(InternalCoroutinesApi::class)
        private val treeLock = SynchronizedObject()
        private val logTrees = mutableListOf<LogTree>()
        private var treeArray: Array<LogTree> by Delegates.observable(emptyArray()) { _, _, _ ->
            hasPlantedTrees = treeArray.isNotEmpty()
        }
        private var hasPlantedTrees = false

        public fun tag(tag: String): TaggedLogger = TaggedLogger(tag)

        public fun getLogger(tag: String? = null): TaggedLogger = TaggedLogger(tag ?: "AppLogger")

        @OptIn(InternalCoroutinesApi::class)
        public fun plant(tree: LogTree) {
            tree.setup()
            synchronized(treeLock) {
                logTrees.add(tree)
                treeArray = logTrees.toTypedArray()
            }
        }

        @OptIn(InternalCoroutinesApi::class)
        public fun plant(vararg trees: LogTree) {
            for (tree in trees) {
                tree.setup()
            }

            synchronized(treeLock) {
                logTrees.addAll(trees)
                treeArray = logTrees.toTypedArray()
            }
        }

        /**
         * Remove a previously planted tree
         */
        @OptIn(InternalCoroutinesApi::class)
        public fun uproot(tree: LogTree) {
            synchronized(treeLock) {
                if (logTrees.remove(tree)) {
                    tree.tearDown()
                    treeArray = logTrees.toTypedArray()
                }
            }
        }

        @OptIn(InternalCoroutinesApi::class)
        public fun clearForest() {
            synchronized(treeLock) {
                logTrees.forEach { it.tearDown() }
                logTrees.clear()
                treeArray = emptyArray()
            }
        }

        @OptIn(InternalCoroutinesApi::class)
        public fun forest(): List<LogTree> {
            synchronized(treeLock) {
                return logTrees.toList()
            }
        }

        public val treeCount: Int
            get() = treeArray.size

        public fun v(message: String, throwable: Throwable? = null) {
            getLogger().v(message, throwable)
        }

        public fun d(message: String, throwable: Throwable? = null) {
            getLogger().d(message, throwable)
        }

        public fun i(message: String, throwable: Throwable? = null) {
            getLogger().i(message, throwable)
        }

        public fun w(message: String, throwable: Throwable? = null) {
            getLogger().w(throwable, message)
        }

        public fun w(throwable: Throwable? = null, message: String = "") {
            getLogger().w(throwable, message)
        }

        public fun e(throwable: Throwable? = null, message: String = "") {
            getLogger().e(throwable, message)
        }

        public fun e(message: String, throwable: Throwable? = null) {
            getLogger().e(throwable, message)
        }

        internal fun logToAllTrees(priority: LogPriority, tag: String, message: String, throwable: Throwable? = null) {
            val trees = treeArray

            for (tree in trees) {
                if (tree.isLoggable(tag, priority)) {
                    tree.log(priority, tag, message, throwable)
                }
            }
        }
    }
}
