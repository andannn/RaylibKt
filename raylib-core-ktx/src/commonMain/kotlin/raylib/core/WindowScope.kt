package raylib.core

import kotlinx.cinterop.CValue
import kotlinx.cinterop.MemScope
import kotlinx.cinterop.NativePlacement
import kotlinx.cinterop.memScoped

interface WindowScope : WindowFunction, NativePlacement {
    fun invalidComponents()
    fun disposeOnClose(disposable: Disposable)
    fun registerGameComponents(block: GameComponentsRegisterScope.() -> Unit): GameComponentManager
}

fun window(
    title: String,
    width: Int,
    height: Int,
    initialFps: Int = 60,
    initialBackGroundColor: CValue<Color>? = null,
    block: WindowScope.() -> GameComponentManager
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
            gameLoop {
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
        .also { it.dispose() }
}

fun interface Disposable {
    fun dispose()
}

internal fun DefaultWindowScope.gameLoop(
    block: WindowScope.() -> Unit
) {
    fun shouldExit(): Boolean =
        if (windowFunction.interceptExitKey) {
            windowFunction.exitWindowRequest
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
    private var drawAction: (DrawScope.() -> Unit)? = null

    fun onUpdate(block: GameScope.(deltaTime: Float) -> Unit) {
        updateActions.add(block)
    }

    fun onDraw(block: DrawScope.() -> Unit) {
        check(drawAction == null)
        drawAction = block
    }

    fun build(): LoopHandler = object : LoopHandler {
        override fun GameScope.update(deltaTime: Float) {
            updateActions.forEach {
                it.invoke(this, deltaTime)
            }
        }

        override fun DrawScope.draw() {
            drawAction?.invoke(this)
        }
    }
}

internal class DefaultWindowScope(
    memScope: MemScope,
    val windowFunction: DefaultWindowFunction
) : WindowScope, WindowFunction by windowFunction, NativePlacement by memScope {
    private val disposables = mutableListOf<Disposable>()

    private var isDirty = false

    override fun invalidComponents() {
        isDirty = true
    }

    override fun disposeOnClose(disposable: Disposable) {
        disposables.add(disposable)
    }

    override fun registerGameComponents(block: GameComponentsRegisterScope.() -> Unit): GameComponentManager {
        return GameComponentManagerImpl(
            isDirty = { isDirty },
            onRebuildFinished = { isDirty = false },
            block
        ).also {
            disposables.add(it)
        }
    }

    fun dispose() {
        disposables.forEach { it.dispose() }
        disposables.clear()
    }
}
