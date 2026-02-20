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
        RlWindow.init(windowWidth, windowHeight, title)
        RlTiming.setTargetFPS(initialFps)
    }

    override var currentFps: Int
        get() = RlTiming.getFPS()
        set(value) {
            if (RlTiming.getFPS() != value) {
                RlTiming.setTargetFPS(value)
            }
        }
    override val frameTimeSeconds: Float
        get() = RlTiming.getFrameTime()
}