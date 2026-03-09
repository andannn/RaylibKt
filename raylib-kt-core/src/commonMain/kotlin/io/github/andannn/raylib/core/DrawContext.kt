/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.core

import io.github.andannn.raylib.base.BasicShapeDrawFunction
import io.github.andannn.raylib.base.Color
import io.github.andannn.raylib.base.RenderTexture
import io.github.andannn.raylib.base.TextDrawFunction
import io.github.andannn.raylib.base.TextureDrawFunction
import kotlinx.cinterop.CValue
import raylib.interop.BeginTextureMode
import raylib.interop.ClearBackground
import raylib.interop.EndTextureMode

inline fun textureDrawScope(
    texture: CValue<RenderTexture>,
    backGroundColor: CValue<Color>? = null,
    crossinline block: () -> Unit
): CValue<RenderTexture> {
    BeginTextureMode(texture)
    if (backGroundColor != null) {
        ClearBackground(backGroundColor)
    }
    block()
    EndTextureMode()
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

