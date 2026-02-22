package raylib.core

import kotlinx.cinterop.CValue

abstract class GameScope(
    initialBackGroundColor: CValue<Color>?,
) : WindowFunction, KeyboardFunction, MouseFunction, GestureFunction {
    var backGroundColor: CValue<Color>? = null

    init {
        backGroundColor = initialBackGroundColor
    }
}

internal fun GameScope(
    initialBackGroundColor: CValue<Color>? = null,
    windowScope: WindowScope,
    keyboardFunction: KeyboardFunction = KeyboardFunction(),
    mouseFunction: MouseFunction = MouseFunction(),
    gestureFunction: GestureFunction = GestureFunction(),
    drawFunction: DrawFunction = DrawFunction()
): GameScope = object : GameScope(initialBackGroundColor),
    WindowScope by windowScope,
    KeyboardFunction by keyboardFunction,
    MouseFunction by mouseFunction,
    GestureFunction by gestureFunction,
    DrawFunction by drawFunction {}
