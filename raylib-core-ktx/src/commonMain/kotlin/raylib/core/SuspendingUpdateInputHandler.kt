package raylib.core

import kotlinx.atomicfu.locks.SynchronizedObject
import kotlinx.atomicfu.locks.synchronized
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.RestrictsSuspension
import kotlin.coroutines.createCoroutine
import kotlin.coroutines.resume

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

internal class SuspendingUpdateInputHandler(
    private val scope: CoroutineScope,
    private val block: suspend SuspendingUpdateEventScope.() -> Unit
) : SuspendingUpdateEventScope, UpdateHandler {
    private var updateInputJob: Job? = null

    private val synchronizedObject: SynchronizedObject = SynchronizedObject()
    private val pointerHandlers =
        mutableListOf<EventHandlerCoroutine<*>>()

    private var gameContext: GameContext? = null
    override suspend fun <R> awaitUpdateEventScope(block: suspend AwaitUpdateEventScope.() -> R): R =
        suspendCancellableCoroutine { continuation ->
            val handlerCoroutine = EventHandlerCoroutine(continuation, gameContext!!)
            synchronized(synchronizedObject) {
                pointerHandlers += handlerCoroutine

                block.createCoroutine(handlerCoroutine, handlerCoroutine).resume(Unit)
            }

            continuation.invokeOnCancellation { handlerCoroutine.cancel(it) }
        }

    private fun dispatchUpdateEvent(deltaTime: Float) {
        pointerHandlers.forEach {
            it.doUpdate(deltaTime)
        }
    }

    override fun update(gameContext: GameContext, deltaTime: Float) {
        this.gameContext = gameContext
        if (updateInputJob == null) {
            updateInputJob = scope.launch(start = CoroutineStart.UNDISPATCHED) {
                block.invoke(this@SuspendingUpdateInputHandler)
            }
        }

        dispatchUpdateEvent(deltaTime)
    }

    private inner class EventHandlerCoroutine<R>(
        private val completion: Continuation<R>,
        gameContext: GameContext
    ) : AwaitUpdateEventScope,
        Continuation<R>,
        GameContext by gameContext {

        private var awaiter: CancellableContinuation<Float>? = null

        override fun resumeWith(result: Result<R>) {
            synchronized(synchronizedObject) { pointerHandlers -= this }
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
