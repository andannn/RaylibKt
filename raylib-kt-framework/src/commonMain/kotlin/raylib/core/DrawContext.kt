package raylib.core

import kotlinx.cinterop.CValue

inline fun textureDrawScope(
    texture: CValue<RenderTexture>,
    backGroundColor: CValue<Color>? = null,
    crossinline block: () -> Unit
): CValue<RenderTexture> {
    raylib.interop.BeginTextureMode(texture)
    if (backGroundColor != null) {
        raylib.interop.ClearBackground(backGroundColor)
    }
    block()
    raylib.interop.EndTextureMode()
    return texture
}

interface DrawContext : Context, BasicShapeDrawFunction, TextDrawFunction, TextureDrawFunction

internal fun DrawContext(
    basicShapeDrawFunction: BasicShapeDrawFunction = BasicShapeDrawFunction(),
    textDrawFunction: TextDrawFunction = TextDrawFunction(),
    textureDrawFunction: TextureDrawFunction = TextureDrawFunction(),
): DrawContext = object : DrawContext,
    BasicShapeDrawFunction by basicShapeDrawFunction,
    TextDrawFunction by textDrawFunction,
    TextureDrawFunction by textureDrawFunction{}

