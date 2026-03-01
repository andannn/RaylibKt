package raylib.gui

import raylib.core.ContextRegistry
import raylib.core.DrawContext
import raylib.core.LoopHandlerBuilder
import raylib.core.get

fun LoopHandlerBuilder.onDrawGui(block: GuiContext.() -> Unit) {
    onDraw {
        with(get<GuiContext>()) {
            block()
        }
    }
}


interface GuiContext : GuiFunction, DrawContext

fun ContextRegistry.GuiContext(): GuiContext =
    object : GuiContext, GuiFunction by GuiFunction(), DrawContext by get<DrawContext>() {}
