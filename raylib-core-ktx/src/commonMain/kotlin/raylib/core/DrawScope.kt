package raylib.core

import kotlinx.cinterop.CValue
import kotlinx.cinterop.readValue

internal fun DrawScope(
    windowContext: WindowContext,
    drawFunction: DrawFunction = DrawFunction(),
    mouseFunction: MouseFunction = MouseFunction(),
    gestureFunction: GestureFunction = GestureFunction()
): DrawScope = object : DrawScope, DrawFunction by drawFunction,
    MouseFunction by mouseFunction,
    WindowContext by windowContext,
    GestureFunction by gestureFunction {}

interface DrawScope : DrawFunction, MouseFunction, WindowContext, GestureFunction


interface DrawFunction : BasicShapeDrawFunction, TextDrawFunction, TextureDrawFunction

fun DrawFunction.textureDrawScope(
    texture: CValue<RenderTexture>,
    backGroundColor: CValue<Color>? = null,
    block: DrawFunction.() -> Unit
): CValue<RenderTexture> {
    raylib.interop.BeginTextureMode(texture)
    if (backGroundColor != null) {
        raylib.interop.ClearBackground(backGroundColor)
    }
    block(this)
    raylib.interop.EndTextureMode()
    return texture
}

fun DrawFunction(
    basicShapeDrawFunction: BasicShapeDrawFunction = BasicShapeDrawFunction(),
    textDrawFunction: TextDrawFunction = TextDrawFunction(),
    textureDrawFunction: TextureDrawFunction = TextureDrawFunction()
): DrawFunction = object :
    DrawFunction, BasicShapeDrawFunction by basicShapeDrawFunction,
    TextDrawFunction by textDrawFunction,
    TextureDrawFunction by textureDrawFunction {}


inline fun mode2d(camera: Camera2D, block: () -> Unit) {
    mode2d(camera.readValue(), block)
}

inline fun mode2d(camera: CValue<Camera2D>, block: () -> Unit) {
    raylib.interop.BeginMode2D(camera)
    block()
    raylib.interop.EndMode2D()
}
