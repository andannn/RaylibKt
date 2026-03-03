package raylib.core

import kotlinx.cinterop.CValue
import raylib.interop.BeginDrawing
import raylib.interop.ClearBackground
import raylib.interop.CloseWindow
import raylib.interop.EndDrawing

@MustUseReturnValues
interface WindowContext : Context, WindowFunction, ContextRegistry {
    @IgnorableReturnValue
    fun postFrameCallback(action: () -> Unit): Disposable
    fun componentRegistry(block: ComponentRegistry.() -> Unit): ComponentManager
}

fun window(
    title: String,
    width: Int,
    height: Int,
    initialFps: Int = 60,
    initialBackGroundColor: CValue<Color>? = null,
    block: WindowContext.() -> ComponentManager
): WindowContext = run {
    val windowFunction = WindowFunction(
        initialFps = initialFps,
        title = title,
        screenWidth = width,
        screenHeight = height,
        backGroundColor = initialBackGroundColor
    )

    val contextRegistry = ContextRegistryImpl()
    val windowContext = WindowContextImpl(contextRegistry, windowFunction)
    with(contextRegistry) {
        put<WindowContext>(windowContext)
        put(GameContext(windowFunction))
        put(DrawContext(windowFunction))
    }

    val componentsManager = windowContext.block()
    return windowContext
        .apply {
            windowFunction.gameLoop {
                onFrame()

                componentsManager.beforeFrame()

                // update state
                componentsManager.performUpdate(frameTimeSeconds)

                // Draw
                BeginDrawing()
                backGroundColor?.let {
                    ClearBackground(it)
                }
                componentsManager.performDraw()
                EndDrawing()

                componentsManager.endFrame()
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

internal class WindowContextImpl(
    contextRegistry: ContextRegistry,
    windowFunction: WindowFunction,
) : WindowContext,
    WindowFunction by windowFunction,
    ContextRegistry by contextRegistry {
    private val disposableRegistry: DisposableRegistryImpl = DisposableRegistryImpl()
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

    override fun componentRegistry(block: ComponentRegistry.() -> Unit): ComponentManager {
        return ComponentRegistryImpl(
            this,
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
