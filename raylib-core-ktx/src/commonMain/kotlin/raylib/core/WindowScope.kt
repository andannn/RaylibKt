package raylib.core

import kotlinx.cinterop.CValue

interface WindowScope : WindowFunction {
    fun disposeOnClose(disposable: Disposable)

    fun gameComponent(block: GameComponentScope.() -> LoopHandler): GameComponent
}

fun window(
    title: String,
    width: Int,
    height: Int,
    initialFps: Int = 60,
    initialBackGroundColor: CValue<Color>? = null,
    block: WindowScope.() -> Unit
): WindowScope {
    val windowFunction = DefaultWindowFunction(
        initialFps = initialFps,
        title = title,
        screenWidth = width,
        screenHeight = height
    )
    val windowScope = DefaultWindowScope(windowFunction)
    return windowScope
        .apply(block)
        .apply {
            val drawScope = DrawScope(this)
            val gameContext = GameScope(initialBackGroundColor, this)

            gameLoop {
                with(gameContext) {
                    // update state
                    performUpdate()

                    // Draw
                    raylib.interop.BeginDrawing()
                    backGroundColor?.let {
                        raylib.interop.ClearBackground(it)
                    }
                    with(drawScope) {
                        performDraw()
                    }
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
    internal val gameComponents = mutableListOf<GameComponent>()

    override fun disposeOnClose(disposable: Disposable) {
        disposables.add(disposable)
    }

    override fun gameComponent(block: GameComponentScope.() -> LoopHandler): GameComponent {
        val scope = GameComponentScope()
        val handler = block(scope)
        return GameComponent(handler, scope).also { gameComponents.add(it) }
    }

    fun GameScope.performUpdate() {
        gameComponents.forEach { handler ->
            with(handler) { update() }
        }
    }

    fun DrawScope.performDraw() {
        gameComponents.forEach { handler ->
            with(handler) { draw() }
        }
    }

    fun dispose() {
        gameComponents.forEach { it.dispose() }
        gameComponents.clear()

        disposables.forEach { it.dispose() }
        disposables.clear()
    }
}
