package raylib.core

inline fun WindowContext.mainGameLoop(block: GameContext.() -> Unit) {
    while (!RlWindow.shouldClose()) {
        block(GameContext(this))
    }
    RlWindow.close()
}

interface GameContext : WindowContext, KeyboardFunction, MouseFunction

fun GameContext(
    windowContext: WindowContext,
    keyboardFunction: KeyboardFunction = KeyboardFunction(),
    mouseFunction: MouseFunction = MouseFunction()
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


inline fun GameContext.drawScope(block: DrawScope.() -> Unit) {
    DrawMode.begin()
    block(DrawScope)
    DrawMode.end()
}

inline fun DrawScope.textDrawScope(block: TextDrawScope.() -> Unit) {
    block(TextDrawScope)
}
