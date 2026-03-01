package raylib.core

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

@RestrictsSuspension
interface SuspendingUpdateEventScope {
    suspend fun <R> awaitUpdateEventScope(block: suspend AwaitUpdateEventScope.() -> R): R
}

@RestrictsSuspension
interface AwaitUpdateEventScope : GameContext {
    /**
     *
     */
    suspend fun awaitUpdateEvent(): Float
}

interface TaskController {
    fun start()
    fun stop()
}

internal class FinishHandleInputException : CancellationException(message = "task is cancelled by user")

internal class SuspendingUpdateTask(
    private val gameContext: GameContext,
    private val block: suspend SuspendingUpdateEventScope.() -> Unit,
) : SuspendingUpdateEventScope, UpdateHandler, TaskController {
    private var activeHandler: EventHandlerCoroutine<*>? = null
    private var isInDispatch = false
    private var newHandlerRegisteredInDispatch = false

    override suspend fun <R> awaitUpdateEventScope(block: suspend AwaitUpdateEventScope.() -> R): R =
        suspendCancellableCoroutine { continuation ->
            val handlerCoroutine = EventHandlerCoroutine(continuation, gameContext)
            check(activeHandler == null) { "handler already registered." }

            activeHandler = handlerCoroutine
            block.createCoroutine(handlerCoroutine, handlerCoroutine).resume(Unit)

            if (isInDispatch) newHandlerRegisteredInDispatch = true

            continuation.invokeOnCancellation { handlerCoroutine.cancel(it) }
        }

    private fun dispatchUpdateEvent(deltaTime: Float) {
        if (activeHandler == null) return

        isInDispatch = true

        do {
            newHandlerRegisteredInDispatch = false
            val current = activeHandler
            current?.doUpdate(deltaTime)
        } while (newHandlerRegisteredInDispatch)

        isInDispatch = false
    }

    override fun update(gameContext: GameContext, deltaTime: Float) {
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

