package raylib.core

import kotlinx.cinterop.CValue
import kotlinx.cinterop.MemScope
import kotlinx.cinterop.NativePlacement
import kotlinx.cinterop.memScoped

@MustUseReturnValues
interface WindowScope : WindowFunction, NativePlacement {
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
        screenHeight = height
    )
    val windowScope = DefaultWindowScope(this, windowFunction)
    val drawScope = DrawScope(windowFunction)
    val gameScope = GameScope(windowScope, initialBackGroundColor)
    val componentsManager = windowScope.block()
    return windowScope
        .apply {
            windowFunction.gameLoop {
                onFrame()

                componentsManager.buildComponentsIfNeeded()

                with(gameScope) {
                    // update state
                    componentsManager.performUpdate(frameTimeSeconds, gameScope)

                    // Draw
                    raylib.interop.BeginDrawing()
                    backGroundColor?.let {
                        raylib.interop.ClearBackground(it)
                    }
                    componentsManager.performDraw(drawScope)
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

interface UpdateHandler {
    fun GameScope.update(deltaTime: Float)
}

interface DrawHandler {
    fun DrawScope.draw()
}

class LoopHandlerBuilder {
    private var updateActions = mutableListOf<(GameScope.(deltaTime: Float) -> Unit)>()
    private var drawActions = mutableListOf<(DrawScope.() -> Unit)>()

    fun onUpdate(block: GameScope.(deltaTime: Float) -> Unit) {
        updateActions.add(block)
    }

    fun onDraw(block: DrawScope.() -> Unit) {
        drawActions.add(block)
    }

    fun build(): LoopHandler = object : LoopHandler {
        override fun GameScope.update(deltaTime: Float) {
            updateActions.forEach {
                it.invoke(this, deltaTime)
            }
        }

        override fun DrawScope.draw() {
            drawActions.forEach {
                it.invoke(this)
            }
        }
    }
}


internal class DefaultWindowScope(
    memScope: MemScope,
    val windowFunction: WindowFunction,
) : WindowScope,
    WindowFunction by windowFunction,
    NativePlacement by memScope {
    private val disposableRegistry: DisposableRegistryImpl = DisposableRegistryImpl()
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
