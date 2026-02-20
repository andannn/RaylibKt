package raylib.core

import kotlinx.cinterop.CValue

interface DrawFunction : BasicShapeDrawFunction, TextDrawFunction

fun GameContext.drawScope(backGroundColor: CValue<Color>? = null, block: DrawFunction.() -> Unit) {
    raylib.interop.BeginDrawing()
    if (backGroundColor != null) {
        raylib.interop.ClearBackground(backGroundColor)
    }
    block(DrawFunction())
    raylib.interop.EndDrawing()
}

fun DrawFunction(
    basicShapeDrawFunction: BasicShapeDrawFunction = BasicShapeDrawFunction(),
    textDrawFunction: TextDrawFunction = TextDrawFunction()
): DrawFunction {
    return DefaultDrawFunction(basicShapeDrawFunction, textDrawFunction)
}

private class DefaultDrawFunction(
    basicShapeDrawFunction: BasicShapeDrawFunction,
    textDrawFunction: TextDrawFunction
) : DrawFunction, BasicShapeDrawFunction by basicShapeDrawFunction,
    TextDrawFunction by textDrawFunction


