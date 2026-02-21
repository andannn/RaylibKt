package raylib.core

import kotlinx.cinterop.CValue
import kotlinx.cinterop.readValue

interface DrawFunction : BasicShapeDrawFunction, TextDrawFunction, TextureDrawFunction

fun GameContext.basicDrawScope(
    backGroundColor: CValue<Color>? = null,
    block: DrawFunction.() -> Unit
) {
    raylib.interop.BeginDrawing()
    if (backGroundColor != null) {
        raylib.interop.ClearBackground(backGroundColor)
    }
    block(DrawFunction())
    raylib.interop.EndDrawing()
}

fun GameContext.textureDrawScope(
    texture: CValue<RenderTexture>,
    backGroundColor: CValue<Color>? = null,
    block: DrawFunction.() -> Unit
): CValue<RenderTexture> {
    raylib.interop.BeginTextureMode(texture)
    if (backGroundColor != null) {
        raylib.interop.ClearBackground(backGroundColor)
    }
    block(DrawFunction())
    raylib.interop.EndTextureMode()
    return texture
}

fun DrawFunction(
    basicShapeDrawFunction: BasicShapeDrawFunction = BasicShapeDrawFunction(),
    textDrawFunction: TextDrawFunction = TextDrawFunction(),
    textureDrawFunction: TextureDrawFunction = TextureDrawFunction()
): DrawFunction {
    return DefaultDrawFunction(basicShapeDrawFunction, textDrawFunction, textureDrawFunction)
}

inline fun DrawFunction.mode2d(camera: Camera2D, block: () -> Unit) {
    mode2d(camera.readValue(), block)
}

inline fun DrawFunction.mode2d(camera: CValue<Camera2D>, block: () -> Unit) {
    raylib.interop.BeginMode2D(camera)
    block()
    raylib.interop.EndMode2D()
}

private class DefaultDrawFunction(
    basicShapeDrawFunction: BasicShapeDrawFunction,
    textDrawFunction: TextDrawFunction,
    textureDrawFunction: TextureDrawFunction
) : DrawFunction, BasicShapeDrawFunction by basicShapeDrawFunction,
    TextDrawFunction by textDrawFunction,
    TextureDrawFunction by textureDrawFunction



