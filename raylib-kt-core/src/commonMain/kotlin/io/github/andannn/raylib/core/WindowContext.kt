/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.core

import io.github.andannn.raylib.base.AudioFunction
import io.github.andannn.raylib.base.FontFunction
import io.github.andannn.raylib.base.ImageFunction
import io.github.andannn.raylib.base.Music
import io.github.andannn.raylib.base.MusicStreamFunction
import io.github.andannn.raylib.base.WindowFunction

interface WindowContext : Context, WindowFunction, ImageFunction, FontFunction, AudioFunction {
    var renderPhase: RenderPhase
}

fun WindowContext(
    windowFunction: WindowFunction,
    fontFunction: FontFunction = FontFunction(),
    imageFunction: ImageFunction = ImageFunction(),
    audioFunction: AudioFunction = AudioFunction()
): WindowContext = WindowContextImpl(
    windowFunction,
    imageFunction,
    fontFunction,
    audioFunction
)

internal class WindowContextImpl(
    windowFunction: WindowFunction,
    imageFunction: ImageFunction,
    fontFunction: FontFunction,
    audioFunction: AudioFunction
) : WindowContext,
    WindowFunction by windowFunction,
    FontFunction by fontFunction,
    AudioFunction by audioFunction,
    ImageFunction by imageFunction {
    override var renderPhase = RenderPhase.UPDATE
}
