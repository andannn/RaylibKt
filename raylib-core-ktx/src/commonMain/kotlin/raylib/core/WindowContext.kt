package raylib.core

import kotlinx.cinterop.CValue
import kotlinx.cinterop.MemScope
import kotlinx.cinterop.NativePlacement
import kotlinx.cinterop.memScoped

interface WindowContext : NativePlacement {
    val title: String
    val screenWidth: Int
    val screenHeight: Int
    var currentFps: Int
    val frameTimeSeconds: Float

    fun ConfigFlags.isEnabled(): Boolean
    fun ConfigFlags.clear()
    fun ConfigFlags.set()
    fun setExitKey(key: KeyboardKey)
    fun interceptExitKey(intercept: Boolean)
    fun requestExit()
    fun disposeOnClose(disposable: Disposable)
    fun toggleFullScreen()
    fun toggleBorderlessWindowed()
    fun minimizeWindow()
    fun maximizeWindow()
    fun restoreWindow()
    fun gameLoopEffect(block: GameLoopEffectScope.() -> Unit)
}

fun window(
    title: String,
    width: Int,
    height: Int,
    initialFps: Int = 60,
    initialBackGroundColor: CValue<Color>? = null,
    block: WindowContext.() -> Unit
): WindowContext =
    memScoped {
        DefaultWindowContext(
            memoScope = this,
            initialFps = initialFps,
            title = title,
            screenWidth = width,
            screenHeight = height
        )
            .apply(block)
            .apply {
                val drawScope = DrawScope(this)
                val gameContext = GameScope(initialBackGroundColor, this)

                gameLoop {
                    with(gameContext) {
                        // update state
                        onUpdate()

                        // Draw
                        raylib.interop.BeginDrawing()
                        backGroundColor?.let {
                            raylib.interop.ClearBackground(it)
                        }
                        with(drawScope) {
                            onDraw()
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

class GameLoopEffectScope {
    internal var loopHandlers = mutableListOf<UpdateHandler>()
    internal var drawHandlers = mutableListOf<DrawHandler>()

    fun onUpdate(onUpdateHandle: GameScope.() -> Unit) {
        loopHandlers.add(
            object : UpdateHandler {
                override fun GameScope.update() {
                    onUpdateHandle()
                }
            }
        )
    }

    fun onDraw(onDrawHandle: DrawScope.() -> Unit) {
        drawHandlers.add(
            object : DrawHandler {
                override fun DrawScope.draw() {
                    this.onDrawHandle()
                }
            }
        )
    }
}

internal fun DefaultWindowContext.gameLoop(
    block: WindowContext.() -> Unit
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

internal interface UpdateHandler {
    fun GameScope.update()
}

internal interface DrawHandler {
    fun DrawScope.draw()
}

internal class DefaultWindowContext(
    memoScope: MemScope,
    initialFps: Int,
    override val title: String,
    override val screenWidth: Int,
    override val screenHeight: Int,
) : WindowContext, NativePlacement by memoScope {
    private val disposables = mutableListOf<Disposable>()
    internal val updateHandlers = mutableListOf<UpdateHandler>()
    internal val drawHandlers = mutableListOf<DrawHandler>()

    internal var interceptExitKey = false
    internal var exitWindowRequest = false

    init {
        raylib.interop.InitWindow(screenWidth, screenHeight, title)
        raylib.interop.SetTargetFPS(initialFps)
    }

    override val frameTimeSeconds: Float
        get() = raylib.interop.GetFrameTime()

    override fun ConfigFlags.isEnabled(): Boolean {
       return raylib.interop.IsWindowState(value)
    }

    override fun ConfigFlags.clear() {
        raylib.interop.ClearWindowState(value)
    }

    override fun ConfigFlags.set() {
        raylib.interop.SetWindowState(value)
    }

    override fun setExitKey(key: KeyboardKey) {
        raylib.interop.SetExitKey(key.value.toInt())
    }

    override fun interceptExitKey(intercept: Boolean) {
        interceptExitKey = intercept
    }

    override fun requestExit() {
        exitWindowRequest = true
    }

    override var currentFps: Int
        get() = raylib.interop.GetFPS()
        set(value) {
            if (raylib.interop.GetFPS() != value) {
                raylib.interop.SetTargetFPS(value)
            }
        }

    override fun disposeOnClose(disposable: Disposable) {
        disposables.add(disposable)
    }

    override fun toggleFullScreen() {
        raylib.interop.ToggleFullscreen()
    }

    override fun toggleBorderlessWindowed() {
        raylib.interop.ToggleBorderlessWindowed()
    }

    override fun minimizeWindow() {
        raylib.interop.MinimizeWindow()
    }

    override fun maximizeWindow() {
        raylib.interop.MaximizeWindow()
    }

    override fun restoreWindow() {
        raylib.interop.RestoreWindow()
    }

    override fun gameLoopEffect(block: GameLoopEffectScope.() -> Unit) {
        val scope = GameLoopEffectScope()
        block(scope)
        scope.loopHandlers.forEach { updateHandlers.add(it) }
        scope.drawHandlers.forEach { drawHandlers.add(it) }
    }

    fun GameScope.onUpdate() {
        updateHandlers.forEach { handler ->
            with(handler) { update() }
        }
    }

    fun DrawScope.onDraw() {
        drawHandlers.forEach { handler ->
            with(handler) { draw() }
        }
    }

    fun dispose() {
        disposables.clear()
        updateHandlers.clear()

        disposables.forEach { it.dispose() }
    }
}
