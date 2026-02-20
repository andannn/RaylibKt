package raylib.core

fun WindowContext.mainGameLoop(block: GameContext.() -> Unit) {
    while (!raylib.interop.WindowShouldClose()) {
        block(GameContext(this))
    }
    raylib.interop.CloseWindow()
}

interface GameContext : WindowContext, KeyboardFunction, MouseFunction

fun GameContext(
    windowContext: WindowContext,
    keyboardFunction: KeyboardFunction = KeyboardFunction(),
    mouseFunction: MouseFunction = MouseFunction(),
): GameContext {
    return DefaultGameContext(windowContext, keyboardFunction, mouseFunction)
}

private class DefaultGameContext(
    windowContext: WindowContext,
    keyboardFunction: KeyboardFunction,
    mouseFunction: MouseFunction,
) : GameContext,
    WindowContext by windowContext,
    KeyboardFunction by keyboardFunction,
    MouseFunction by mouseFunction
