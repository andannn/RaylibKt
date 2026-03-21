/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.tiled.util

import io.github.andannn.raylib.foundation.blendMode
import io.github.andannn.raylib.tiled.model.BlendMode

@PublishedApi
internal inline fun BlendMode.blend(
    crossinline block: () -> Unit
) {
    when (this) {
        BlendMode.NORMAL -> block()
        BlendMode.ADD -> blendMode(io.github.andannn.raylib.foundation.BlendMode.BLEND_ADDITIVE, block)
        BlendMode.MULTIPLY -> blendMode(io.github.andannn.raylib.foundation.BlendMode.BLEND_MULTIPLIED, block)
        BlendMode.SCREEN -> TODO()
        BlendMode.OVERLAY -> TODO()
        BlendMode.DARKEN -> TODO()
        BlendMode.LIGHTEN -> TODO()
        BlendMode.COLOR_DODGE -> TODO()
        BlendMode.COLOR_BURN -> TODO()
        BlendMode.HARD_LIGHT -> TODO()
        BlendMode.SOFT_LIGHT -> TODO()
        BlendMode.DIFFERENCE -> TODO()
        BlendMode.EXCLUSION -> TODO()
    }
}