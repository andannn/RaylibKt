package raylib.core

import kotlinx.cinterop.CValue

abstract class GameScope(
    initialBackGroundColor: CValue<Color>?,
) : WindowContext, KeyboardFunction, MouseFunction, GestureFunction {
    var backGroundColor: CValue<Color>? = null

    init {
        backGroundColor = initialBackGroundColor
    }
}

fun GameScope(
    initialBackGroundColor: CValue<Color>? = null,
    windowContext: WindowContext,
    keyboardFunction: KeyboardFunction = KeyboardFunction(),
    mouseFunction: MouseFunction = MouseFunction(),
    gestureFunction: GestureFunction = GestureFunction(),
    drawFunction: DrawFunction = DrawFunction()
): GameScope {
    return DefaultGameContext(
        initialBackGroundColor,
        windowContext,
        keyboardFunction,
        mouseFunction,
        gestureFunction,
        drawFunction
    )
}

private class DefaultGameContext(
    initialBackGroundColor: CValue<Color>?,
    windowContext: WindowContext,
    keyboardFunction: KeyboardFunction,
    mouseFunction: MouseFunction,
    gestureFunction: GestureFunction,
    drawFunction: DrawFunction
) : GameScope(initialBackGroundColor),
    WindowContext by windowContext,
    KeyboardFunction by keyboardFunction,
    MouseFunction by mouseFunction,
    GestureFunction by gestureFunction,
    DrawFunction by drawFunction
