package raylib.core

interface GameContext : Context, WindowFunction, KeyboardFunction, MouseFunction, GestureFunction, GamepadFunction

internal fun GameContext(
    windowScope: WindowFunction,
    keyboardFunction: KeyboardFunction = KeyboardFunction(),
    mouseFunction: MouseFunction = MouseFunction(),
    gestureFunction: GestureFunction = GestureFunction(),
    gamepadFunction: GamepadFunction = GamepadFunction()
): GameContext = object :
    GameContext,
    WindowFunction by windowScope,
    KeyboardFunction by keyboardFunction,
    MouseFunction by mouseFunction,
    GestureFunction by gestureFunction,
    GamepadFunction by gamepadFunction {}
