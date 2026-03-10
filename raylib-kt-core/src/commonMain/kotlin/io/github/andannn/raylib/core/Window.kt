/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.core

import io.github.andannn.raylib.base.Color
import io.github.andannn.raylib.base.WindowFunction
import kotlinx.cinterop.CValue
import raylib.interop.BeginDrawing
import raylib.interop.ClearBackground
import raylib.interop.CloseWindow
import raylib.interop.EndDrawing
import raylib.interop.rlDisableBackfaceCulling

fun window(
    title: String,
    width: Int,
    height: Int,
    initialFps: Int = 60,
    initialBackGroundColor: CValue<Color>? = null,
    init: ContextRegistry.() -> Unit = {},
    block: ComponentRegistry.() -> Unit
) {
    val windowFunction = WindowFunction(
        initialFps = initialFps,
        title = title,
        screenWidth = width,
        screenHeight = height,
        backGroundColor = initialBackGroundColor
    )

    val windowContext = WindowContext(windowFunction)
    val gameContext = GameContext()
    val drawContext = DrawContext()
    val contextRegistry = ContextRegistryInternal()
    contextRegistry.provideStaticDependency(windowContext)
    contextRegistry.provideStaticDependency(gameContext)
    contextRegistry.provideStaticDependency(drawContext)

    init.invoke(contextRegistry)

    with(RootComponent(contextRegistry, windowContext, block)) {
        try {
            windowFunction.gameLoop {
                // update state
                windowContext.renderPhase = RenderPhase.SYNC
                buildComponents()

                // update state
                windowContext.renderPhase = RenderPhase.UPDATE
                buildComponents()

                // Draw
                windowContext.renderPhase = RenderPhase.DRAW
                BeginDrawing()
                windowFunction.backGroundColor?.let {
                    ClearBackground(it)
                }
                buildComponents()
                EndDrawing()
            }
        } finally {
            dispose()
        }
    }
}

enum class RenderPhase {
    SYNC,
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
