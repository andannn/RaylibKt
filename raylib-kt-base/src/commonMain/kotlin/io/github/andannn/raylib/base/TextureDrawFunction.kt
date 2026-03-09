/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.base

import kotlinx.cinterop.CValue
import raylib.interop.DrawTexture
import raylib.interop.DrawTexturePro
import raylib.interop.DrawTextureRec


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
        DrawTextureRec(texture, source, position, tint)
    }

    override fun drawTexture(
        texture: CValue<Texture2D>,
        source: CValue<Rectangle>,
        dest: CValue<Rectangle>,
        origin: CValue<Vector2>,
        tint: CValue<Color>,
        rotation: Float,
    ) {
        DrawTexturePro(texture, source, dest, origin, rotation, tint)
    }

    override fun drawTexture(
        texture: CValue<Texture>,
        posX: Int,
        posY: Int,
        tint: CValue<Color>
    ) {
        DrawTexture(texture, posX, posY, tint)
    }
}