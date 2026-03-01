package raylib.core

import kotlinx.cinterop.CValue
import kotlinx.cinterop.MemScope
import kotlinx.cinterop.NativePlacement
import kotlinx.cinterop.memScoped
import raylib.interop.BeginDrawing
import raylib.interop.ClearBackground
import raylib.interop.CloseWindow
import raylib.interop.EndDrawing
import kotlin.reflect.KClass
import kotlin.reflect.cast

@MustUseReturnValues
interface WindowContext : WindowFunction, ContextRegistry, DisposableRegistry {
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
    block: WindowContext.() -> ComponentManager
): WindowContext = memScoped {
    val windowFunction = WindowFunction(
        initialFps = initialFps,
        title = title,
        screenWidth = width,
        screenHeight = height,
        backGroundColor = initialBackGroundColor
    )
    val windowScope = DefaultWindowScope(this, windowFunction)
    val componentsManager = windowScope.block()
    return windowScope
        .apply {
            windowFunction.gameLoop {
                onFrame()

                componentsManager.buildComponentsIfNeeded()

                // update state
                componentsManager.performUpdate(frameTimeSeconds)

                // Draw
                BeginDrawing()
                backGroundColor?.let {
                    ClearBackground(it)
                }
                componentsManager.performDraw()
                EndDrawing()
            }
        }
        .also { windowScope ->
            windowScope.dispose()
        }
}

internal fun WindowFunction.gameLoop(
    block: () -> Unit
) {
    while (!shouldExit()) {
        block()
    }
    CloseWindow()
}

interface LoopHandler {
    fun update(deltaTime: Float)
    fun draw()
}

fun interface UpdateHandler {
    fun update(gameContext: GameContext, deltaTime: Float)
}

fun interface DrawHandler {
    fun draw(drawContext: DrawContext)
}

class LoopHandlerBuilder(
    private val windowFunction: WindowFunction,
    private val gameContext: GameContext = GameContext(windowFunction),
    private val drawContext: DrawContext = DrawContext(windowFunction),
) {
    private var updateActions = mutableListOf<UpdateHandler>()
    private var drawActions = mutableListOf<DrawHandler>()

    fun onUpdate(block: GameContext.(deltaTime: Float) -> Unit) {
        updateActions.add(block)
    }

    fun suspendingTask(block: suspend SuspendingUpdateEventScope.() -> Unit): TaskController {
        return SuspendingUpdateTask(gameContext, block).also { updateActions.add(it) }
    }

    fun onDraw(block: DrawContext.() -> Unit) {
        drawActions.add(block)
    }

    fun build(): LoopHandler = object : LoopHandler {
        override fun update(deltaTime: Float) {
            updateActions.forEach {
                it.update(gameContext, deltaTime)
            }
        }

        override fun draw() {
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
) : WindowContext,
    ContextRegistry by ContextRegistryImpl(),
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
