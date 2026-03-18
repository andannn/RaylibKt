/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.core

import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.RestrictsSuspension
import kotlin.coroutines.cancellation.CancellationException
import kotlin.coroutines.createCoroutine
import kotlin.coroutines.resume

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
        SuspendingUpdateTask(find<GameContext>(), block)
            .also {
                if (startImmediately) it.start()
            }
    }.also { task ->
        update {
            task.performUpdate(find<WindowContext>().frameTimeSeconds)
        }
    }
}

/**
 * Controls the lifecycle of a registered suspending task.
 */
interface TaskController {
    fun start()
    fun stop()
}

/**
 * The high-level scope for a suspending game task.
 */
@RestrictsSuspension
interface SuspendingUpdateEventScope {
    /**
     * Enters a specialized scope where you can wait for frame updates.
     */
    suspend fun <R> awaitUpdateEventScope(block: suspend AwaitUpdateEventScope.() -> R): R
}

/**
 * Suspends the coroutine until the given [condition] is satisfied.
 *
 * This function polls the [condition] on every frame update. It is non-blocking
 * to the main thread as it yields execution between checks using [AwaitUpdateEventScope.awaitUpdateEvent].
 *
 * @param condition A lambda that is evaluated every frame; returns true to resume.
 */
suspend inline fun SuspendingUpdateEventScope.await(crossinline condition: GameContext.() -> Boolean) {
    awaitUpdateEventScope {
        while (!condition()) {
            awaitUpdateEvent()
        }
    }
}

/**
 * A scope that provides access to the [GameContext] and the ability to wait for the next frame.
 */
@RestrictsSuspension
interface AwaitUpdateEventScope : GameContext {
    /**
     * Suspends the execution and resumes on the next frame update.
     *
     * @return The deltaTime (time elapsed since last frame).
     */
    suspend fun awaitUpdateEvent(): Float
}

internal class FinishHandleInputException : CancellationException(message = "task is cancelled by user")

@PublishedApi
internal class SuspendingUpdateTask(
    private val gameContext: GameContext,
    private val block: suspend SuspendingUpdateEventScope.() -> Unit,
) : SuspendingUpdateEventScope, UpdateHandler, TaskController {
    private var activeHandler: EventHandlerCoroutine<*>? = null

    override suspend fun <R> awaitUpdateEventScope(block: suspend AwaitUpdateEventScope.() -> R): R =
        suspendCancellableCoroutine { continuation ->
            val handlerCoroutine = EventHandlerCoroutine(continuation, gameContext)
            check(activeHandler == null) { "handler already registered." }

            activeHandler = handlerCoroutine
            block.createCoroutine(handlerCoroutine, handlerCoroutine).resume(Unit)


            continuation.invokeOnCancellation { handlerCoroutine.cancel(it) }
        }

    private fun dispatchUpdateEvent(deltaTime: Float) {
        val current = activeHandler
        current?.doUpdate(deltaTime)
    }

    override fun performUpdate(deltaTime: Float) {
        dispatchUpdateEvent(deltaTime)
    }

    override fun start() {
        activeHandler?.cancel(FinishHandleInputException())
        createTask().resume(Unit)
    }

    override fun stop() {
        activeHandler?.cancel(FinishHandleInputException())
    }

    private fun createTask(): Continuation<Unit> {
        return block.createCoroutine(this, object : Continuation<Unit> {
            override val context: CoroutineContext = EmptyCoroutineContext

            override fun resumeWith(result: Result<Unit>) {
                val exceptionOrNull = result.exceptionOrNull()
                if (exceptionOrNull != null && exceptionOrNull !is FinishHandleInputException) {
                    throw exceptionOrNull
                }
            }
        })
    }

    private inner class EventHandlerCoroutine<R>(
        private val completion: Continuation<R>,
        gameContext: GameContext
    ) : AwaitUpdateEventScope,
        Continuation<R>,
        GameContext by gameContext {

        private var awaiter: CancellableContinuation<Float>? = null

        override fun resumeWith(result: Result<R>) {
            activeHandler = null
            completion.resumeWith(result)
        }

        fun doUpdate(deltaTime: Float) {
            awaiter?.run {
                awaiter = null
                resume(deltaTime)
            }
        }

        fun cancel(cause: Throwable?) {
            awaiter?.cancel(cause)
            awaiter = null
        }

        override val context: CoroutineContext = EmptyCoroutineContext

        override suspend fun awaitUpdateEvent(): Float = suspendCancellableCoroutine { continuation ->
            awaiter = continuation
        }
    }
}

