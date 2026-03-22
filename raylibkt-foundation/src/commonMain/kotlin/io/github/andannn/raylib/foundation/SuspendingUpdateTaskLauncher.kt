/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.foundation

import io.github.andannn.raylib.runtime.ComponentScope
import io.github.andannn.raylib.runtime.SuspendingUpdateEventScope
import io.github.andannn.raylib.runtime.SuspendingUpdateTask
import io.github.andannn.raylib.runtime.TaskController
import io.github.andannn.raylib.runtime.find
import io.github.andannn.raylib.runtime.remember

/**
 * Creates and registers a task that can use 'suspend' to pause execution until the next frame.
 * This is useful for writing sequential logic (e.g., "Move for 2 seconds, then wait for a click")
 * without manually managing state variables.
 *
 * @param startImmediately Whether to start the task immediately.
 * @param block The suspending code to execute.
 *
 * @return A [TaskController] to manually start or stop the task.
 */
@Suppress("NOTHING_TO_INLINE")
inline fun ComponentScope.rememberSuspendingTask(
    startImmediately: Boolean = true,
    noinline block: suspend SuspendingUpdateEventScope.() -> Unit
): TaskController {
    return remember {
        SuspendingUpdateTask(block)
            .also {
                if (startImmediately) it.start()
            }
    }.also { task ->
        update {
            task.performUpdate(find<WindowContext>().frameTimeSeconds)
        }
    }
}

