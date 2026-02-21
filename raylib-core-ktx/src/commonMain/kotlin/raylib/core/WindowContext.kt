package raylib.core

interface WindowContext {
    val title: String
    val screenWidth: Int
    val screenHeight: Int
    var currentFps: Int
    val frameTimeSeconds: Float
}

fun window(
    title: String,
    width: Int,
    height: Int,
    initialFps: Int = 60,
    block: WindowContext.() -> Unit
): WindowContext {
    return DefaultWindowContext(initialFps, title, width, height).apply(block)
}

internal class DefaultWindowContext(
    initialFps: Int,
    override val title: String,
    override val screenWidth: Int,
    override val screenHeight: Int
) : WindowContext {
    init {
        raylib.interop.InitWindow(screenWidth, screenHeight, title)
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