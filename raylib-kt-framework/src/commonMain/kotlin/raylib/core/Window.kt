package raylib.core

import kotlinx.cinterop.CValue
import raylib.interop.BeginDrawing
import raylib.interop.ClearBackground
import raylib.interop.CloseWindow
import raylib.interop.EndDrawing

var isDirty = false

fun window(
    title: String,
    width: Int,
    height: Int,
    initialFps: Int = 60,
    initialBackGroundColor: CValue<Color>? = null,
    initContext: ContextRegistry.() -> Unit = {},
    block: ComponentRegistry.() -> Unit
) {
    val windowFunction = WindowFunction(
        initialFps = initialFps,
        title = title,
        screenWidth = width,
        screenHeight = height,
        backGroundColor = initialBackGroundColor
    )

    val contextRegistry = ContextRegistryImpl()
    contextRegistry.put<WindowContext>(WindowContext(windowFunction))
    contextRegistry.put(GameContext(windowFunction))
    contextRegistry.put(DrawContext(windowFunction))
    contextRegistry.initContext()

    val rootComponent = RootComponent(contextRegistry, block)
    with(rootComponent) {
        buildComponents()

        try {
            windowFunction.gameLoop {
                // update state
                this.performUpdate(windowFunction.frameTimeSeconds)

                if (isDirty) {
                    buildComponents()
                    rootComponent.dumpDebugInfo()

                    isDirty = false
                }

                // Draw
                BeginDrawing()
                windowFunction.backGroundColor?.let {
                    ClearBackground(it)
                }
                this.performDraw()
                EndDrawing()
            }
        } finally {
            dispose()
        }
    }
}

private fun WindowFunction.gameLoop(
    block: () -> Unit
) {
    while (!shouldExit()) {
        block()
    }
    CloseWindow()
}
