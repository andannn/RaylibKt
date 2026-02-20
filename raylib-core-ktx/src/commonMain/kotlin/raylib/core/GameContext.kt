package raylib.core

fun WindowContext.gameLoop(block: GameContext.() -> Unit) {
    while (!raylib.interop.WindowShouldClose()) {
        block(GameContext(this))
    }
    raylib.interop.CloseWindow()
}

interface GameContext : WindowContext, KeyboardFunction, MouseFunction,TouchFunction

fun GameContext(
    windowContext: WindowContext,
    keyboardFunction: KeyboardFunction = KeyboardFunction(),
    mouseFunction: MouseFunction = MouseFunction(),
    touchFunction: TouchFunction = TouchFunction(),
): GameContext {
    return DefaultGameContext(windowContext, keyboardFunction, mouseFunction, touchFunction)
}

private class DefaultGameContext(
    windowContext: WindowContext,
    keyboardFunction: KeyboardFunction,
    mouseFunction: MouseFunction,
    touchFunction: TouchFunction,
) : GameContext,
    WindowContext by windowContext,
    KeyboardFunction by keyboardFunction,
    MouseFunction by mouseFunction,
    TouchFunction by touchFunction
