package raylib.core

interface WindowContext {
    val title: String
    val windowWidth: Int
    val windowHeight: Int
    var currentFps: Int
    val frameTimeSeconds: Float
}

fun window(
    title: String,
    initialFps: Int,
    width: Int,
    height: Int,
    block: WindowContext.() -> Unit
): WindowContext {
    return DefaultWindowContext(initialFps, title, width, height).apply(block)
}

internal class DefaultWindowContext(
    initialFps: Int,
    override val title: String,
    override val windowWidth: Int,
    override val windowHeight: Int
) : WindowContext {
    init {
        raylib.interop.InitWindow(windowWidth, windowHeight, title)
        raylib.interop.SetTargetFPS(initialFps)
    }

    override var currentFps: Int
        get() = raylib.interop.GetFPS()
        set(value) {
            if (raylib.interop.GetFPS() != value) {
                raylib.interop.SetTargetFPS(value)
            }
        }
    override val frameTimeSeconds: Float
        get() = raylib.interop.GetFrameTime()
}