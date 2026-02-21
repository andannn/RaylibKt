package raylib.core

fun WindowContext.gameLoop(block: GameContext.() -> Unit) {
    while (!raylib.interop.WindowShouldClose()) {
        block(GameContext(this))
    }
    raylib.interop.CloseWindow()
}

interface GameContext : WindowContext, KeyboardFunction, MouseFunction, GestureFunction {
    val frameTimeSeconds: Float
}

fun GameContext(
    windowContext: WindowContext,
    keyboardFunction: KeyboardFunction = KeyboardFunction(),
    mouseFunction: MouseFunction = MouseFunction(),
    gestureFunction: GestureFunction = TouchFunction(),
): GameContext {
    return DefaultGameContext(windowContext, keyboardFunction, mouseFunction, gestureFunction)
}

private class DefaultGameContext(
    windowContext: WindowContext,
    keyboardFunction: KeyboardFunction,
    mouseFunction: MouseFunction,
    gestureFunction: GestureFunction,
) : GameContext,
    WindowContext by windowContext,
    KeyboardFunction by keyboardFunction,
    MouseFunction by mouseFunction,
    GestureFunction by gestureFunction {
    override val frameTimeSeconds: Float
        get() = raylib.interop.GetFrameTime()
}
