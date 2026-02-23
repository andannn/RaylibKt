package raylib.core

interface WindowFunction {
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
    fun toggleFullScreen()
    fun toggleBorderlessWindowed()
    fun minimizeWindow()
    fun maximizeWindow()
    fun restoreWindow()
}

internal class DefaultWindowFunction(
    initialFps: Int,
    override val title: String,
    override val screenWidth: Int,
    override val screenHeight: Int,
) : WindowFunction {

    override val frameTimeSeconds: Float
        get() = raylib.interop.GetFrameTime()

    init {
        raylib.interop.InitWindow(screenWidth, screenHeight, title)
        raylib.interop.SetTargetFPS(initialFps)
    }

    internal var interceptExitKey = false
    internal var exitWindowRequest = false

    override fun interceptExitKey(intercept: Boolean) {
        interceptExitKey = intercept
    }

    override fun requestExit() {
        exitWindowRequest = true
    }

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

    override var currentFps: Int
        get() = raylib.interop.GetFPS()
        set(value) {
            if (raylib.interop.GetFPS() != value) {
                raylib.interop.SetTargetFPS(value)
            }
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
}

