package raylib.core

import kotlinx.cinterop.CValue


interface TextureDrawFunction {
    fun drawTexture(
        texture: CValue<Texture2D>,
        source: CValue<Rectangle>,
        position: CValue<Vector2>,
        tint: CValue<Color>
    )

    fun drawTexture(
        texture: CValue<Texture2D>,
        source: CValue<Rectangle>,
        dest: CValue<Rectangle>,
        origin: CValue<Vector2>,
        tint: CValue<Color>,
        rotation: Float = 0f,
    )

    fun drawTexture(
        texture: CValue<Texture>, posX: Int, posY: Int, tint: CValue<Color>
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

    override fun drawTexture(
        texture: CValue<Texture2D>,
        source: CValue<Rectangle>,
        dest: CValue<Rectangle>,
        origin: CValue<Vector2>,
        tint: CValue<Color>,
        rotation: Float,
    ) {
        raylib.interop.DrawTexturePro(texture, source, dest, origin, rotation, tint)
    }

    override fun drawTexture(
        texture: CValue<Texture>,
        posX: Int,
        posY: Int,
        tint: CValue<Color>
    ) {
        raylib.interop.DrawTexture(texture, posX, posY, tint)
    }
}