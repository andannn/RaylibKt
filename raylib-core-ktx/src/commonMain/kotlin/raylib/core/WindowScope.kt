package raylib.core

import kotlinx.cinterop.CValue
import kotlinx.cinterop.MemScope
import kotlinx.cinterop.NativePlacement
import kotlinx.cinterop.memScoped
import kotlinx.coroutines.CoroutineScope

@MustUseReturnValues
interface WindowScope : WindowFunction, DisposableRegistry {
    @IgnorableReturnValue
    fun postFrameCallback(action: () -> Unit): Disposable
    fun invalidComponents()
    fun componentRegistry(block: ComponentFactory.() -> Unit): ComponentManager
}

fun window(
    title: String,
    width: Int,
    height: Int,
    initialFps: Int = 60,
    initialBackGroundColor: CValue<Color>? = null,
    block: WindowScope.() -> ComponentManager
): WindowScope = memScoped {
    val windowFunction = DefaultWindowFunction(
        initialFps = initialFps,
        title = title,
        screenWidth = width,
        screenHeight = height,
        backGroundColor = initialBackGroundColor
    )
    val windowScope = DefaultWindowScope(this, windowFunction)
    val drawContext = DrawContext(windowFunction)
    val gameContext = GameContext(windowScope)
    val componentsManager = windowScope.block()
    return windowScope
        .apply {
            windowFunction.gameLoop {
                onFrame()

                componentsManager.buildComponentsIfNeeded()

                with(gameContext) {
                    // update state
                    componentsManager.performUpdate(frameTimeSeconds, gameContext)

                    // Draw
                    raylib.interop.BeginDrawing()
                    backGroundColor?.let {
                        raylib.interop.ClearBackground(it)
                    }
                    componentsManager.performDraw(drawContext)
                    raylib.interop.EndDrawing()
                }
            }
        }
        .also { windowScope ->
            windowScope.dispose()
        }
}

internal fun DefaultWindowFunction.gameLoop(
    block: () -> Unit
) {
    fun shouldExit(): Boolean =
        if (interceptExitKey) {
            exitWindowRequest
        } else {
            raylib.interop.WindowShouldClose()
        }
    while (!shouldExit()) {
        block()
    }
    raylib.interop.CloseWindow()
}

interface LoopHandler : UpdateHandler, DrawHandler

fun interface UpdateHandler {
    fun update(gameContext: GameContext, deltaTime: Float)
}

fun interface DrawHandler {
    fun draw(drawContext: DrawContext)
}

class LoopHandlerBuilder(
    private val scope: CoroutineScope
) {
    private var updateActions = mutableListOf<UpdateHandler>()
    private var drawActions = mutableListOf<DrawHandler>()

    fun onUpdate(block: GameContext.(deltaTime: Float) -> Unit) {
        updateActions.add(block)
    }

    fun suspendingScope(block: suspend SuspendingUpdateEventScope.() -> Unit) {
        updateActions.add(SuspendingUpdateInputHandler(scope, block))
    }

    fun onDraw(block: DrawContext.() -> Unit) {
        drawActions.add(block)
    }

    fun build(): LoopHandler = object : LoopHandler {
        override fun update(gameContext: GameContext, deltaTime: Float) {
            updateActions.forEach {
                it.update(gameContext, deltaTime)
            }
        }

        override fun draw(drawContext: DrawContext) {
            drawActions.forEach {
                it.draw(drawContext)
            }
        }
    }
}


internal class DefaultWindowScope(
    memScope: MemScope,
    val windowFunction: WindowFunction,
    private val disposableRegistry: DisposableRegistryImpl = DisposableRegistryImpl()
) : WindowScope,
    WindowFunction by windowFunction,
    NativePlacement by memScope,
    DisposableRegistry by disposableRegistry {
    var isDirty = false

    private val callBacks = mutableListOf<FrameCallBack>()

    override fun postFrameCallback(action: () -> Unit): Disposable {
        val callBack = FrameCallBack(action) {
            callBacks.remove(it)
        }
        callBacks.add(callBack)
        return callBack
    }

    fun onFrame() {
        if (callBacks.isEmpty()) return

        val iterator = callBacks.iterator()
        while (iterator.hasNext()) {
            val listener = iterator.next()
            listener.action()
            iterator.remove()
        }
    }

    override fun invalidComponents() {
        isDirty = true
    }

    override fun componentRegistry(block: ComponentFactory.() -> Unit): ComponentManager {
        return ComponentManagerImpl(
            windowFunction,
            isDirty = { isDirty },
            onRebuildFinished = { isDirty = false },
            block
        ).also { componentManager ->
            disposableRegistry.disposeOnClose(componentManager)
        }
    }

    fun dispose() {
        disposableRegistry.dispose()
        callBacks.clear()
    }

    class FrameCallBack(val action: () -> Unit, val onDispose: (FrameCallBack) -> Unit) : Disposable {
        override fun dispose() = onDispose(this)
    }
}
