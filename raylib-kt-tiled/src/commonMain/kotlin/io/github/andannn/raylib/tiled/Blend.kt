/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.tiled

import io.github.andannn.raylib.base.BlendMode
import io.github.andannn.raylib.base.blendMode
import io.github.andannn.raylib.tiled.model.TiledBlendMode

@PublishedApi
internal inline fun TiledBlendMode.blend(
    crossinline block: () -> Unit
) {
    when (this) {
        TiledBlendMode.NORMAL -> block()
        TiledBlendMode.ADD -> blendMode(BlendMode.BLEND_ADDITIVE, block)
        TiledBlendMode.MULTIPLY -> blendMode(BlendMode.BLEND_MULTIPLIED, block)
        TiledBlendMode.SCREEN -> TODO()
        TiledBlendMode.OVERLAY -> TODO()
        TiledBlendMode.DARKEN -> TODO()
        TiledBlendMode.LIGHTEN -> TODO()
        TiledBlendMode.COLOR_DODGE -> TODO()
        TiledBlendMode.COLOR_BURN -> TODO()
        TiledBlendMode.HARD_LIGHT -> TODO()
        TiledBlendMode.SOFT_LIGHT -> TODO()
        TiledBlendMode.DIFFERENCE -> TODO()
        TiledBlendMode.EXCLUSION -> TODO()
    }
}