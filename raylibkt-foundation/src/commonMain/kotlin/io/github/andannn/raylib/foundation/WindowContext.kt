/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.foundation

import io.github.andannn.raylib.runtime.ComponentScope
import io.github.andannn.raylib.runtime.Context
import io.github.andannn.raylib.runtime.ContextProvider
import io.github.andannn.raylib.runtime.find

interface WindowContext : Context, WindowFunction, ImageFunction, FontFunction, AudioFunction {
    var renderPhase: RenderPhase
}

val ContextProvider.windowContext get() = find<WindowContext>()
val ContextProvider.screenHeight get() = find<WindowContext>().screenHeight
val ContextProvider.screenWidth get() = find<WindowContext>().screenWidth


inline fun ComponentScope.update(crossinline block: GameContext.(Float) -> Unit) {
    if (find<WindowContext>().renderPhase == RenderPhase.UPDATE) {
        block(find<GameContext>(), find<WindowContext>().frameTimeSeconds)
    }
}

inline fun ComponentScope.draw(crossinline block: DrawContext.() -> Unit) {
    if (find<WindowContext>().renderPhase == RenderPhase.DRAW) {
        block(find<DrawContext>())
    }
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
