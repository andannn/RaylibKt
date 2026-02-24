package raylib.core

import kotlinx.cinterop.CValue

abstract class GameScope(
    initialBackGroundColor: CValue<Color>?,
) : WindowFunction, KeyboardFunction, MouseFunction, GestureFunction, GamepadFunction {
    var backGroundColor: CValue<Color>? = null

    init {
        backGroundColor = initialBackGroundColor
    }
}

internal fun GameScope(
    windowScope: WindowFunction,
    initialBackGroundColor: CValue<Color>? = null,
    keyboardFunction: KeyboardFunction = KeyboardFunction(),
    mouseFunction: MouseFunction = MouseFunction(),
    gestureFunction: GestureFunction = GestureFunction(),
    drawFunction: DrawFunction = DrawFunction(),
    gamepadFunction: GamepadFunction = GamepadFunction()
): GameScope = object : GameScope(initialBackGroundColor),
    WindowFunction by windowScope,
    KeyboardFunction by keyboardFunction,
    MouseFunction by mouseFunction,
    GestureFunction by gestureFunction,
    GamepadFunction by gamepadFunction,
    DrawFunction by drawFunction {}
