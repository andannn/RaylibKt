package raylib.core

import kotlinx.cinterop.CValue

interface WindowScope : WindowFunction {
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
): WindowScope {
    val windowFunction = DefaultWindowFunction(
        initialFps = initialFps,
        title = title,
        screenWidth = width,
        screenHeight = height
    )
    val windowScope = DefaultWindowScope(windowFunction)
    val drawScope = DrawScope(windowFunction)
    val gameScope = GameScope(windowScope, initialBackGroundColor)
    val componentsManager = windowScope.block()
    return windowScope
        .apply {
            gameLoop {
                componentsManager.buildComponentsIfNeeded()
                with(gameScope) {
                    // update state
                    componentsManager.performUpdate(gameScope)

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
    fun GameScope.update()
}

interface DrawHandler {
    fun DrawScope.draw()
}

class LoopHandlerBuilder {
    private var updateAction: (GameScope.() -> Unit)? = null
    private var drawAction: (DrawScope.() -> Unit)? = null

    fun onUpdate(block: GameScope.() -> Unit) {
        check(updateAction == null)
        updateAction = block
    }

    fun onDraw(block: DrawScope.() -> Unit) {
        check(drawAction == null)
        drawAction = block
    }

    fun build(): LoopHandler = object : LoopHandler {
        override fun GameScope.update() {
            updateAction?.invoke(this)
        }

        override fun DrawScope.draw() {
            drawAction?.invoke(this)
        }
    }
}

internal class DefaultWindowScope(
    val windowFunction: DefaultWindowFunction
) : WindowScope, WindowFunction by windowFunction {
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
            onRebuildFinished = { isDirty = false},
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
