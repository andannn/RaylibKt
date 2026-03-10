/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.core

import io.github.andannn.raylib.base.FontFunction
import io.github.andannn.raylib.base.ImageFunction
import io.github.andannn.raylib.base.WindowFunction

interface WindowContext : Context, WindowFunction, ImageFunction, FontFunction {
    var renderPhase: RenderPhase
}

fun WindowContext(
    windowFunction: WindowFunction,
    fontFunction: FontFunction = FontFunction(),
    imageFunction: ImageFunction = ImageFunction(),
): WindowContext = WindowContextImpl(
    windowFunction,
    imageFunction,
    fontFunction
)

internal class WindowContextImpl(
    windowFunction: WindowFunction,
    imageFunction: ImageFunction,
    fontFunction: FontFunction,
    ) : WindowContext,
    WindowFunction by windowFunction,
    FontFunction by fontFunction,
    ImageFunction by imageFunction {
    override var renderPhase = RenderPhase.SYNC
}
