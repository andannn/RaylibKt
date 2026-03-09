/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package raylib.core

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
    initialBackGroundColor: CValue<Color>? = null,
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
    with(RootComponent(contextRegistry, windowContext, block)) {
        try {
            windowFunction.gameLoop {
                contextRegistry.provide<WindowContext>(windowContext) {
                    contextRegistry.provide(gameContext) {
                        contextRegistry.provide(drawContext) {
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
                    }
                }
            }
        } finally {
            dispose()
        }
    }
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
