package raylib.core

import kotlinx.cinterop.CValue
import kotlinx.cinterop.readValue

internal fun DrawScope(
    windowScope: WindowFunction,
    drawFunction: DrawFunction = DrawFunction(),
    mouseFunction: MouseFunction = MouseFunction(),
    gestureFunction: GestureFunction = GestureFunction()
): DrawScope = object : DrawScope, DrawFunction by drawFunction,
    MouseFunction by mouseFunction,
    WindowFunction by windowScope,
    GestureFunction by gestureFunction {}

interface DrawScope : DrawFunction, MouseFunction, WindowFunction, GestureFunction


interface DrawFunction : BasicShapeDrawFunction, TextDrawFunction, TextureDrawFunction


fun DrawFunction(
    basicShapeDrawFunction: BasicShapeDrawFunction = BasicShapeDrawFunction(),
    textDrawFunction: TextDrawFunction = TextDrawFunction(),
    textureDrawFunction: TextureDrawFunction = TextureDrawFunction()
): DrawFunction = object :
    DrawFunction, BasicShapeDrawFunction by basicShapeDrawFunction,
    TextDrawFunction by textDrawFunction,
    TextureDrawFunction by textureDrawFunction {}


inline fun mode2d(camera: Camera2D, crossinline block: () -> Unit) {
    mode2d(camera.readValue(), block)
}

inline fun mode2d(camera: CValue<Camera2D>, crossinline block: () -> Unit) {
    raylib.interop.BeginMode2D(camera)
    block()
    raylib.interop.EndMode2D()
}

inline fun DrawFunction.textureDrawScope(
    texture: CValue<RenderTexture>,
    backGroundColor: CValue<Color>? = null,
    crossinline block: DrawFunction.() -> Unit
): CValue<RenderTexture> {
    raylib.interop.BeginTextureMode(texture)
    if (backGroundColor != null) {
        raylib.interop.ClearBackground(backGroundColor)
    }
    block(this)
    raylib.interop.EndTextureMode()
    return texture
}


inline fun scissorMode(rectangle: Rectangle, enabled: Boolean = true, crossinline block: () -> Unit) {
    scissorMode(
        rectangle.x.toInt(),
        rectangle.y.toInt(),
        rectangle.width.toInt(),
        rectangle.height.toInt(),
        enabled,
        block
    )
}

inline fun scissorMode(
    x: Int,
    y: Int,
    width: Int,
    height: Int,
    enabled: Boolean = true,
    crossinline block: () -> Unit
) {
    if (enabled) raylib.interop.BeginScissorMode(x, y, width, height)
    block()
    if (enabled) raylib.interop.EndScissorMode()
}