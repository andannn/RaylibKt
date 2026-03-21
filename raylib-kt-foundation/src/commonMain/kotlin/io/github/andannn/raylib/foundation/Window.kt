/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.foundation

import io.github.andannn.raylib.runtime.ComponentRegistry
import io.github.andannn.raylib.runtime.ContextRegistry
import io.github.andannn.raylib.runtime.Rebuildable
import io.github.andannn.raylib.runtime.provideStaticDependency
import kotlinx.cinterop.CValue
import raylib.interop.BeginDrawing
import raylib.interop.ClearBackground
import raylib.interop.CloseWindow
import raylib.interop.EndDrawing

fun window(
    title: String,
    width: Int,
    height: Int,
    initialFps: Int = 60,
    isDebug: Boolean = false,
    initialBackGroundColor: CValue<Color>? = null,
    init: ContextRegistry.() -> Unit = {},
    block: ComponentRegistry.() -> Unit
) {
    val windowFunction = WindowFunction(
        initialFps = initialFps,
        title = title,
        screenWidth = width,
        screenHeight = height,
        backGroundColor = initialBackGroundColor,
        isDebug = isDebug,
    )

    val windowContext = WindowContext(windowFunction)
    val gameContext = GameContext(windowContext)
    val drawContext = DrawContext(windowContext)
    val root =Rebuildable(isDebug, block)
    root.provideStaticDependency(windowContext)
    root.provideStaticDependency(gameContext)
    root.provideStaticDependency(drawContext)

    init.invoke(root)

    windowContext.initAudioDevice()
    with(root) {
        try {
            windowFunction.gameLoop {
                // update state
                windowContext.renderPhase = RenderPhase.UPDATE
                rebuild()

                // Draw
                windowContext.renderPhase = RenderPhase.DRAW
                BeginDrawing()
                windowFunction.backGroundColor?.let {
                    ClearBackground(it)
                }
                rebuild()
                EndDrawing()
            }
        } finally {
            dispose()
        }
    }
    windowContext.closeAudioDevice()
}

enum class RenderPhase {
    UPDATE,
    DRAW
}

private fun WindowFunction.gameLoop(
    block: () -> Unit
) {
    while (!shouldExit()) {
        block()
    }
    CloseWindow()
}
