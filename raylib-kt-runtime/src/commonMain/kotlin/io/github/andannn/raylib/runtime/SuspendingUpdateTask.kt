/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.runtime

import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.RestrictsSuspension
import kotlin.coroutines.cancellation.CancellationException
import kotlin.coroutines.createCoroutine
import kotlin.coroutines.resume
import kotlin.time.Duration
import kotlin.time.DurationUnit

/**
 * Suspends the coroutine until the given [condition] is satisfied.
 *
 * This function polls the [condition] on every frame update. It is non-blocking
 * to the main thread as it yields execution between checks using [AwaitUpdateEventScope.awaitUpdateEvent].
 *
 * @param condition A lambda that is evaluated every frame; returns true to resume.
 */
context(scope: SuspendingUpdateEventScope)
suspend inline fun await(crossinline condition: () -> Boolean) {
    scope.awaitUpdateEventScope {
        while (!condition()) {
            awaitUpdateEvent()
        }
    }
}

context(scope: SuspendingUpdateEventScope)
suspend fun awaitDuration(
    duration: Duration,
    onProgress: (Float) -> Unit = {}
) = scope.awaitUpdateEventScope {
    val totalSeconds = duration.toDouble(DurationUnit.SECONDS).toFloat()
    var elapsedTime = 0f

    while (true) {
        val dt = awaitUpdateEvent()
        elapsedTime += dt

        val fraction = (elapsedTime / totalSeconds).coerceIn(0f, 1f)

        onProgress(fraction)

        if (fraction >= 1f) break
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

@RestrictsSuspension
interface AwaitUpdateEventScope {
    /**
     * Suspends the execution and resumes on the next frame update.
     *
     * @return The deltaTime (time elapsed since last frame).
     */
    suspend fun awaitUpdateEvent(): Float
}

internal class FinishHandleInputException : CancellationException(message = "task is cancelled by user")

class SuspendingUpdateTask(
    private val block: suspend SuspendingUpdateEventScope.() -> Unit,
) : SuspendingUpdateEventScope, TaskController {
    private var activeHandler: EventHandlerCoroutine<*>? = null

    override suspend fun <R> awaitUpdateEventScope(block: suspend AwaitUpdateEventScope.() -> R): R =
        suspendCancellableCoroutine { continuation ->
            val handlerCoroutine = EventHandlerCoroutine(continuation)
            check(activeHandler == null) { "handler already registered." }

            activeHandler = handlerCoroutine
            block.createCoroutine(handlerCoroutine, handlerCoroutine).resume(Unit)


            continuation.invokeOnCancellation { handlerCoroutine.cancel(it) }
        }

    private fun dispatchUpdateEvent(deltaTime: Float) {
        val current = activeHandler
        current?.doUpdate(deltaTime)
    }

    fun performUpdate(deltaTime: Float) {
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
    ) : AwaitUpdateEventScope,
        Continuation<R> {

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
