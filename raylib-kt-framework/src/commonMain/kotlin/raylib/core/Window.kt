package raylib.core

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
    disableBackfaceCulling: Boolean = false,
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

    if (disableBackfaceCulling) rlDisableBackfaceCulling()

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
