package raylib.core

import kotlinx.cinterop.CValue

fun WindowScope.loadRenderTexture(
    width: Int,
    height: Int,
): CValue<RenderTexture2D> {
    val texture = raylib.interop.LoadRenderTexture(width, height)
    disposeOnClose {
        raylib.interop.UnloadRenderTexture(texture)
    }
    return texture
}

interface TextureDrawFunction {
    fun drawTexture(
        texture: CValue<Texture2D>,
        source: CValue<Rectangle>,
        position: CValue<Vector2>,
        tint: CValue<Color>
    )

}

fun TextureDrawFunction(): TextureDrawFunction {
    return DefaultTextureDrawFunction()
}

private class DefaultTextureDrawFunction : TextureDrawFunction {
    override fun drawTexture(
        texture: CValue<Texture2D>,
        source: CValue<Rectangle>,
        position: CValue<Vector2>,
        tint: CValue<Color>
    ) {
        raylib.interop.DrawTextureRec(texture, source, position, tint)
    }
}